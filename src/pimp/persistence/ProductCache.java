package pimp.persistence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Product;

public class ProductCache {
	
	//0: Unmodified
	//1: Added (i.e. has been created the the New Product button)
	//2: Updated (you should not update an Added product)
	//3: Deleted
	
	private ActionListener productAddedListener;
	private ActionListener productRemovedListener;
	private ArrayList<CachedItem> list;
	
	public ProductCache(){
		list = new ArrayList<CachedItem>();
	}
	
	public void addToCache(List<Product> products, int status){
		for (Product p : products){
			CachedItem c = new CachedItem(p, status);
			list.add(c);
		}
		productAddedListener.actionPerformed(new ActionEvent(products, 0, null));
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
		productRemovedListener.actionPerformed(new ActionEvent(p, 0, null));
	}
	
	public void addProductAddedListener(ActionListener a){
		productAddedListener = a;
	}
	
	public void addProductRemovedListener(ActionListener a){
		productRemovedListener = a;
	}
}
