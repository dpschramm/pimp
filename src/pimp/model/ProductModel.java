package pimp.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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

	private ArrayList<CachedItem> list;
	private ArrayList<String> classesLoaded;
	
	public ProductModel(){
		list = new ArrayList<CachedItem>();
		classesLoaded = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param products
	 * @param status
	 */
	public void add(List<Product> products){
		for (Product p : products){
			CachedItem c = new CachedItem(p, Status.FRESH);
			System.out.println(Status.FRESH + ": " + p.toString());
			list.add(c);
		}
		productsAddedListener.actionPerformed(new ActionEvent(products, 0, null));
	}
	
	public void add(Product p){
		ArrayList<Product> l = new ArrayList<Product>();
		l.add(p);
		add(l);
	}
	
	public ArrayList<Product> get(String className){
		ArrayList<Product> l = new ArrayList<Product>();
			for (CachedItem c : list){
				Product p = (Product) c.getProduct();
				if (p.getClass().toString().equals(className));
				{
					l.add(p);
				}
			}
		return l;
	}
	
	
	public void delete(List<Product> products){
		for (Product p : products){
			for (CachedItem c : list){

				if (list.get(list.indexOf(c)).getProduct().equals(p)){
					list.get(list.indexOf(c)).setStatus(Status.DELETED);
					System.out.println("deleted");
				}
			}			
		}
		productsRemovedListener.actionPerformed(new ActionEvent(products, 0, null));
	}
	
	public void update(Product product, Product changes) {
		for (CachedItem c : list){
			if (list.get(list.indexOf(c)).getProduct().equals(product))
			{
				
				
				
				list.get(list.indexOf(c)).setStatus(Status.UPDATED);
				System.out.println("Updated");
			}
		}			
		productUpdatedListener.actionPerformed(new ActionEvent(product, 0, null));
	}
	
	public void addToClassesLoaded(String s){
		classesLoaded.add(s);
	}
	
	public boolean isLoaded(String s){
		if(classesLoaded.contains(s))
		{
			return true;
		}
		return false;
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
