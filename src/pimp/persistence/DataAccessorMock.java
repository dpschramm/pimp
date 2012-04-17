package pimp.persistence;

import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Car;
import pimp.productdefs.Drink;
import pimp.productdefs.Jacket;
import pimp.productdefs.Product;

public class DataAccessorMock {
	
	private static boolean initialized = false;
	
	public DataAccessorMock() {
		// Do nothing.
	}
	
	public static void initialize(String filename) {
		initialized = true;
	}
	
	public static List<Product> load() {
		if (!initialized) return null;
		
		List<Product> productList = new ArrayList();
		
		Car honda = new Car();
		honda.colour = "grey";
		honda.make = "Honda";
		honda.model = "Civic";
		honda.year = 1992;
		honda.name = "Ellie's Honda is boring";
		honda.quantity = 1;
		productList.add(honda);
		
		Car nissan = new Car();
		nissan.colour = "red";
		nissan.make = "Nissan";
		nissan.model = "Primera";
		nissan.year = 1998;
		nissan.name = "Nissan Primera";
		nissan.quantity = 5;
		productList.add(nissan);
			
		Jacket orangeJacket = new Jacket();
		orangeJacket.brand = "Generic Brand";
		orangeJacket.colour = "orange";
		orangeJacket.isWaterproof = true;
		orangeJacket.name = "Orange Jacket";
		orangeJacket.size = "L";
		orangeJacket.quantity = 7;
		productList.add(orangeJacket);
			
		Jacket purpleJacket = new Jacket();
		purpleJacket.brand = "Generic Brand";
		purpleJacket.colour = "purple";
		purpleJacket.isWaterproof = false;
		purpleJacket.name = "Purple Jacket";
		purpleJacket.size = "M";
		purpleJacket.quantity = 9;
		productList.add(purpleJacket);
		
		//has public attributes so that field builder will work
		Drink liftPlus = new Drink();
		liftPlus.name = "Lift Plus";
		liftPlus.capacity = "440ml";
		liftPlus.flavour = "Fizzy Lemony Tang";
		liftPlus.quantity = 4;
		productList.add(liftPlus);
		
		return productList;
	}
}
