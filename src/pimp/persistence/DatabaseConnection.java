package pimp.persistence;

import java.util.List;
import java.util.Map;

import pimp.model.Product;

public abstract class DatabaseConnection {
	protected abstract boolean save(Product p);
	protected abstract List<Product> loadProductList();
	protected abstract Map<Integer, Product> getIdToProductMap(String className);
	protected abstract Map<Integer, String> getProductIdsAndNames(String className);
	protected abstract Product loadProductFromId(int id, String productClass);
	protected abstract void delete(Product p);
	protected abstract void update(Product p);
	protected abstract String getDatabaseName();
	protected abstract void setDatabase(String name);
}
