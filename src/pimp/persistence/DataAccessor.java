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
	
	public void connect(DatabaseConnection dbConnector) {
		this.dbc = dbConnector;
	}
	
	public boolean save(Product product) {
		return dbc.save(product);
	}
	
	public List<Product> loadProductList() {
		return dbc.loadProductList();
	}
	
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
	
	public Map<Integer, Product> getIdToProductMap(String className) {
		return dbc.getIdToProductMap(className);
	}
	
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
