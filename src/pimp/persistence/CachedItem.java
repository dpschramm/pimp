package pimp.persistence;

import pimp.productdefs.Product;

public class CachedItem<Product, Status>{
	private Product product;
	private Status status;
	
	public CachedItem(Product p, Status s){
        this.product = p;
        this.status = s;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
