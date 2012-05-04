package pimp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import pimp.productdefs.Product;

/**
 * 
 * @author Joel Mason
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