/**
 * This is the main class for the PIMP program.
 */

package pimp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import pimp.classloader.JarLoader;
import pimp.gui.DatabaseSelector;
import pimp.gui.ProductGui;
import pimp.gui.SelectClassDialog;
import pimp.model.Product;
import pimp.model.ProductModel;
import pimp.model.Status;
import pimp.persistence.DataAccessor;
import pimp.persistence.SqliteConnection;


/**
 * The ProductController class acts as the controller for our inventory management program.
 */
public class ProductController {
	
	// Default Database identifier
	private String defaultDatabaseName = "test.db";
	
	// Model
	private ProductModel cache;
	
	// Directory to look for product definitions in
	private String productDir = "products"; 
	
	// Reference to View instance
	private ProductGui gui;	
	
	// Reference to DataAccessor instance
	private DataAccessor da;
	
	/** 
	 * ProductController is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public ProductController() {
		//Initialise cache
		cache = new ProductModel();
		// Initialize Gui
		gui = new ProductGui(this, cache);
		// Set up database.
		da = DataAccessor.getInstance();
		setupDatabase(defaultDatabaseName);
		// Display the gui.
		gui.display();
	}
	
	/**
	 * Load existing products from database.
	 * @param databaseName, name of database
	 */
	private void setupDatabase(String databaseName) {
		System.out.println("Opening database: " + databaseName);
		da.connect(new SqliteConnection(databaseName));
		loadClasses();
	}

	/**
	 * Launches SelectClassDialog and creates a new instance of 
	 * the selected product type.
	 */
	public void createNewProduct() {
		// Create and show product dialog.
		SelectClassDialog selectDialog = new SelectClassDialog(gui, 
				loadClasses());
		// Create product from selected class.
		Class<?> c = selectDialog.getSelectedClass();
		if (c != null) {
			try {
				Product p = (Product) c.newInstance();
				// Set initial name of product to "New <ProductType>"
				p.name = "New " + c.getSimpleName();
				// Notify model of changes
				cache.add(p);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Creates a new product from the current state of the form.
	 * This is used for copying products.
	 * @param fromForm, product object retrieved from current form state.
	 */
	public void createNewProduct(Object fromForm){
		Product newProduct = (Product) fromForm;
		cache.add(newProduct);
	}

	/**
	 * Loads all products of a certain class into the cache.
	 * @param className, the type of products to load into the cache
	 */
	public void getProductsByClass(String className) {
		System.out.println("Getting products for class: " + className);
		// If we haven't already loaded these products into the cache.
		if (!cache.isLoaded(className)){
			// Get products  and their ids from data accessor 
			Map<Integer, Product> m = da.getIdToProductMap(className);
			ArrayList<Product> l = new ArrayList<Product>();
			// Retrieve product values from map
			for (Product p : m.values()) {
			    l.add(p);
			}
			// Load list of products into the cache.
			cache.load(l);
		}
	}	

	/**
	 * Delete a list of products from cache.
	 * @param products the list of products to be deleted.
	 */
	public void removeFromCache(List<Product> products){
		//Fire it over to the cache.
		cache.delete(products);
	}
	

	/**
	 * Changes item in cache to reflect values in updated product instance
	 * @param original, product prior to change
	 * @param updated, changed product
	 */
	public void updateCacheItem(Product original, Product updated){
		cache.update(original, updated);
	}
	
	/**
	 * Loads available classes that extend the Product superclass
	 * @return Set<Class<?>> set, collection of classes that extend Product
	 */
	public Set<Class<?>> loadClasses() {
		// Load directory and retrieve classes
		JarLoader.load(productDir);
		Set<Class<?>> set = JarLoader.getClasses(Product.class);
		// Notifies view so it can update it's tree structure
		gui.setClasses(set); 
		return set;
	}
	
	/**
	 * Commits to persistence any changes made to the cache. This is
	 * done by checking the status of each product in the cache and 
	 * changing the database accordingly. 
	 */
	public void commitCache(){
		// Get the current cache state
		Map<Product, Status> list = cache.getCache();
		for (Product p : list.keySet()) {
		    Status s = list.get(p);
		    // If product is flagged as deleted
		    if (s == Status.DELETED)
		    {
		    	da.delete(p);
		    }
		    // If product has been updated
		    else if (s == Status.UPDATED)
		    {
		    	da.update(p);
		    }
		    // If product has been newly created
		    else if (s == Status.NEW)
		    {
		    	da.save(p);
		    }
		}
		// Reset cache statuses
		cache.flush();
		System.out.println("Saved ");
	}

	
	/**
	 * Brings up a dialog to select the database file
	 * and load products from that database.
	 */
	public void open() {
		// Launch DatabaseSelector
		DatabaseSelector dbs = new DatabaseSelector();
		// Get selected file
		File file = dbs.getFile("Open");
		// If selected file is valid
		if (file != null) {
			/* Remove and refresh references to current 
			   class list in view and cache */
			gui.empty();
			cache.clearClassLoadedList();
			setupDatabase(file.getName());
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
				da.exportDb(file);
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
