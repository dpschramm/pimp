package pimp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import pimp.productdefs.Product;

public class NodeItem extends DefaultMutableTreeNode{

    private int id;
    private String nodeName;
    private Object o;
    //Constructor that takes a product.
    public NodeItem(Product p){
    	this.id = id;
    	this.nodeName = p.toString();
    	this.o = p;
    }
    
    //Constructor that takes a class.
    public NodeItem(Class<?> c){
    	this.id = -1;
    	this.nodeName = extractName(c);
    	this.o = c;
    }
    
    public Object getStoredObject(){
    	return o;
    }

	public int getID(){
        return id;
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