package pimp;

import pimp.gui.MainDisplay;

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
