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
	
	private static DatabaseConnection instance = null;

	public static void initialise(DatabaseConnection dbc) {
		instance = dbc;
	}
	
	public static boolean save(Product product) {
		if (instance == null) {
			System.out.println("DataAccessor must be initialised first. Call DataAccessor.initialise().");
			return false;
		} else {
			return instance.save(product);
		}
	}
	
	public static List<Product> loadProductList() {
		if (instance == null) {
			System.out.println("DataAccessor must be initialised first. Call DataAccessor.initialise().");
			return null;
		} else {
			return instance.loadProductList();
		}
	}
	
	public static void exportDb(File newDatabaseFile) throws Exception {
		InputStream in = new FileInputStream(instance.getDatabaseName());
		OutputStream out = new FileOutputStream(newDatabaseFile);
		byte[] buffer = new byte[1024];
		int len;
		
		while((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}
		
		in.close();
		out.close();
		
		instance.setDatabase(newDatabaseFile.getName());
	}
	
	public static Map<Integer, Product> getIdToProductMap(String className) {
		if (instance == null) {
			return null;
		}
		
		return instance.getIdToProductMap(className);
	}
	
	public static Map<Integer, String> getProductIdsAndNames(String className) {
		if (instance == null) {
			return null;
		}
		
		return instance.getProductIdsAndNames(className);
	}
	
	public static Product loadProductFromId(int id, String productClass) {
		if (instance == null) {
			System.out.println("DataAccessor must be initialised first. Call DataAccessor.initialise()");
			return null;
		}
		
		return instance.loadProductFromId(id, productClass);
	}
	
	public static boolean isInitialised() {
		if (instance == null) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void saveAll(Collection<Product> products) {
		if (instance != null) {
			for (Product p : products) {
				save(p);
			}
		}
	}
	
	public static void delete(Product p) {
		if (instance != null) {
			instance.delete(p);
		}
	}
	
	public static void update(Product p) {
		if (instance != null) {
			instance.update(p);
		}
	}
}
