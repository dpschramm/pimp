package pimp.model;

import pimp.form.ProductForm;
import pimp.form.FormField;

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
	@FormField(displayName="Name")
	public String name;
	
	private int id;
	
	@FormField(displayName="Quantity")
	public int quantity;
	private Class<?> companionFormClass;	//Private so this doesn't get saved to persistence layer.
	
	/** Default constructor - doesn't take parameters as these will be set 
	 * later by the UI form. */
	public Product() {
		this("", 0, null, 0);
	}
	
	public Product(String name, int quantity, Class<?> companionFormClass, int id) {
		this.name = name;
		this.quantity = quantity;
		this.companionFormClass = companionFormClass;
		this.id = id;
	}
	
	@Override
	public String toString() {
	    return this.name;
	}
}
