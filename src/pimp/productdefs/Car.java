package pimp.productdefs;

import pimp.annotations.FormField;
import pimp.model.Product;

/** An example of a specific product that could be instantiated.
 * 
 * @author Daniel Schramm
 *
 */
public class Car extends Product {
	
	// Fields
	@FormField
	public String colour;
	@FormField
	public String make;
	@FormField
	public String model;
	@FormField
	public int year;
}
