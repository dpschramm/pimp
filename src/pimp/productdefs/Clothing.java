package pimp.productdefs;

import java.awt.Color;

import pimp.form.FormField;

public abstract class Clothing extends Product {
	@FormField(displayName="Colour")
	public Color colour;
}
