package pimp.productdefs;

import pimp.annotations.FormField;

public class Jacket extends Clothing {

	@FormField(displayName="Size")
	public String size;
	
	@FormField(displayName="Brand")
	public String brand;
	
	@Override
	public String toString() {
		return this.name;
	}

	@FormField(displayName="Is Waterproof")
	public boolean isWaterproof;
}
