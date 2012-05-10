/**
 * Provides a single point of access to a database implementation
 * Makes use of the singleton pattern.
 * connect() must be called before any transactions are attempted.
 */

package pimp.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pimp.model.Product;

public class DataAccessor {
	
	private DatabaseConnection dbc = null;
	
	// Singleton stuff
	
	private static DataAccessor instance = null;

	public static DataAccessor getInstance() {
		if (instance == null) {
			instance = new DataAccessor();
		}
		return instance;
	}
	
	private DataAccessor() {} // Disable the public constuctor.
	
	/**
	 * Sets the connection object for this instance of DataAccessor.
	 * Must be called with a valid DatabaseConnection before any transactions can be made.
	 * @param dbConnector
	 */
	public void connect(DatabaseConnection dbConnector) {
		this.dbc = dbConnector;
	}
	
	public boolean save(Product product) {
		return dbc.save(product);
	}
	
	/**
	 * Retrieves all products from all tables in the database
	 * @return
	 */
	public List<Product> loadProductList() {
		return dbc.loadProductList();
	}
	
	/**
	 * Copies a database file to a new location.
	 * Original file is copied byte-for-byte to the new location
	 * and future transactions will be made to the new location.
	 * @param newDatabaseFile: the new database file
	 * @throws Exception: if the new file cannot be opened.
	 */
	public void exportDb(File newDatabaseFile) throws Exception {
		InputStream in = new FileInputStream(dbc.getDatabaseName());
		OutputStream out = new FileOutputStream(newDatabaseFile);
		byte[] buffer = new byte[1024];
		int len;
		
		while((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		
		in.close();
		out.close();
		
		dbc.setDatabase(newDatabaseFile.getName());
	}
	
	/**
	 * @param className: the name of the class that we want all the instances of
	 * @return A mapping from product id to product.
	 */
	public Map<Integer, Product> getIdToProductMap(String className) {
		return dbc.getIdToProductMap(className);
	}
	
	/**
	 * @param className: the name of the class that we want all the instances of.
	 * @return A mapping from product id to product name
	 */
	public Map<Integer, String> getProductIdsAndNames(String className) {
		return dbc.getProductIdsAndNames(className);
	}
	
	public Product loadProductFromId(int id, String productClass) {
		return dbc.loadProductFromId(id, productClass);
	}
	
	public void saveAll(Collection<Product> products) {
		for (Product p : products) {
			dbc.save(p);
		}
	}
	
	public void delete(Product p) {
		dbc.delete(p);
	}
	
	public void update(Product p) {
		dbc.update(p);
	}

}
