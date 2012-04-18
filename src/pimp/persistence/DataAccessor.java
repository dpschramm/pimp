package pimp.persistence;

import java.util.List;

import pimp.productdefs.*;

public class DataAccessor {
	private ProductLoader loader;
	private ProductSaver saver;
	
	public static DataAccessor instance = null;
	
	private DataAccessor(ProductLoader loader, ProductSaver saver) {
		this.loader = loader;
		this.saver = saver;
	}

	public static void initialise(ProductLoader loader, ProductSaver saver) {
		if (instance == null) {
			instance = new DataAccessor(loader, saver);
		}
	}
	
	public static boolean save(Product product) {
		if (instance == null) {
			System.out.println("DataAccessor must be initialised first. Call DataAccessor.initialise().");
			return false;
		} else {
			return instance.saver.save(product);
		}
	}
	
	public static List<Product> load() {
		if (instance == null) {
			System.out.println("DataAccessor must be initialised first. Call DataAccessor.initialise().");
			return null;
		} else {
			return instance.loader.load();
		}
	}
}
