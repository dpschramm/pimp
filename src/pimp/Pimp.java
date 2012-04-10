/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import pimp.gui.MainDisplay;
import pimp.gui.SelectProductDialog;
import pimp.productdefs.Car;
import pimp.productdefs.Jacket;
import pimp.productdefs.Product;
import pimp.testdefs.TestClass;
import pimp.persistence.*;

/**
 * The Pimp class acts as the controller for our inventory management program.
 */
public class Pimp {
	
	public MainDisplay gui; // Why is this static, can we change? -DS
	private List<Product> products;
	
	// Maintain class list.
	private ProductLoader loader;
	private XmlProductLoader xmlLoader;//It would be nice if this was declared as the abstract ProductLoader, 
									   //but that will have to wait until we don't have two ProductLoaders
	
	/** 
	 * Pimp is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public Pimp(MainDisplay gui) {
		String dir = "test.xml";
		// Create empty product list.
		//products = new ArrayList<Product>(); //should be initialised in  loadProducts
		loader = new ProductLoader("directory"); //perhaps directory will have to be a cmd argument
		xmlLoader = new XmlProductLoader();
		this.gui = gui;
		gui.setVisible(true);
		gui.addNewProductListener(new newProductListener());
		
		/** TODO add code to automatically load previous product list. */
		loadProducts();
		
		/**
		 * The code below needs to be commented and refactored -DS
		 */
		FormBuilder fb = new FormBuilder(TestClass.class);
		fb.addFormElement(new StringFormElement());
		fb.addFormElement(new DoubleFormElement());
		fb.addFormElement(new IntFormElement());
		fb.addFormElement(new DateFormElement());
		fb.addFormElement(new ColorFormElement());
		fb.createForm();
		
		@SuppressWarnings("deprecation")
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", new Date(2012, 4, 3), Color.BLUE);
		JPanel newForm;
		try {
			newForm = (JPanel) fb.fillForm(tc1);
			setDynamicProductForm(newForm);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {}
		
		
	}

	public void loadProducts(){	
		//String dir = "src/test.xml";
		//products = xmlLoader.loadAllProducts(dir);
		
		//Code to manually create a set of products, because
		//the xml loader is being a bit weird atm
		
		products = new ArrayList<Product>();
		//create products
		Car honda = new Car();
		honda.setColour("grey");
		honda.setMake("Honda");
		honda.setModel("Civic");
		honda.setYear(1992);
		honda.setName("Ellie's Honda is boring");
		honda.setQuantity(1);
		//save
		products.add(honda);
		
		Car nissan = new Car();
		nissan.setColour("red");
		nissan.setMake("Nissan");
		nissan.setModel("Primera");
		nissan.setYear(1998);
		nissan.setName("Nissan Primera");
		nissan.setQuantity(5);
		products.add(nissan);
			
		Jacket orangeJacket = new Jacket();
		orangeJacket.setBrand("Generic Brand");
		orangeJacket.setColour("orange");
		orangeJacket.setIsWaterproof(true);
		orangeJacket.setName("Orange Jacket");
		orangeJacket.setSize("L");
		orangeJacket.setQuantity(7);
		products.add(orangeJacket);
				
		Jacket purpleJacket = new Jacket();
		purpleJacket.setBrand("Generic Brand");
		purpleJacket.setColour("purple");
		purpleJacket.setIsWaterproof(false);
		purpleJacket.setName("Purple Jacket");
		purpleJacket.setSize("M");
		purpleJacket.setQuantity(9);
		products.add(purpleJacket);
		
		//do stuff to init list in gui
		gui.createProductTable(products);
		//gui.validate();
	}
	
	public void setDynamicProductForm(JPanel form){
		//Where should this kind of logic be? Oh this is terribly confusing.
		form.setBounds(0, 0, gui.dynamicPanel.getWidth(), gui.dynamicPanel.getHeight());
		gui.dynamicPanel.removeAll();
		gui.dynamicPanel.add(form);
		form.setVisible(true);
		gui.validate();
		gui.setVisible(true);
		gui.repaint();
	}
	
	/**
	 * This ActionListener is applied to the New button on the main gui. 
	 * When clicked it needs to launch a NewProductDialog, retrieve the input
	 * from that and create a product of the returned type
	 */
	class newProductListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {

			//The action has been triggered by the main gui
			// create new product dialog.
			SelectProductDialog selectDialog = new SelectProductDialog(gui, loader.getClassList());
				
			//The event has been triggered by the new product dialog. 
			//This means we can go ahead and create the product now.
			Class<? extends Product> c = selectDialog.getSelectedClass();
			
			try {
				/*Currently crashing here, but I think this is because all I've
				  had to test with so far is the abstract Product class.
				*/
				// Debug messages
				if (c != null) {
					Product p = c.newInstance();
					System.out.println("You selected to create a " + p.getClass().getName());
					products.add(p);
				}
				else System.out.println("No selection.");
				
				//TODO Other things that will need to happen here
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Delete the specified product.
	 * 
	 * @param p
	 */
	class deleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/** 
	 * Save products to file.
	 */
	class saveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Load products from file.
	 */
	class loadButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
