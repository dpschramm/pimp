package pimp.persistence;

import pimp.productdefs.Product;

/**
 * 
 * @author Joel Mason
 *
 */
public class CachedItem {
	private Product product;
	private Status status;
	
	public CachedItem(Product p, Status s) {
        this.product = p;
        this.status = s;
    }

	public Product getProduct() {
		return product;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
