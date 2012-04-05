package pimp;

import pimp.gui.MainDisplay;
import pimp.persistence.*;
import pimp.productdefs.Car;
import pimp.productdefs.Jacket;

public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//do we have a model to instantiate?	
		MainDisplay md = new MainDisplay(); //view
		Pimp p = new Pimp(md); //controller
	}

}
