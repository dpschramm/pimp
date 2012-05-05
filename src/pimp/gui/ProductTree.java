package pimp.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pimp.productdefs.Product;
/**
 * 
 * @author Joel Mason
 *
 */

public class ProductTree extends JTree {
	
	// Product tree.
	private NodeItem root;
	private DefaultTreeModel model;
    private ActionListener classSelectListener;
	private HashMap<String, NodeItem> map;
	
	
	public ProductTree() {
		super();
		
		//this.parent = parent;
		
		// Create the model.
		root = new NodeItem(Product.class);
		model = new DefaultTreeModel(root);
		map = new HashMap<String, NodeItem>();
		
		// Set model and settings.
		setModel(model);
		getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setShowsRootHandles(false);
		
		// Selection listener.
		addTreeSelectionListener(new TreeSelectionListener() {
			/* Retrieve the selected object and creates a new dynamic form to display the 
			 * product's attributes.
			 */
			@Override
			public void valueChanged(TreeSelectionEvent event) {
				if(event.isAddedPath()){
					retrieveAndSave(); 
				
					TreePath path = event.getNewLeadSelectionPath();
					NodeItem selectedNode = (NodeItem) path.getLastPathComponent();
					Object o = selectedNode.getStoredObject();
					if (o.getClass().equals(Class.class)){
						//Need to figure out what the source is - it can't be null.
						String s = o.toString();
						ActionEvent i = new ActionEvent(model, 0, s);
						classSelectListener.actionPerformed(i);
						System.out.println("Class");
					}
					else
					{
						//parent.updateProductForm((Product)selectedNode.getStoredObject());
						updateParentForm((Product)selectedNode.getStoredObject());
					}
				}
			}
		});
		
		addTreeExpansionListener(new TreeExpansionListener() {
			/* Retrieve the selected object and creates a new dynamic form to display the 
			 * product's attributes.
			 */

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				TreePath path = (TreePath) event.getPath();
				if (!(path == null)){
				NodeItem selectedNode = (NodeItem) path.getLastPathComponent();
				Object o = selectedNode.getStoredObject();
				if (o.getClass().equals(Class.class)){
//					//Need to figure out what the source is - it can't be null.
					String s = o.toString();
					ActionEvent i = new ActionEvent(model, 0, s);
					classSelectListener.actionPerformed(i);
				}
				}
			}
		});
		
		
	}
	
	
	
	// This exists because we can't refer to parent from in that inner class up there
	// We could move that code from out of the constructor at some stage
	private void retrieveAndSave(){
		/*Product p =*/ 
		//parent.saveCurrentChanges();	
	}

	private void updateParentForm(Product p){
		//parent.updateProductForm(p);
	}
	
	public void addClassSelectListener(ActionListener a){
		classSelectListener = a;
	}

	
	public void removeNode(MutableTreeNode node){
		model.removeNodeFromParent(node);
		repaint();
	}
	
	public void empty() {
		root.removeAllChildren();
		model.setRoot(root);
	}
	
	/**
	 * This should be re-written as two separate methods; one that gets the
	 * currently selected product, and the other that removes a specified
	 * product.
	 * 
	 * @return the product that was removed.
	 */
	public ArrayList<Product> getSelectedProduct() {
		
		ArrayList<Product> products = new ArrayList<Product>();
		
		TreePath selectionPath = getSelectionPath();
		NodeItem selectedNode = (NodeItem)
				selectionPath.getLastPathComponent();
		
		Object o = selectedNode.getStoredObject();
		
		Class<?> c = o.getClass();
		if(!(selectedNode == root)){
			if(!o.getClass().equals(Class.class)){				
				products.add((Product) o);
			}
			else
			{
				int n = JOptionPane.showConfirmDialog(
					    this.getParent(),
					    "This will delete all products in this category\n" + "Are you sure you wish to do this?" ,
					    "An Inane Question",
					    JOptionPane.YES_NO_OPTION);
				if (n == 0)
				{
					//Get the children
					Enumeration<NodeItem> children = selectedNode.children();
					while (children.hasMoreElements())
					{
						NodeItem child = (NodeItem) children.nextElement();
						if (child.getStoredObject().equals(Product.class)){
							products.add((Product) child.getStoredObject());
						}
					}
				}
			}
		}
		return products;
	}
	/**
	 * @param product
	 */
	public void addProduct(Product p){	
		NodeItem node = new NodeItem(p);
		NodeItem parent = getNodeFromMap(p.getClass().toString());
		insertNode(node, parent);
//		model.insertNodeInto(node, (MutableTreeNode) parent, parent.getChildCount());
//		scrollPathToVisible(new TreePath(node.getPath()));
	}
	

	/*
	 * New methods for adding products. This one takes the whole map of products within a class
	 */
	public void addProduct(Map<Integer, Product> products, String className) {
		NodeItem p = map.get(className);
		//Huuuuge hack to make it work for the time being.
		//TODO: Remove when we have a cache.
		p.removeAllChildren();
		for (Map.Entry<Integer, Product> entry : products.entrySet()) {
		    Integer key = entry.getKey();
		    Product value = entry.getValue();
		    NodeItem n = new NodeItem(value);
		    insertNode(n, p);
		}
	}
	
	/*
	 * This one adds a single product under a node.
	 */
	
	private void insertNode(NodeItem n, NodeItem p) {
		model.insertNodeInto(n, (MutableTreeNode) p, p.getChildCount());
		scrollPathToVisible(new TreePath(n.getPath()));
	}
	
	
