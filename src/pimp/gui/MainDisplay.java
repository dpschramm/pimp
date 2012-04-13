package pimp.gui;

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
	
	private JTree productTree;
	private JButton btnNewProduct;
	private JScrollPane treeScrollPanel;
	
	/** Constructor 
	 * 
	 * Needs to be passed list of objects from database
	 */
	public MainDisplay() {
		// Exit application when close button clicked.
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Add panels.
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
	
	/**
	 * This method creates the product table.
	 */
	public void createProductTable(List<Product> products) {
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Products");
		for(Product product: products){
			DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(product/*.getName()*/);
			root.add(leaf);
		}
		productTree = new JTree(root);
		
		// Product Table ScrollPane Setup
		treeScrollPanel = new JScrollPane(productTree);
		
		// Add to main panel.
		getContentPane().add(treeScrollPanel, BorderLayout.WEST);
	}

	public void addToProductTable(Product product){
		DefaultTreeModel model = (DefaultTreeModel) productTree.getModel();
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(product/*.getName()*/);
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
		model.insertNodeInto(newNode, root, model.getChildCount(root));
		treeScrollPanel.revalidate();
		repaint();
	}
	
	/**
	 * This method creates and returns the button panel.
	 */
	private JPanel createButtonPanel() {
		
		// Create New Product Button
		btnNewProduct = new JButton("New Product");
		
		// Create Load Product Button
		JButton btnLoadProducts = new JButton("Load Products");
		btnLoadProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				/*JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");*/
				//Needs to bring up dialog for xml file selection
			}
		});
		
		// Create Edit Product Button
		JButton btnEdit = new JButton("Edit");
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
			}
		});
		
		// Create Copy Product Button
		JButton btnCopy = new JButton("Copy");
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
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
		
		// Add buttons to panels.
		JPanel leftPanel = new JPanel(new FlowLayout());
		leftPanel.add(btnNewProduct);
		leftPanel.add(btnLoadProducts);
		
		JPanel rightPanel = new JPanel(new FlowLayout());
		rightPanel.add(btnEdit);
		rightPanel.add(btnCopy);
		rightPanel.add(btnDelete);
		
		// Add panels to button panel.
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(leftPanel, BorderLayout.WEST);
		buttonPanel.add(rightPanel, BorderLayout.EAST);
		return buttonPanel;
	}
	
	public void updateProductForm(JPanel form) {
		getContentPane().add(form, BorderLayout.EAST);
	}
	
	public void addNewProductListener(ActionListener npl){
		btnNewProduct.addActionListener(npl);
	}
	
	public void addTreeSelectionListener(TreeSelectionListener tsl){
		productTree.addTreeSelectionListener(tsl);
	}
	
}
