package pimp.productdefs;

import java.awt.Color;

import pimp.form.FormField;
import pimp.model.Product;

public abstract class Clothing extends Product {
	@FormField(displayName="Colour")
	public Color colour;
}
