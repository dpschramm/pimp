package pimp.persistence;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import pimp.productdefs.Product;

public class ProductCache {
	
	private ActionListener modelChangedListener;
	private ArrayList<Product> list;
	
	public ProductCache(){
		list = new ArrayList<Product>();
	}
	
	private void addToCache(List<Product> products){
		for (Product p : products){
			list.add(p);
		}
	}
	
	private ArrayList<Product> getFromCache(Class <?> c){
		ArrayList<Product> l = new ArrayList<Product>();
			for (Product p : list){
				if (p.getClass().equals(c));
				l.add(p);
			}
		return l;
	}
}
