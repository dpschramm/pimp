package pimp.persistence;

import java.util.Collection;

import pimp.productdefs.Product;

public abstract class ProductSaver {
	
	protected abstract boolean save(Product prod);
	
	protected void saveAll(Collection<Product> products) {
		for (Product product : products) {
			save(product);
		}
	}
}
