package pimp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import pimp.model.Product;

/**
 * 
 * @author Joel Mason
 * NodeItems were originally designed with the goal to store a String representing a product
 * and a hidden ID field corresponding to the product's unique ID. This was to ensure we didn't 
 * have all products in memory.
 * 
 * With the switch to a caching system we probably could've gotten away with DefaultMutableTreeNodes
 * but it's always handy having your own class extensions.
 *
 */

public class NodeItem extends DefaultMutableTreeNode{

    private String nodeName;
    private Object o;
    
    //Constructor that takes a product.
    public NodeItem(Product p){
    	this.nodeName = p.toString();
    	this.o = p;
    }
    
    //Constructor that takes a class.
    public NodeItem(Class<?> c){
    	this.nodeName = extractName(c);
    	this.o = c;
    }
    
    public Object getStoredObject(){
    	return o;
    }
    
    public void setName(Product p){
    	nodeName = p.toString();
    }
    
    @Override
    public String toString() {
        return this.nodeName;
    }
    
	//Makes the class name prettier.
	public String extractName(Class<?> c){
		String s = c.toString();
		return s.substring(s.lastIndexOf('.')+1);
	}
}