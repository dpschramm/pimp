package pimp.productdefs;

public class Jeans extends Clothing {

	private String size;
	private String legLength; //eg short, medium, long.
	private String brand;
	
	public void setSize(String size){
		this.size = size;
	}
	
	public String getSize(){
		return this.size;
	}
	
	public void setLegLength(String length){
		this.legLength = length;
	}
	
	public String getLegLength(){
		return this.legLength;
	}
	
	public void setBrand(String brand){
		this.brand = brand;
	}
	
	public String getBrand(){
		return this.brand;
	}
}
