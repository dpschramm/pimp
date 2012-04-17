package pimp.productdefs;

import pimp.form.FormField;

public class Jacket extends Clothing {
	@FormField
	public String size;
	@FormField
	public String brand;
	@FormField
	public String isWaterproof;
}
