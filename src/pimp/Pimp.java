/**
 * This is the main class for the PIMP program.
 */

package pimp;

// Gui
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pimp.gui.MainDisplay;
import pimp.gui.SelectProductDialog;
import pimp.persistence.DataAccessor;
import pimp.persistence.ProductCache;
import pimp.persistence.Status;
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
	private String defaultDatabaseName = "test.db";
	
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
		//Probably here?
		cache.addProductsAddedListener(new productAddedListener());
		cache.addProductsRemovedListener(new productRemovedListener());
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

		/*Shoe shoe = new Shoe();
		shoe.name = "STYLISH SHOOOOE";
		shoe.quantity = 4;
		shoe.shoeSize = 12;
		shoe.sizingSystem = "EU";
		gui.updateProductForm(shoe);*/

	}

	public void getNewProduct() {
		List<Class<?>> classList = dcl.getClassList();
		List<Class<?>> concreteClassList = new ArrayList<Class<?>>();
		//Ensuring we don't give the option of creating abstract classes
		for(Class<?> c: classList){
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
				ArrayList<Product> l = new ArrayList<Product>();
				l.add(c.newInstance());
				cache.add(l);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getProductsByClass(String className){
		if (!cache.isLoaded(className)){
			Map<Integer, Product> m = DataAccessor.getIdToProductMap(className);
			ArrayList<Product> l = new ArrayList<Product>();
			for (Product p : m.values()) {
			    l.add(p);
			}
			cache.add(l);
			cache.addToClassesLoaded(className);
		}
	}
	
	class productAddedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Code to update the tree here.
			gui.setProducts((List<Product>) e.getSource());
		}
	}
	
	class productRemovedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Remove from tree;
			gui.removeProduct((List<Product>) e.getSource());
		}
	}	


	public void remove(List<Product> products){
		//We've received a list of products that need to be deleted.
		//Fire it over to the cache.
		cache.delete(products);
	}
	
	/**
	 * This method was written to do the controller side processing of the
	 * export button. It takes a file and attempts to save the database to
	 * it.
	 */
	public boolean exportDatabase(File dbFile) {
		try {
			DataAccessor.exportDb(dbFile);
		} catch (Exception e1) {
			System.err.println("Could not export database to " + dbFile.getName());
			e1.printStackTrace();
			return false;
		}	
		return true;
	}

}
