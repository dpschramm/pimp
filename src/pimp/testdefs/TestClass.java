package pimp.testdefs;


import java.awt.Color;
import java.util.Date;

import pimp.form.FormField;


public class TestClass {
	
	@FormField
	public int age;
	
	@FormField
	public double shoeSize;
	
	@FormField
	public String name;
	
	public Date dob;
	
	@FormField
	public Color favColor;
	
	public TestClass(int age, double shoeSize, String name, Date dob, Color favColor) {
		this.age = age;
		this.shoeSize = shoeSize;
		this.name = name;
		this.dob = dob;
		this.favColor = favColor;
	}
}
