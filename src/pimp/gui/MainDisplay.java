package pimp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import pimp.productdefs.Product;

/**
 * The main user interface window.
 * 
 * Q. Should this extend JFrame? We don't override any of JFrame's 
 * functionality so we could use a wrapper instead -DS.
 * 
 * @author Daniel Schramm, Joel Harrison, Ellie Rasmus, Joel Mason
 *
 */
public class MainDisplay extends JFrame {
	
	// Product tree.
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private JTree productTree;
	private HashMap<String, DefaultMutableTreeNode> nodeMap;
	
	//Keeping this reference to the dynamic form means we can remove it before replacing it with a new one.
	private JPanel dynamicForm;
	// Buttons.
	private JButton btnNew;
	private JButton btnDelete;
	
	/** 
	 * Constructor
	 */
	public MainDisplay() {
		// Exit application when close button clicked.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Add panels.
		getContentPane().add(createProductTreePanel(), BorderLayout.WEST);
		getContentPane().add(createButtonPanel(), BorderLayout.NORTH);
	}
	
	/**
	 * Show the gui.
	 */
	public void display() {
		pack();							// Size the window to fit contents.
		setLocationRelativeTo(null); 	// Center frame on screen.
		setVisible(true); 				// Show the gui.
	}
	
	private JComponent createProductTreePanel() {
		
		// Create the model.
		rootNode = new DefaultMutableTreeNode("Products");
		treeModel = new DefaultTreeModel(rootNode);
		nodeMap = new HashMap<String, DefaultMutableTreeNode>();
		
		// Create the GUI.
		productTree = new JTree(treeModel);
		productTree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent e) {
				System.out.println("hello");
			}
		});
		productTree.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		productTree.setShowsRootHandles(false);
		
		// Return containing Scroll Panel.
		JScrollPane treeScrollPanel = new JScrollPane(productTree);
		return treeScrollPanel;
	}
	
	/**
	 * This method creates and returns the button panel.
	 */
	private JPanel createButtonPanel() {	
		
		// Create New Product Button
		btnNew = new JButton("New");
		
		// Create Copy Product Button
		JButton btnCopy = new JButton("Copy");
		
		// Create Delete Product Button
		btnDelete = new JButton("Delete");
		
		
		// Create Load Product Button
		JButton btnLoadProducts = new JButton("Load Products");
		btnLoadProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
				//Needs to bring up dialog for xml file selection
			}
		});
		
		// Add buttons to panels.
		JPanel leftPanel = new JPanel(new FlowLayout());
		leftPanel.add(btnNew);
		leftPanel.add(btnCopy);
		leftPanel.add(btnDelete);
		
		JPanel rightPanel = new JPanel(new FlowLayout());
		rightPanel.add(btnLoadProducts);
		
		// Add panels to button panel.
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(leftPanel, BorderLayout.WEST);
		buttonPanel.add(rightPanel, BorderLayout.EAST);
		return buttonPanel;
	}
	
	public TreePath getSelectedTreePath(){
		return productTree.getSelectionPath();
	}
	
	public String getSelectedProductId(){
		
		return null;
	}
	
	public void removeTreeItem(MutableTreeNode node){
		DefaultTreeModel model = (DefaultTreeModel) productTree.getModel();
		model.removeNodeFromParent(node);
		repaint();
	}
	
	/**
	 * 
	 * @param products
	 */
	public void addToProductTable(List<Product> products) {
		for (Product p : products) {
			addToProductTable(p);
		}
	}
	
	/**
	 * 
	 * @param product
	 */
	public void addToProductTable(Product product){
		//DefaultMutableTreeNode node = new DefaultMutableTreeNode(product);	
		NodeItem node = new NodeItem(product.toString(), 1);
		DefaultMutableTreeNode parent = getNodeFromMap(product.getClass().toString());
		treeModel.insertNodeInto(node, (MutableTreeNode) parent, parent.getChildCount());
		productTree.scrollPathToVisible(new TreePath(node.getPath()));
		if(Modifier.isAbstract(product.getClass().getSuperclass().getModifiers()))
		{
			//Product is abstract
		}
	}
	
	/**
	 * Takes a classList and adds each class to the productTree
	 * @param classList
	 */
	public void addProductStructure(List<Class<?>> classList){
		for (Class<?> c : classList)
		{
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
			NodeItem node = new NodeItem(c.toString(), 1);
			//Get the appropriate parent node for this category
			parent = getNodeFromMap(c.getSuperclass().toString());
			//Add it to the nodeMap for future subcategories to use
			addToNodeMap(c.toString(), node);
			//insert the node into the tree and scroll it
			treeModel.insertNodeInto(node, parent, parent.getChildCount());
			productTree.scrollPathToVisible(new TreePath(node.getPath()));
		}
	}
	
	/**
	 * Method to determine whether the class has been added to the tree yet.
	 * Not entiely necessary but tidies code a little.
	 * @param c class name
	 * @return true if the class has been added to the tree.
	 */
	public boolean hasClassBeenAdded(Class<?> c){
		if (getNodeFromMap(c.toString()).equals(rootNode))
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
	public void addToNodeMap(String s, DefaultMutableTreeNode n){
		nodeMap.put(s, n);		
	}
	
	/**
	 * Used to insert subcategories under the correct node.
	 * @param s the class
	 * @return n the node (insertion point). If there is no correlating
	 * node it returns the rootNode - this happens when a tier 1 product type
	 * calls this method.
	 */
	public DefaultMutableTreeNode getNodeFromMap(String s){
		DefaultMutableTreeNode n = nodeMap.get(s);
		if (n == null){
			return rootNode;
		}
		return n;		
	}
	
	/**
	 * 
	 * @param form
	 */
	public void updateProductForm(JPanel form) {
		if(dynamicForm != null){
			getContentPane().remove(dynamicForm);
		}
		dynamicForm = form;
		getContentPane().add(dynamicForm, BorderLayout.CENTER);
		//We need both of these
		validate();
		repaint();
	}
	
	/**
	 * 
	 * @param npl
	 */
	public void addNewProductListener(ActionListener npl){
		btnNew.addActionListener(npl);
	}
	
	/**
	 * 
	 * @param tsl
	 */
	public void addTreeSelectionListener(TreeSelectionListener tsl){
		productTree.addTreeSelectionListener(tsl);
	}
	
	public void addDeleteButtonListener(ActionListener dbl){
		btnDelete.addActionListener(dbl);
	}
	
}
