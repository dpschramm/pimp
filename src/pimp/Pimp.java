/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import pimp.gui.MainDisplay;
import pimp.productdefs.Car;
import pimp.productdefs.Product;
import pimp.testdefs.TestClass;

/**
 * The Pimp class acts as the controller for our inventory management program.
 */
public class Pimp {
	
	private static MainDisplay gui; // Why is this static, can we change? -DS
	private List<Product> products;
	
	// Program entry point.
	public static void main(String[] args) {
		new Pimp();
	}
	
	/** 
	 * Constructor creates and shows GUI.
	 */
	public Pimp() {
		
		// Create empty product list.
		products = new ArrayList<Product>();
		
		/** TODO add code to automatically load previous product list. */
		loadProducts();
		
		/**
		 * The code below needs to be commented and refactored -DS
		 */
		
		// Create GUI
		gui = new MainDisplay();
		gui.setVisible(true);
		
		FormBuilder fb = new FormBuilder(TestClass.class);
		@SuppressWarnings("deprecation")
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", new Date(2012, 4, 3), Color.BLUE);
		JPanel newForm;
		try {
			newForm = (JPanel) fb.fillForm(tc1);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			newForm = new JPanel();
			newForm.setBackground(Color.RED);
			newForm.setSize(new Dimension(200, 200));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			newForm = new JPanel();
			newForm.setBackground(Color.RED);
			newForm.setSize(new Dimension(200, 200));
			//e.printStackTrace();
		}
		setDynamicProductForm(newForm);
		
	}

	public static void setDynamicProductForm(JPanel form){
		form.setBounds(0, 0, gui.dynamicPanel.getWidth(), gui.dynamicPanel.getHeight());//display.dynamicPanel.getBounds());
		gui.dynamicPanel.removeAll();
		gui.dynamicPanel.add(form);
		form.setVisible(true);
		gui.validate();
		gui.setVisible(true);
		gui.repaint();
	}
	
	/**
	 * This method should be called when new products need to be created (i.e.
	 * when the "New" button is clicked).
	 */
	public void createProduct() {
		
		Product newProduct;
		
		// create new product dialog.
		
		// get the class of the new product to be created.
		
		// create the class
		newProduct = new Car(); // REPLACE this with whatever class was
								// returned by the previous dialog.
		
		products.add(newProduct);
		
		// update gui to reflect change in the product list.
	}
	
	/**
	 * Delete the specified product.
	 * 
	 * @param p
	 */
	public void deleteProduct(Product p) {
		
	}
	
	/** 
	 * Save products to file.
	 */
	public void saveProducts() {
		
	}
	
	/**
	 * Load products from file.
	 */
	public void loadProducts() {
		
	}
}
