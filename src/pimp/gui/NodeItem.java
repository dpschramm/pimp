package pimp.gui;

import javax.swing.tree.DefaultMutableTreeNode;

public class NodeItem extends DefaultMutableTreeNode{
	
    private String nodeName;
    private int id;
    public NodeItem(String nodeName, int id){
    	this.id = id;
    	this.nodeName = nodeName;
    }
    public String getName(){
       return nodeName;
    }
    public int getID(){
        return id;
     }
    
    @Override
    public String toString() {
        return this.nodeName;
    }
}