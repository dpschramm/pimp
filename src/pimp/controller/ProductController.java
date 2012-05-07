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
import pimp.persistence.DatabaseConnection;
import pimp.persistence.SqliteConnection;
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
	
	private void setupDatabase(String databaseName) {
		// Load existing products.
		System.out.println("Opening database: " + databaseName);
		da.connect(new SqliteConnection(databaseName));
		loadClasses();
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
			Map<Integer, Product> m = da.getIdToProductMap(className);
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
	public void updateCacheItem(Product original, Product updated){
		cache.update(original, updated);
	}
	
	private List<Class<?>> loadClasses() {
		List<Class<?>> cpl = DynamicJarLoader.load(productDir, Product.class);
		gui.setClasses(cpl); // must be called before setProducts.
		return cpl;
	}
	
	public void commitCache(){
		Map<Product, Status> list = cache.getCache();
		for (Product p : list.keySet()) {
		    Status s = list.get(p);
		    if (s == Status.DELETED)
		    {
		    	da.delete(p);
		    }
		    else if (s == Status.UPDATED)
		    {
		    	da.update(p);
		    }
		    else if (s == Status.NEW)
		    {
		    	da.save(p);
		    }
		}
		cache.flush(); // Update cache statuses.
		System.out.println("Saved ");
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
