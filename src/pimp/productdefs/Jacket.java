package pimp.productdefs;

public class Jacket extends Clothing {

	private String size;
	private String brand;
	private boolean isWaterproof;
	
	public void setSize(String size){
		this.size = size;
	}
	public String getSize(){
		return this.size;
	}
	public void setBrand(String brand){
		this.brand = brand;
	}
	public String getBrand(){
		return this.brand;
	}
	public void setIsWaterproof(boolean isWaterproof){
		this.isWaterproof = isWaterproof;
	}
	public boolean getIsWaterproof(){
		return this.isWaterproof;
	}
}
