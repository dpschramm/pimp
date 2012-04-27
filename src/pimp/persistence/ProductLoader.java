package pimp.persistence;

import java.util.List;

import pimp.productdefs.Product;

public abstract class ProductLoader {
	protected abstract List<Product> load();
}
