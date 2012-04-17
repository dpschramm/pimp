package pimp.gui;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JScrollPane;

import pimp.productdefs.Product;

/**
 * The main user interface window.
 * 
 * Q. Should this extend JFrame? We don't override any of JFrame's 
 * functionality so we could use a wrapper instead -DS.
 * 
 * @author Daniel Schramm, Joel Harrison, Ellie Rasmus
 *
 */
public class MainDisplay extends JFrame {
	
	// Product tree.
	private DefaultMutableTreeNode rootNode;
	private DefaultTreeModel treeModel;
	private JTree productTree;
	//Keeping this reference to the dynamic form means we can remove it before replacing it with a new one.
	private JPanel dynamicForm;
	// Buttons.
	private JButton btnNew;
	
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
		
		// Create the GUI.
		productTree = new JTree(treeModel);
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
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		
		// Create Delete Product Button
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
			}
		});
		
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
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(product);
		treeModel.insertNodeInto(node, rootNode, rootNode.getChildCount());
		productTree.setSelectionPath(new TreePath(node.getPath()));
		System.out.println("Added product: '" + product + "'");
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
	
}
