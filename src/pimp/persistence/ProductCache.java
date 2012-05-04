package pimp.persistence;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Product;

public class ProductCache {
	
	private ActionListener productAddedListener;
	private ActionListener productRemovedListener;
	private ArrayList<Product> list;
	
	public ProductCache(){
		list = new ArrayList<Product>();
	}
	
	public void addToCache(List<Product> products){
		for (Product p : products){
			list.add(p);
		}
		productAddedListener.actionPerformed(new ActionEvent(products, 0, null));
	}
	
	public ArrayList<Product> getFromCache(String className){
		ArrayList<Product> l = new ArrayList<Product>();
			for (Product p : list){
				if (p.getClass().toString().equals(className));
				l.add(p);
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
