package pimp.testdefs;

import java.awt.Color;
import java.util.Date;

public class AnotherTestClass {

	private int age;
	private double shoeSize;

	public AnotherTestClass(int age, double shoeSize) {
		super();
		this.age = age;
		this.shoeSize = shoeSize;
	}

	public AnotherTestClass() {
		super();
	}

	@Form(field = "age", isSet = false)
	public int getAge() {
		return age;
	}

	@Form(field = "age", isSet = true)
	public void setAge(int age) {
		this.age = age;
	}

	@Form(field = "shoeSize", isSet = false)
	public double getShoeSize() {
		return shoeSize;
	}

	@Form(field = "shoeSize", isSet = true)
	public void setShoeSize(double shoeSize) {
		this.shoeSize = shoeSize;
	}

}
