/**
 * This is the main class for the PIMP program.
 */

package pimp;

// Gui
import java.util.Map;

import pimp.gui.MainDisplay;
import pimp.gui.SelectProductDialog;

// Other
import pimp.persistence.DataAccessor;
import pimp.persistence.ProductCache;
import pimp.productdefs.Drink;
import pimp.productdefs.Product;


/**
 * The Pimp class acts as the controller for our inventory management program.
 * 
 * @author Daniel Schramm, Ellie Rasmus
 */
public class Pimp {
	
	// Database stuff
	private String defaultDatabaseName = "products.db";
	
	private ProductCache cache;
	
	// Product classes.
	private DirectoryClassLoader dcl;
	private String productPackage = "pimp.productdefs";
	private String productDir = "products"; /* Not sure what format this should take
												may need to be a cmd argument. */ 
	
	private MainDisplay gui;	// View.	
	
	/**
	 * Main method just creates a new Pimp object.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Pimp();
	}
	
	/** 
	 * Pimp is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public Pimp() {
		// Load class definitions.
		dcl = new DirectoryClassLoader(productDir, productPackage);
		
		// Initialize Gui
		gui = new MainDisplay(this);
		
		//Initialise cache
		cache = new ProductCache();
		
		// Load existing products.

		initialiseDB(defaultDatabaseName);
		
		// Make form.
		createForm();

		gui.display();
	}
	
	public void initialiseDB(String databaseName) {
		DataAccessor.initialise(databaseName);
		
		// Load existing products.
		gui.setClasses(dcl.getClassList()); // must be called before setProducts.
		//gui.setProducts(DataAccessor.loadProductList());
	}
	
	private void createForm() {
		// Fill the form.
		Drink drink = new Drink();
		drink.capacity = "Large";
		drink.flavour = "Blue";
		drink.name = "Gatorade";
		drink.quantity = 40;
		
		// Update the form displayed by the GUI.
		gui.updateProductForm(drink);
	}

	public Product getProduct() {
		// Create and show product dialog.
		SelectProductDialog selectDialog = new SelectProductDialog(gui, 
				dcl.getClassList());
		
		// Create product from selected class.
		Class<? extends Product> c = selectDialog.getSelectedClass();
		if (c != null) {
			try {
				return c.newInstance();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// No product selected.
		return null;
	}
	
	public Map<Integer, Product> getProductsByClass(String className){
	Map<Integer, Product> m = DataAccessor.getIdToProductMap(className);
	return m;
	}
}
