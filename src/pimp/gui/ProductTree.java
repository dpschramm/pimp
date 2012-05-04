package pimp.gui;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pimp.productdefs.Product;

public class ProductTree extends JTree {
	
	// Product tree.
	private DefaultMutableTreeNode root;
	private DefaultTreeModel model;

	private HashMap<String, NodeItem> map;
	
	private MainDisplay parent;
	
	public ProductTree(MainDisplay parent) {
		super();
		
		this.parent = parent;
		
		// Create the model.
		root = new DefaultMutableTreeNode("Products");
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
				TreePath path = event.getNewLeadSelectionPath();
				NodeItem selectedNode = (NodeItem) path.getLastPathComponent();
				if (!selectedNode.getStoredClass().equals(null)){
					//Fire class selected event
					
					//updateSelection(selectedNode);
				}
			}
		});
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
	public Product removeSelectedProduct() {
		TreePath selectionPath = getSelectionPath();
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
				selectionPath.getLastPathComponent();
		
		Product product = (Product) selectedNode.getUserObject();
		
		Class<?> c = product.getClass();
		if(!Modifier.isAbstract(c.getModifiers())){
			removeNode(selectedNode);
		}
		return product;
	}
	
	/**
	 * @param products
	 */
	public void addProduct(List<Product> products) {
		for (Product p : products) {
			addProduct(p);
		}
	}
	
	/**
	 * @param product
	 */
	public void addProduct(Product product){	
		NodeItem node = new NodeItem(1, product.toString());
		DefaultMutableTreeNode parent = getNodeFromMap(product.getClass().toString());
		model.insertNodeInto(node, (MutableTreeNode) parent, parent.getChildCount());
		scrollPathToVisible(new TreePath(node.getPath()));
	}
	
//	public void addProduct(Map<Integer, String> products) {
//		
//		Iterator it = products.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<Integer, String> pairs = (Map.Entry)it.next();
//			addProduct(pairs.getKey(), pairs.getValue());
//		}
//		
//	}
	
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
			DefaultMutableTreeNode parent;
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
	public DefaultMutableTreeNode getNodeFromMap(String s){
		DefaultMutableTreeNode n = map.get(s);
		if (n == null) {
			return root;
		}
		return n;	
	}
	
	//This will be used if we use product IDs
	private Product getProductFromTree(){
		// Get id from tree
		TreePath path = getSelectionPath();
		if(path != null){
			NodeItem selectedNode = (NodeItem) path.getLastPathComponent();
			int id = selectedNode.getID();
			// Get product from id
			return null;
		}
		return null;
	}
	
}
