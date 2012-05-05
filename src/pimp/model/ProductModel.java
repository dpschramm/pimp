package pimp.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pimp.Pimp;
import pimp.productdefs.Product;

/**
 * 
 * @author Joel Mason, Daniel Schramm
 * 
 * ProductModel is effectively a cache, or was designed as such. It was intended to reduce calls to the database by buffering 
 * objects in memory, which at the time was our main model. However, after implementing it we transitioned towards having the cache as our main model
 * of data and only really using the database for persistence on program exit etc - rather than saving every change to the database then updating the view
 * with the changes, we buffer the changes to this ProductModel and then update the view via listeners.
 * 
 * This means that the tree nodes don't have to contain any extra information like Strings and IDs and maintain maps to products - they are
 * all just pointers to the products in the cache.
 *
 */

public class ProductModel {
	
	private ActionListener productsAddedListener;
	private ActionListener productsDeletedListener;
	private ActionListener productUpdatedListener;

	private Map<Product, Status> list;
	private ArrayList<String> classesLoaded;
	
	public ProductModel(){
		list = new HashMap<Product, Status>();
		classesLoaded = new ArrayList<String>();
	}
	
	/**
	 * Adds a single product to the list. This will only get called when a brand new product is created
	 * via the NEW PRODUCT button on the gui. Thus, only one is created at a time. It marks it with the flag Status.NEW
	 * to indicate that it doesn't exist in the database.
	 * @param p the product to add
	 */
	public void add(Product p){
		list.put(p, Status.NEW);
		if (productsAddedListener != null) {
			productsAddedListener.actionPerformed(new ActionEvent(p, 0, null));
		}
	}
	
	/**
	 * Load on the other hand is in charge of taking items pulled from the database - typically, there will be more than
	 * one product to add, so it takes a list. It marks it with the flag Status.FRESH to indicate that it's untouched.
	 * @param products
	 */
	public void load(List<Product> products){
		for (Product p : products){
			System.out.println(Status.FRESH + ": " + p.toString());
			list.put(p, Status.FRESH);
		}
		if (productsAddedListener != null) {
			productsAddedListener.actionPerformed(new ActionEvent(products, 0, null));
		}
	}
	
	/**
	 * TODO: Does this ever actually get called?
	 * 
	 * @param className
	 * @return
	 */
//	public List<Product> get(String className){
//		ArrayList<Product> l = new ArrayList<Product>();
//		for (Product p : list.keySet()) {
//			if (p.getClass().toString().equals(className)) {
//				l.add(p);
//			}
//		}
//		return l;
//	}
	
	/**
	 * Flags a list of products in the cache for deletion
	 * As this comes from the DELETE button on the gui, and that can return a single product 
	 * (if the user selected a leaf) or a list of products (if the user selected a category/class)
	 * this method takes a list.
	 * @param products the products to be flagged.
	 */
	public void delete(List<Product> products){
		for (Product p : products){
			// Remove the product from the cache if not persisted.
			if (list.get(p) == Status.NEW) {
				list.remove(p);
			}
			// Otherwise mark for deletion next sync.
			else {
				list.put(p, Status.DELETED);
			}
			System.out.println("Deleted product: " + p);
		}
		
		if (productsDeletedListener != null) {
			productsDeletedListener.actionPerformed(new ActionEvent(products, 0, null));
		}
	}
	
	/**
	 * This method is called when the controller decides to persist the changed buffered in the cache.
	 * Items marked for deletion are deleted from the database and then this is called to remove it from the cache.
	 * @param p the product to delete
	 */
	public void delete(Product p){
		ArrayList<Product> l = new ArrayList<Product>();
		l.add(p);
		delete(l);
	}
	
	/**
	 * This method takes a pointer to a product and updates it so it's state
	 * matches that of the updatedProduct. A productUpdatedListen is fired to
	 * indicate the product that has been updated.
	 * 
	 * It cannot simply replace the product as our whole framework relies on the 
	 * assumption that there is a central model and everything else is just a pointer
	 * to that. 
	 * 
	 * @param product the product to be updated.
	 * @param updatedProduct the target product.
	 */
	public void update(Product product, Product updatedProduct) {
		
		// Copy the fields.
		Field[] fields = product.getClass().getFields();
		for (Field f : fields) {
			try {
				f.set(product, f.get(updatedProduct));
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Only change status if it is fresh from the database.
		if (list.get(product) == Status.FRESH) {
			//list.get(product).setStatus(Status.UPDATED);
		}
		
		// Fire update event.
		if (productUpdatedListener != null){
			productUpdatedListener.actionPerformed(new ActionEvent(product, 0, null));
		}
		
		System.out.println("Updated product: " + product);
	}
	
	/**
	 * Returns the whole cache. This is used when the controller wants to process the
	 * cache and persist the changes.
	 * @return the cache
	 */
	public Map<Product, Status> getCache(){		
		return list;
	}
	
	/**
	 * Called after changes are committed. Logically, any products in the cache are identical
	 * to those in the persistence layer after a 'commit', so mark them as FRESH (as if they had just been loaded)
	 */
	public void flushCache(){
		for (Map.Entry<Product, Status> entry : list.entrySet()) {
		    Status s = entry.getValue();
		    s = Status.FRESH;

		}
	}
	
	/**
	 * Identify that a class's products have been loaded from the persistence layer.
	 * @param s
	 */
	public void addToClassesLoaded(String s){
		classesLoaded.add(s);
	}
	
	/**
	 * A quick lookup to see if the class has been loaded from the persistence layer yet.
	 * @param s the class name
	 * @return
	 */
	public boolean isLoaded(String s){
		return classesLoaded.contains(s);
	}

	
	/** Self explanatory.
	 * 
	 * @param a
	 */
	public void addProductsAddedListener(ActionListener a){
		productsAddedListener = a;
	}
	
	public void addProductsDeletedListener(ActionListener a){
		productsDeletedListener = a;
	}
	
	public void addProductUpdatedListener(ActionListener a){
		productUpdatedListener = a;
	}

	

	
}
