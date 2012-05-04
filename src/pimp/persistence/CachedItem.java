package pimp.persistence;

import pimp.productdefs.Product;

public class CachedItem<Product, Integer>{
	private Product product;
	private int status;
	
	public CachedItem(Product p, int s){
        this.product = p;
        this.status = s;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
