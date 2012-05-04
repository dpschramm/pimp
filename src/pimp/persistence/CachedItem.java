package pimp.persistence;

import pimp.productdefs.Product;

/**
 * 
 * @author Joel Mason
 *
 */

public class CachedItem<Product, Enum>{
	private Product product;
	private int status;
	
	public CachedItem(Product p, int s){
        this.product = p;
        this.status = s;
    }

	public Product getProduct() {
		return product;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
