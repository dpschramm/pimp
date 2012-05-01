package pimp.persistence;

import java.util.Collection;
import java.util.List;

import pimp.productdefs.*;

public class DataAccessor {
	private ProductLoader loader;
	private ProductSaver saver;
	
	public static DBDataAccessor instance = null;
	
	private DataAccessor(ProductLoader loader, ProductSaver saver) {
		this.loader = loader;
		this.saver = saver;
	}

	public static void initialise(String dbName) {
		if (instance == null) {
			instance = new DBDataAccessor(dbName);
		}
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
	
	public static Product loadProductFromId(int id, Class<?> productClass) {
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
