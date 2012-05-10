package pimp.persistence;

import java.awt.Color;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pimp.classloader.JarLoader;
import pimp.model.Product;

public class SqliteConnection extends DatabaseConnection {
	private Connection conn;
	private String databaseName;
	
	public SqliteConnection(String dbName) {
		setDatabase(dbName);
	}

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * Saves a product to DB.
	 * @return Whether or not the save was successful.
	 */
	@Override
	protected boolean save(Product product) {
		Class<?> c = product.getClass();
		String className = c.getName();
		className = className.replace('.', '_');
		if (!tableExists(className)) {	
			//If we haven't seen an object of this class before, then we need to create a new table
			createTable(c);
		}
		
		return insertIntoTable(className, product);
	}
	
	/**
	 * Inserts a product into the appropriate DB table.
	 * @param className
	 * @param product
	 * @return Whether or not the insert was successful.
	 */
	private boolean insertIntoTable(String className, Product product) {
		try {
			int originalCount = getRecordCount(className);
			Class<?> c = product.getClass();
			Field[] fields = c.getFields();
			fields = sortFields(fields);
			
			/*Construct DB insert PreparedStatement...*/
			String sql = "INSERT INTO " + className + "(";
			for (int i = 0; i < fields.length; i++) {
				sql += fields[i].getName();
				if (i < fields.length-1) {
					sql += ",";
				}
			}
			
			sql += ") values (";
			for (int i = 0; i < fields.length; i++) {
				sql += "?";
				if (i < fields.length-1) {
					sql += ",";
				}
			}
			sql += ");";

			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			for (int i = 0; i < fields.length; i++) {	//Populate the values of the PreparedStatement
				if (!fields[i].getName().equals("id")) {
					Object obj = fields[i].get(product);
					if (obj != null) {
						setPreparedStatementValues(preparedStatement, fields[i], i+1, product);	//preparedStatement args start from 1
					}
				}
			}
			
			//Execute the insert statement:
			preparedStatement.addBatch();
			preparedStatement.executeBatch();
			
			if (getRecordCount(className) == originalCount+1) {	//Number of records increased as a result of this method being called.
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			System.err.println("Error on database insert");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Sorts a Field array, since Class.getFields returns them in no particular order,
	 * and removes duplicate fields that occur when a subclass re-declares a public method.
	 */
	private Field[] sortFields(Field[] fields) {
		Comparator<Field> comp = new FieldComparator();
		List<Field> fieldList = new ArrayList<Field>();
		
		/* Remove duplicates */
		for (Field field : fields) {
			boolean add = true;
			for (Field listField : fieldList) {
				if (field.getName().equals(listField.getName())) {
					add = false;
				}
			}
			if (add) {
				fieldList.add(field);
			}
		}
		
		/* Sort the array, now that duplicates have been removed */
		Field[] newFields = new Field[fieldList.size()];
		int i = 0;
		for (Field field : fieldList) {
			newFields[i] = field;
			i++;
		}
		
		Arrays.sort(newFields, comp);
		
		return newFields;
	}

	/**
	 * 
	 * @param preparedStatement The PreparedStatement we want to set the values for
	 * @param field The field that contains the value we want to set
	 * @param index The position of the '?' in the preparedStatement that we want to replace
	 * @param product Used for retrieving the instance value of field.
	 */
	private void setPreparedStatementValues(PreparedStatement preparedStatement, 
											Field field, 
											int index,
											Product product) {
		
		try {
			Class<?> type = field.getType();
			String typeName = type.getSimpleName();
			
			/* Get the appropriate data representation to store */
			if (typeName.equals("String")) {
				preparedStatement.setString(index, field.get(product).toString());
			} else if (typeName.equals("Color")) {
				preparedStatement.setInt(index, ((Color)field.get(product)).getRGB());
			} else if (typeName.equals("int")) {
				preparedStatement.setInt(index, (Integer) field.get(product));
			} else if (typeName.equals("double")) {
				preparedStatement.setDouble(index, (Double) field.get(product));
			} else if (typeName.equals("Date")) {
				Date date = (Date)field.get(product);
				
				preparedStatement.setString(index, getDateString(date));
			}
		} catch (Exception e) {
			System.err.println("Error preparing statement.");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the number of records in the database table corresponding to a class name.
	 */
	private int getRecordCount(String className) {
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + className);
			if (rs.next()) {
				int count = rs.getInt("count(*)");
				rs.close();
				return count;
			} else {
				return 0;
			}
		} catch (Exception e) {
			System.err.println("error getting record count for table " + className);
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Creates a new database table based on the public fields available in a class.
	 * @param c
	 */
	private void createTable(Class<?> c) {
		String tableName = c.getName().replace('.', '_');
		try {
			Field[] fields = c.getFields();
			fields = sortFields(fields);
			
			/* Create the new table */
			Statement statement = conn.createStatement();
			statement.executeUpdate("CREATE TABLE " + tableName +
									" (id INTEGER PRIMARY KEY AUTOINCREMENT);");
			
			/* Alter the new table, adding the columns required to store objects of class c */
			for (Field field : fields) {
				Class<?> type = field.getType();
				String dbColType = getDbColType(type.getSimpleName());
				String columnName = field.getName();
				if (!columnName.equals("id")) {	//Id column has already been created.
					statement.executeUpdate("ALTER TABLE " + tableName +
											" ADD COLUMN " + columnName + 
											" " + dbColType + ";");
				}
			}
		} catch (Exception e) {
			System.err.println("Error on table creation of table " + tableName);
			e.printStackTrace();
		}
		
	}

	/**
	 * Returns the database column type that corresponds to a particular java datatype.
	 * @param javaTypeName
	 * @return
	 */
	private String getDbColType(String javaTypeName) {
		if (javaTypeName.equals("String")) {
			return "TEXT";
		} else if (javaTypeName.equals("int")) {
			return "INTEGER";
		} else if (javaTypeName.equals("double")) {
			return "REAL";
		} else if (javaTypeName.equals("Date")) {
			return "TEXT";	//SQLite doesn't have a datetime type. Will be of form "YYYY-MM-DD HH:MM:SS.SSS".
		} else if (javaTypeName.equals("Color")) {
			return "TEXT";
		}
		
		return null;
	}

	/**
	 * Checks whether a table has previously been created.
	 * @param className
	 * @return
	 */
	private boolean tableExists(String className) {
		className = className.replace('.', '_');
		boolean success = false;
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT name" +
									" FROM sqlite_master" +
									" WHERE type=\'table\'" +
									" AND name=\'" + className +"\';");
			if (rs.next()) {
				success = true;
				rs.close();
			}
		} catch (Exception e) {
			System.err.println("Error checking if table exists");
			e.printStackTrace();
		}
		
		return success;
	}

	/**
	 * Loads a list of all products from all tables in the database.
	 */
	@Override
	protected List<Product> loadProductList() {
		List<Product> products = new ArrayList<Product>();
		
		List<String> tableNames = getTableNames();
		
		for (String table : tableNames) {
			products.addAll(getProductsFromTable(table));
		}
		
		return products;
	}

	/**
	 * Gets all the products from the given table.
	 * @param tableName
	 * @return
	 */
	private List<Product> getProductsFromTable(String tableName) {
		List<Product> products = new ArrayList<Product>();
		
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT *" +
												  " FROM \'" + tableName + "\';");
			while (rs.next()) {
				Product newProduct = createProductFromResultSet(rs, tableName);
				products.add(newProduct);
			}
			rs.close();
		} catch (Exception e) {
			System.err.println("Error retreiving product from table " + tableName);
			e.printStackTrace();
		}
		return products;
	}

	/**
	 * Constructs a new product from data in a ResultSet. Reflection is used to get 
	 * the appropriate field corresponding to each table column.
	 * @param rs
	 * @param tableName
	 * @return the newly created product.
	 */
	private Product createProductFromResultSet(ResultSet rs, String tableName) {
		Product product = null;
		try {
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			String className = tableName.replace('_', '.');
			Class<?> c = JarLoader.getClass(className);
			product = (Product) c.newInstance();
			
			for (int i = 1; i <= columnCount; i++) {
				String columnName = metaData.getColumnName(i);
				Field f = c.getField(columnName);
				Class<?> fieldType = f.getType();
				String fieldTypeName = fieldType.getSimpleName();
				
				if (fieldTypeName.equals("Date")) {
					System.out.println(rs.getString(i));
					String dateString = rs.getString(i);
					if (dateString != null) {
						Date date = parseDate(dateString);
						f.set(product, date);
					}
					
				} else if (fieldTypeName.equals("Color")) {
					f.set(product, new Color(rs.getInt(i)));
				} else if (fieldTypeName.equalsIgnoreCase("double")){
					f.set(product, rs.getDouble(i));
				} else if (fieldTypeName.equalsIgnoreCase("int")) {
					f.set(product, rs.getInt(i));
				} else if (fieldTypeName.equals("String")) {
					f.set(product, rs.getString(i));
				} else if (fieldTypeName.equals("long")) {
					f.set(product, rs.getLong(i));
				}
			}
		} catch (Exception e) {
			System.err.println("Error re-creating product object");
			e.printStackTrace();
			return null;
		}
		
		return product;
	}

	/**
	 * Custom date parser since DateFormat isn't working for our date format.
	 * @param dateString
	 * @return A date representation of dateString.
	 */
	private Date parseDate(String dateString) {
		Date date = new Date();
		String[] parts = dateString.split(" ");
		String[] yyyymmdd = parts[0].split("/");
		String[] hhmmss = parts[1].split(":");
		
		date.setYear(new Integer(yyyymmdd[0]) - 1900);
		date.setMonth(new Integer(yyyymmdd[1]) - 1);
		date.setDate(new Integer(yyyymmdd[2]));
		
		date.setHours(new Integer(hhmmss[0]));
		date.setMinutes(new Integer(hhmmss[1]));
		date.setSeconds(new Integer(hhmmss[2]));
		
		return date;
	}
	
	/**
	 * @param date
	 * @return A string representation of a date in yyyy/MM/dd HH:mm:ss format.
	 */
	private String getDateString(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return df.format(date);
	}

	/**
	 * @return The names of all the tables that exist in the database.
	 */
	private List<String> getTableNames() {
		List<String> tableNames = new ArrayList<String>();
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT name" +
												  " FROM sqlite_master" +
												  " WHERE type=\'table\' AND name not like \'%sqlite%\';");
			while(rs.next()) {
				tableNames.add(rs.getString("name").replace('_', '.'));
			}
			rs.close();
			
		} catch (Exception e) {
			System.err.println("Error loading from database");
			e.printStackTrace();
		}
		
