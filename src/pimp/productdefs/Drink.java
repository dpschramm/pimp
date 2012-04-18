package pimp.productdefs;

import pimp.form.FormField;

public class Drink extends Product {
	
	@FormField(displayName="Capacity")
	public String capacity;
	
	@FormField(displayName="Quantity")
	public int quantity;
	
	@FormField(displayName="Flavour")
	public String flavour;
	
}
