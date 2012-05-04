package pimp.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pimp.productdefs.*;

public class DataAccessor {
	
	private static DBDataAccessor instance = null;

	public static void initialise(String dbName) {
		instance = new DBDataAccessor(dbName);
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
		
		initialise(newDatabaseFile.getName());
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
		for (Product p : products) {
			save(p);
		}
	}
}
