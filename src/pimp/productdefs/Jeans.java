package pimp.productdefs;

import pimp.form.FormField;

public class Jeans extends Clothing {
	@FormField
	public String size;
	@FormField
	public String legLength; //eg short, medium, long.
	@FormField
	public String brand;
}
