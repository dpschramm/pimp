package pimp.productdefs;

import pimp.CompanionForm;

/** The abstract product class provides the basis for all custom products to
 * be tracked in the inventory program.
 * 
 * There are currently no abstract methods so it would be possible to 
 * instantiate a concrete product object, but we decided to make this class
 * abstract as it doesn't make sense to have "Products" without having a more
 * specific description of the product.
 * 
 * @author Daniel Schramm
 *
 */
public abstract class Product {
	
	// Fields
	public String name;
	public int quantity;
	public CompanionForm displayForm;
	
	/** Default constructor - doesn't take parameters as these will be set 
	 * later by the UI form. */
	public Product() {
		name = "";
		quantity = 0;
		displayForm = null;
	}
	
	@Override
	public String toString() {
	    return this.name;
	}

}
