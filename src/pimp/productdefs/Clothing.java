package pimp.productdefs;

public abstract class Clothing extends Product {
	private String colour;
	
	public void setColour(String colour){
		this.colour = colour;
	}
	
	public String getColour(){
		return this.colour;
	}
}
