/**
 * This is the main class for the PIMP program.
 */

package pimp;

// Gui

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pimp.gui.MainDisplay;
import pimp.gui.SelectProductDialog;

// Other
import pimp.persistence.DataAccessor;
import pimp.productdefs.Drink;
import pimp.productdefs.Product;
import pimp.testdefs.Shoe;


/**
 * The Pimp class acts as the controller for our inventory management program.
 * 
 * @author Daniel Schramm, Ellie Rasmus
 */
public class Pimp {
	
	// Database stuff
	private String defaultDatabaseName = "products.db";
	
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
		/*Drink drink = new Drink();
		drink.capacity = "Large";
		drink.flavour = "Blue";
		drink.name = "Gatorade";
		drink.quantity = 40;
		
		// Update the form displayed by the GUI.
		gui.updateProductForm(drink);*/
		Shoe shoe = new Shoe();
		shoe.name = "STYLISH SHOOOOE";
		shoe.quantity = 4;
		shoe.shoeSize = 12;
		shoe.sizingSystem = "EU";
		gui.updateProductForm(shoe);
	}

	public Product getNewProduct() {
		List<Class<?>> classList = dcl.getClassList();
		List<Class<?>> concreteClassList = new ArrayList<Class<?>>();
		//Ensuring we don't give the option of creating abstract classes
		for(Class c: classList){
			if(!Modifier.isAbstract(c.getModifiers())){
				concreteClassList.add(c);				
			}
		}
		// Create and show product dialog.
		SelectProductDialog selectDialog = new SelectProductDialog(gui, 
				/*dcl.getClassList()*/concreteClassList);
		
		// Create product from selected class.
		Class<? extends Product> c = selectDialog.getSelectedClass();
		if (c != null) {
			try {
				Product p = c.newInstance();
				//DataAccessor.save(p);
				return p;
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
	
	/*
	 * It seems weird, that this is necessary. But we don't want to use a data accessor
	 * from the gui. That would be A Bad Thing to do.
	 * */
	public void saveChangesToProduct(Product p){
		DataAccessor.save(p);
	}
	
	public Map<Integer, String> getProductsByClass(String className){
	Map<Integer, String> m = DataAccessor.getProductIdsAndNames(className);
	return m;
	}
	
}
