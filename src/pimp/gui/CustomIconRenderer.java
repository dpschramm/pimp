package pimp.gui;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class CustomIconRenderer extends DefaultTreeCellRenderer {
    ImageIcon classIcon;
    ImageIcon productIcon;
    
    
    public CustomIconRenderer() {
    	classIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/classIcon.png"));
    	productIcon = new ImageIcon(CustomIconRenderer.class.getResource("/images/productIcon.png"));
    	System.out.println("Renderer Initialised");
    }
   
    public Component getTreeCellRendererComponent(JTree tree,
      Object value,boolean sel,boolean expanded,boolean leaf,
      int row,boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, 
          expanded, leaf, row, hasFocus);
                
        	NodeItem n = (NodeItem)value;
        	Object o = n.getStoredObject();
        	JLabel label = (JLabel) this ;
        
	        if (o.getClass().equals(Class.class)) 
	        {
	            label.setIcon(classIcon);
	        } 
	        else 
	        {
	        	label.setIcon(productIcon);
	        } 
        
        return this;
    }
}