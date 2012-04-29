package pimp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import pimp.productdefs.Product;

public class NodeItem extends DefaultMutableTreeNode{
	
    private Object o;
    private int id;
    private String nodeName;
    
    //Constructor that takes a product.
    public NodeItem(Product p, int id){
    	this.id = id;
    	this.o = p;
    	this.nodeName = p.toString();
    }
    
    //Constructor that takes a class.
    public NodeItem(Class c, int id){
    	this.id = id;
    	this.nodeName = extractName(c);
    	this.o = c;
    }
    
    public Object getObject(){
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
	public String extractName(Class c){
		String s = c.toString();
		return s.substring(s.lastIndexOf('.')+1);
	}
}