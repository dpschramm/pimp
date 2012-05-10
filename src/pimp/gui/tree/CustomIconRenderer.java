package pimp.gui.tree;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * CustomIconRenderer is used to give the tree nodes a better affordance for the user.
 * Using the default renderer, classes would appear as leaves (and thus, didn't look liek they
 * were expandable) when really they needed to be expanded in order to load their children.
 * 
 * This class takes the storedObject of the node and interprets it, setting
 * a custom graphic dependent on its content (and state)
 * @author Joel
 *
 */

public class CustomIconRenderer implements TreeCellRenderer {
    
    private DefaultTreeCellRenderer defaultRenderer;
    
    public CustomIconRenderer() {
    	ImageIcon closedIcon = new ImageIcon(CustomIconRenderer.class.getResource("images/classIcon.png"));
    	ImageIcon openIcon = new ImageIcon(CustomIconRenderer.class.getResource("images/openIcon.png"));
    	ImageIcon productIcon = new ImageIcon(CustomIconRenderer.class.getResource("images/productIcon.png"));
    	defaultRenderer = new DefaultTreeCellRenderer();
    	defaultRenderer.setLeafIcon(productIcon);
    	defaultRenderer.setClosedIcon(closedIcon);
    	defaultRenderer.setOpenIcon(openIcon);
    }
   
    public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded,
			boolean leaf, int row, boolean hasFocus) {
    
    	// Check if is class node or product node.
    	NodeItem node = (NodeItem) value;
        if (node.isClassNode()) {
            leaf = false;
        }
        
        return defaultRenderer.getTreeCellRendererComponent(tree, value, sel, 
				expanded, leaf, row, hasFocus);
    }
}