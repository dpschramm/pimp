package pimp.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTree;

import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.JScrollPane;

import pimp.Pimp;
import pimp.productdefs.Product;

/**
 * The main user interface window.
 * 
 * @author Daniel Schramm, Joel Harrison, Ellie Rasmus
 *
 */
public class MainDisplay extends JFrame {
	public JTree productTree;
	public JScrollPane dynamicPanel; // I don't think this should be public, we need getters/setters.
	private JPanel mainPanel;
	private JButton btnNewProduct;
	private JScrollPane treeScrollPanel;
	
	/** Constructor 
	 * 
	 * Needs to be passed list of objects from database
	 */
	public MainDisplay() {
		// Frame settings.
		setResizable(false); // We currently use absolute positioning.
		setBounds(new Rectangle(0, 0, 800, 550));	
		
		// Main panel.
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 786, 516);
		mainPanel.setLayout(null);
		
		// ??
		dynamicPanel = new JScrollPane();
		dynamicPanel.setBounds(205, 66, 571, 450);
		mainPanel.add(dynamicPanel);
		
		// Button Setup
		createButtons();
		
		
		
		// Add main panel to frame.
		getContentPane().setBounds(new Rectangle(0, 0, 800, 550));
		getContentPane().setLayout(null);
		getContentPane().add(mainPanel);
	}
	
	/**
	 * Show the gui.
	 */
	public void display() {
		//pack();							// Size the window to fit contents.
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
		treeScrollPanel.setBounds(12, 66, 300, 450);
		// Add to main panel.
		mainPanel.add(treeScrollPanel);
		productTree.setVisible(true);
		treeScrollPanel.revalidate();
		repaint();
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
	 * This method contains all the button setup code.
	 */
	private void createButtons() {	
		
		// Create New Product Button
		btnNewProduct = new JButton("New Product");
	
		btnNewProduct.setBounds(12, 17, 144, 25);
		
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
		btnLoadProducts.setBounds(168, 17, 144, 25);
		
		// Create Edit Product Button
		JButton btnEdit = new JButton("Edit");
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
			}
		});
		btnEdit.setBounds(363, 17, 117, 25);
		
		// Create Copy Product Button
		JButton btnCopy = new JButton("Copy");
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
			}
		});
		btnCopy.setBounds(492, 17, 117, 25);
		
		// Create Delete Product Button
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
			}
		});
		btnDelete.setBounds(621, 17, 117, 25);
		
		// Add buttons to main panel.
		mainPanel.add(btnNewProduct);
		mainPanel.add(btnLoadProducts);
		mainPanel.add(btnEdit);
		mainPanel.add(btnCopy);
		mainPanel.add(btnDelete);
	}
	
	public void addNewProductListener(ActionListener npl){
		btnNewProduct.addActionListener(npl);
	}
	
	public void addTreeSelectionListener(TreeSelectionListener tsl){
		productTree.addTreeSelectionListener(tsl);
	}
	
	public void updateProductForm(JPanel form) {
		//Where should this kind of logic be? Oh this is terribly confusing.
		form.setBounds(0, 0, dynamicPanel.getWidth(), dynamicPanel.getHeight());
		dynamicPanel.removeAll();
		dynamicPanel.add(form);
		form.setVisible(true);
		validate();
		setVisible(true);
		repaint();
	}
}
