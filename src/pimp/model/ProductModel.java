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
 * @author Joel Mason
 *
 */

public class ProductModel {
	
	private ActionListener productsAddedListener;
	private ActionListener productsRemovedListener;
	private ActionListener productUpdatedListener;

	private Map<Product, Status> list;
	private ArrayList<String> classesLoaded;
	private Pimp controller;
	
	public ProductModel(Pimp controller){
		this.controller = controller;
		list = new HashMap<Product, Status>();
		classesLoaded = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param products
	 * @param status
	 */
	public void add(List<Product> products){
		for (Product p : products){
			System.out.println(Status.FRESH + ": " + p.toString());
			list.put(p, Status.FRESH);
		}
		if (productsAddedListener != null) {
			productsAddedListener.actionPerformed(new ActionEvent(products, 0, null));
		}
	}
	
	public void add(Product p){
		ArrayList<Product> l = new ArrayList<Product>();
		l.add(p);
		add(l);
	}
	
	public List<Product> get(String className){
		ArrayList<Product> l = new ArrayList<Product>();
		for (Product p : list.keySet()) {
			if (p.getClass().toString().equals(className)) {
				l.add(p);
			}
		}
		return l;
	}
	
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
		
		if (productsRemovedListener != null) {
			productsRemovedListener.actionPerformed(new ActionEvent(products, 0, null));
		}
	}
	
	/**
	 * This method takes a pointer to a product and updates it so it's state
	 * matches that of the updatedProduct. A productUpdatedListen is fired to
	 * indicate the product that has been updated.
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
	
	public void commit() {
		
		for (Map.Entry<Product, Status> entry : list.entrySet()) {
		    Product p = entry.getKey();
		    Status s = entry.getValue();
		    if (s == Status.DELETED)
		    {
		    	//DB.delete(p);
		    	list.remove(p);
		    }
		    else if (s == Status.UPDATED)
		    {
		    	//DB.update(p);
		    	s = Status.FRESH;
		    }
		    else if (s == Status.FRESH)
		    {
		    	System.out.println("Trying to save product " + p.toString());
		    	controller.saveToPersistance(p);
		    }
//		    else if (s == Status.FRESH)
//		    {
//		    	Do nothing
//		    }
		}
		
	}
	
	public void addToClassesLoaded(String s){
		classesLoaded.add(s);
	}
	
	public boolean isLoaded(String s){
		return classesLoaded.contains(s);
	}

	
	public void addProductsAddedListener(ActionListener a){
		productsAddedListener = a;
	}
	
	public void addProductsRemovedListener(ActionListener a){
		productsRemovedListener = a;
	}
	
	public void addProductUpdatedListener(ActionListener a){
		productUpdatedListener = a;
	}

	

	
}
