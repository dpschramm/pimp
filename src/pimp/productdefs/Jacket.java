package pimp.productdefs;

import pimp.form.FormField;

public class Jacket extends Clothing {

	@FormField(displayName="Size")
	public String size;
	
	@FormField(displayName="Brand")
	public String brand;
	
	@Override
	public String toString() {
		return "Jacket [size=" + size + ", brand=" + brand + ", isWaterproof="
				+ isWaterproof + ", colour=" + colour + ", name=" + name
				+ ", quantity=" + quantity + ", displayForm=" + displayForm
				+ "]";
	}

	@FormField(displayName="Is Waterproof")
	public boolean isWaterproof;
}
