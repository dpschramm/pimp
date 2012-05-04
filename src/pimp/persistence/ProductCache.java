package pimp.persistence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Product;

public class ProductCache {
	
	private ActionListener productsAddedListener;
	private ActionListener productsRemovedListener;
	private ArrayList<CachedItem> list;
	
	public ProductCache(){
		list = new ArrayList<CachedItem>();
	}
	
	/**
	 * 
	 * @param products
	 * @param status
	 */
	public void addToCache(List<Product> products, Status status){
		for (Product p : products){
			CachedItem c = new CachedItem(p, status);
			list.add(c);
		}
		productsAddedListener.actionPerformed(new ActionEvent(products, 0, null));
	}
	
	public ArrayList<Product> getFromCache(String className){
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
	
	public void removeFromCache(Product p){
		list.remove(p);
		productsRemovedListener.actionPerformed(new ActionEvent(p, 0, null));
	}
	
	public void addProductsAddedListener(ActionListener a){
		productsAddedListener = a;
	}
	
	public void addProductsRemovedListener(ActionListener a){
		productsRemovedListener = a;
	}
}
