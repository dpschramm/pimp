package pimp.productdefs;

import pimp.form.FormField;

public class Jeans extends Clothing {
	
	@FormField(displayName="Size")
	public String size;
	
	@FormField(displayName="Leg Length")
	public String legLength; //eg short, medium, long.
	
	@FormField(displayName="Brand")
	public String brand;
}
