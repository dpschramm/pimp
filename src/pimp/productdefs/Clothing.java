package pimp.productdefs;

import java.awt.Color;

import pimp.annotations.FormField;
import pimp.model.Product;

public abstract class Clothing extends Product {
	@FormField(displayName="Colour")
	public Color colour;
}
