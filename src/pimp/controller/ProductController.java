/**
 * This is the main class for the PIMP program.
 */

package pimp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import pimp.classloader.DynamicJarLoader;
import pimp.form.ProductForm;
import pimp.gui.DatabaseSelector;
import pimp.gui.ProductGui;
import pimp.gui.SelectProductDialog;
import pimp.model.Product;
import pimp.model.ProductModel;
import pimp.model.Status;
import pimp.persistence.DataAccessor;
import pimp.productdefs.Drink;


/**
 * The ProductController class acts as the controller for our inventory management program.
 * 
 * @author Daniel Schramm, Ellie Rasmus
 */
public class ProductController {
	
	// Database stuff
	private String defaultDatabaseName = "test.db";
	
	// Model
	private ProductModel cache;
	
	// Product classes.
	private String productDir = "products"; /* Not sure what format this should take
												may need to be a cmd argument. */ 
	
	private ProductGui gui;	// View.
	
	/** 
	 * ProductController is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public ProductController() {
		
		//Initialise cache
		cache = new ProductModel();
		
		// Initialize Gui
		gui = new ProductGui(this, cache);
		
		// Load existing products.
		initialiseDB(defaultDatabaseName);
		
		// Make form.
		createForm();

		gui.display();
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
	
	public void createNewProduct() {
		
		// Create and show product dialog.
		SelectProductDialog selectDialog = new SelectProductDialog(gui, 
				loadClasses());
		
		// Create product from selected class.
		Class<? extends Product> c = selectDialog.getSelectedClass();
		if (c != null) {
			try {
				Product p = c.newInstance();
				p.name = "New " + c.getSimpleName();
				cache.add(p);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void createNewProduct(Object fromForm){
		Product newProduct = (Product) fromForm;
		cache.add(newProduct);
	}
	
	public void createProductCopy(Product productToCopy){
		// Do something?
	}
	
	public void getProductsByClass(String className) {
		if (!cache.isLoaded(className)){
			Map<Integer, Product> m = DataAccessor.getIdToProductMap(className);
			ArrayList<Product> l = new ArrayList<Product>();
			
			for (Product p : m.values()) {
			    l.add(p);
			}
			
			cache.load(l);
		}
	}	

	/**
	 * Delete a list of products.
	 * 
	 * @param products the list of products to be deleted.
	 */
	public void removeFromCache(List<Product> products){
		//Fire it over to the cache.
		cache.delete(products);
	}
	

	/**
	 * Can someone please figure out how to get pairs working
	 * @param p
	 */
	public void updateCacheItem(ArrayList<Product> p){
		Product product = p.get(0);
		Product changes = p.get(1);
		cache.update(product, changes);
	}

	
	public void initialiseDB(String databaseName) {
		DataAccessor.initialise(databaseName);
		// Load existing products.
		loadClasses();
	}
	
	private List<Class<?>> loadClasses() {
		List<Class<?>> cpl = DynamicJarLoader.load(productDir, Product.class);
		gui.setClasses(cpl); // must be called before setProducts.
		return cpl;
	}
	
	public void commitCache(){
		Map<Product, Status> list = cache.getCache();
		for (Map.Entry<Product, Status> entry : list.entrySet()) {
		    Product p = entry.getKey();
		    Status s = entry.getValue();

			System.out.println(s.toString() + ": " + p.toString());
		    if (s == Status.DELETED)
		    {
		    	DataAccessor.delete(p);
		    }
		    else if (s == Status.UPDATED)
		    {
		    	DataAccessor.update(p);
		    }
		    else if (s == Status.NEW)
		    {
		    	DataAccessor.save(p);
		    }
//		    else if (s == Status.FRESH)
//		    {
//		    	Do nothing
//		    }
		}
		cache.flush();
	}

	
	/**
	 * Brings up a dialog to select the database file
	 * and load products from that database.
	 */
	public void open() {
		DatabaseSelector dbs = new DatabaseSelector();
		File file = dbs.getFile("Open");
		if (file != null) {
			gui.empty();
			initialiseDB(file.getName());
			JOptionPane.showMessageDialog(gui, 
					"Database " + file.getName() + " opened");
		}
	}
	
	/**
	 * Brings up a dialog to choose/create new database location.
	 * The current database will be copied to the new location, 
	 * and all subsequent database transactions will be performed at
	 * the new location.
	 */
	public void saveAs() {
		// Get file to process from user.
		DatabaseSelector dbs = new DatabaseSelector();
		File file = dbs.getFile("Export");
		
		if (file != null) {	// If the user selected a file...
			try {
				DataAccessor.exportDb(file);
				JOptionPane.showMessageDialog(gui, 
						"Database successfully exported to " + 
						file.getName() + ".");
			} catch (Exception e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(gui, 
					"Could not export database to this location");
			}
		}
	}



}
