package pimp.productdefs;

/** An example of a specific product that could be instantiated.
 * 
 * @author Daniel Schramm
 *
 */
public class Car extends Product {
	
	// Fields
	private String colour;
	private String make;
	private String model;
	private int year;
	
	// Getters and setters
	public String getColour() {
		return colour;
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
	public String getMake() {
		return make;
	}
	
	public void setMake(String make) {
		this.make = make;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(String model) {
		this.model = model;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
}
