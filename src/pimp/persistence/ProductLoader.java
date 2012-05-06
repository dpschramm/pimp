package pimp.persistence;

import java.util.List;

import pimp.model.Product;

public abstract class ProductLoader {
	protected abstract List<Product> load();
}