		return tableNames;
	}
	
	/**
	 * Provides a mapping from id to name of all products of a class that are stored in the db.
	 */
	@Override
	protected Map<Integer, String> getProductIdsAndNames(String className) {
		String tableName = extractTableName(className);
		Map<Integer, String> map = new HashMap<Integer, String>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs;
			if (tableExists(tableName)) {
				rs = stmt.executeQuery("SELECT id, name " +
									   "FROM " + tableName + ";");
			
				while (rs.next()) {
					map.put(rs.getInt("id"), rs.getString("name"));
				}
			}
		} catch (Exception e) {
			System.err.println("Error loading id-name map");
			e.printStackTrace();
		}
		
		return map;
	}
	
	/**
	 * Provides a mapping from id to product for all instances of a class that are stored in the db.
	 */
	@Override
	protected Map<Integer, Product> getIdToProductMap(String className) {
		Map<Integer, Product> map = new HashMap<Integer, Product>();
		String tableName = extractTableName(className);
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs;
			
			if (tableExists(tableName)) {
				rs = stmt.executeQuery("SELECT * FROM " + tableName + ";");
				
				while (rs.next()) {
					Product p = createProductFromResultSet(rs, tableName);
					int id = rs.getInt("id");
					
					map.put(id, p);
				}
			}
		} catch (Exception e) {}
		
		return map;
	}
	
	/**
	 * Loads a particular instance of a product from the database.
	 * @param id The id of the desired product
	 * @param className The name of the product that we are interested in's class.
	 */
	@Override
	protected Product loadProductFromId(int id, String className) {
		String tableName = extractTableName(className);
		Product newProduct = null;
		try {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * " +
												  "FROM " + tableName + 
												  " WHERE id=\'" + id + "\';");
			if (rs.next()) {
				newProduct = createProductFromResultSet(rs, tableName);
			}
		} catch (Exception e) {
			System.err.println("Error loading single product from database");
			e.printStackTrace();
		}
		
		return newProduct;
	}
	
	/**
	 * Extracts the database table name corresponding to a class.
	 * @param className
	 * @return
	 */
	private String extractTableName(String className) {
		String tableName = className.replace('.', '_');
		if (tableName.contains("class ")) {
			tableName = tableName.replace("class ", "");
		}
		return tableName;
	}

	/**
	 * Deletes product p from the database
	 */
	@Override
	protected void delete(Product p) {
		long id = p.id;
		
		try {
			Statement stmt = conn.createStatement();
			String tableName = extractTableName(p.getClass().getName());
			stmt.executeUpdate("DELETE FROM " + tableName +
							   " WHERE id = " + id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the data stored against product p in the database.
	 * DB values are overwritten by the values in p's public fields.
	 */
	@Override
	protected void update(Product p) {
		long id = p.id;
		Class<?> c = p.getClass();
		String sql = "";
		Field[] fields = c.getFields();
		
		try {
			String tableName = extractTableName(c.getName());
			/* Construct SQLite update statement: */
			if (fields.length > 1) {	//There are more fields than just an id
				sql = "UPDATE " + tableName + " SET ";
	
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					
					Class<?> fieldType = field.getType(); 
					String fieldTypeName = fieldType.getSimpleName();
					
					if (field.getName().equals("id")) {
						continue;
					}
					
					sql += field.getName();
					sql += "=";
					
					if (fieldTypeName.equalsIgnoreCase("int")) {
						sql += field.get(p);
					} else if (fieldTypeName.equals("String")) {
						String s = (String)field.get(p);
						if (s != null) {
							sql += "\'" + s.replaceAll("'", "''") + "\'";
						} else {
							sql += "\'\'";
						}
					} else if (fieldTypeName.equalsIgnoreCase("double")) {
						sql += field.get(p);
					} else if (fieldTypeName.equals("Date")) {
						Date date = (Date) field.get(p);
						if (date != null) {
							sql += "\'" + getDateString(date) + "\'";
						} else {
							sql += "\'\'";
						}
					} else if (fieldTypeName.equals("Color")) {
						Color color = (Color) field.get(p);
						if (color != null) {
							sql += (color.getRGB());
						} else {
							sql += "\'\'";
						}
					}
					else {	//Unsupported datatype
						sql = sql.substring(0, sql.lastIndexOf(","));
					}
					
					if (i < fields.length-1) {
						sql += ", ";
					}
				}
				sql += " WHERE id = " + id + ";";
			}
			System.out.println(sql);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the database that future transactions will be made to.
	 */
	@Override
	protected void setDatabase(String name) {
		this.databaseName = name;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+name);
		} catch (Exception e) {}
	}
}