//	/**
//	 * @param products
//	 */
//	public void addProduct(List<Product> products) {
//		for (Product p : products) {
//			addProduct(p);
//		}
//	}
//


	
	/**
	 * Takes a classList and adds each class to the productTree
	 * @param classList
	 */
	public void addProductStructure(List<Class<?>> classList){
		for (Class<?> c : classList) {
			addProductStructure(c);
		}
	}
	
	/**
	 * Adds a class to the productTree. These are the abstract product types and
	 * serve as parent nodes to product instances (e.g. Jacket XXL Red)
	 * and also abstract product subtypes (e.g. Jackets)
	 * @param c the class to add.
	 */
	public void addProductStructure(Class<?> c){
		//if c has a superclass that isn't Product (i.e if it's a subcategory)
		//yet the superclass hasn't been added yet, recursively call this method
		//until we either get to a category that HAS been added, or start at the top
		//category and recurse our way back down.
		if (c.getSuperclass() != Product.class && !hasClassBeenAdded(c.getSuperclass()))
		{
			addProductStructure(c.getSuperclass());
		}
		//Due to the above statement, classes may have been added in a different order
		//to how they are stored in the classList. This stops them re-adding themselves.
		//You cannot put this check around the initial method call as it will not work.
		if (!hasClassBeenAdded(c))
		{
			NodeItem parent;
			NodeItem node = new NodeItem(c);
			//Get the appropriate parent node for this category
			parent = getNodeFromMap(c.getSuperclass().toString());
			//Add it to the nodeMap for future subcategories to use
			addToNodeMap(c.toString(), node);
			//insert the node into the tree and scroll it
			model.insertNodeInto(node, parent, parent.getChildCount());
			scrollPathToVisible(new TreePath(node.getPath()));
		}
	}
	
	/**
	 * Method to determine whether the class has been added to the tree yet.
	 * Not entirely necessary but tidies code a little.
	 * @param c class name
	 * @return true if the class has been added to the tree.
	 */
	public boolean hasClassBeenAdded(Class<?> c){
		if (getNodeFromMap(c.toString()).equals(root))
		{
			return false;
		}
		return true;	
	}
	
	/**
	 * Insert item to the nodeMap
	 * @param s the class's toString() value
	 * @param n the node 
	 */
	public void addToNodeMap(String s, NodeItem n){
		map.put(s, n);		
	}
	
	/**
	 * Used to insert subcategories under the correct node.
	 * @param s the class
	 * @return n the node (insertion point). If there is no correlating
	 * node it returns the rootNode - this happens when a tier 1 product type
	 * calls this method.
	 */
	public NodeItem getNodeFromMap(String s){
		NodeItem n = map.get(s);
		if (n == null) {
			return root;
		}
		return n;	
	}

	public void removeProduct(Product p) {
		removeNode(p);
	}
	
	public void removeNode(Product p){
		NodeItem n;
		Class<?> c = p.getClass();
		NodeItem parent = getNodeFromMap(c.toString());
		Enumeration<NodeItem> children = parent.children();
		
		while (children.hasMoreElements()){
			NodeItem child = children.nextElement();
			if(child.getStoredObject().equals(p))
			{
				removeNode(child);
			}
			System.out.println(parent.toString());
		}
	}
	
}
