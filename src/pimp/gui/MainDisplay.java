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
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;

import pimp.Main;
import pimp.Pimp;
import pimp.ProductEditor;

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
	private NewProductDialog npd;
	private Pimp p;
	private JButton btnNewProduct;
	
	/** Constructor 
	 * 
	 * Needs to be passed list of objects from database
	 */
	public MainDisplay() {
		this.p = p;
		// Frame settings.
		setResizable(false); // We currently use absolute positioning.
		setBounds(new Rectangle(0, 0, 800, 550));	
		
		// Main panel.
		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 786, 516);
		mainPanel.setLayout(null);
		
		// ??
		dynamicPanel = new JScrollPane();
		dynamicPanel.setBounds(343, 66, 416, 450);
		mainPanel.add(dynamicPanel);
		
		// Button Setup
		createButtons();
		
		// Product Table Setup
		createProductTable();
		
		// Add main panel to frame.
		getContentPane().setBounds(new Rectangle(0, 0, 800, 550));
		getContentPane().setLayout(null);
		getContentPane().add(mainPanel);
	}
	
	/**
	 * This method creates the product table.
	 */
	private void createProductTable() {
		
		// Create tree.
		productTree = new JTree();
		
		// Create table panel
		/*DefaultMutableTreeNode top =
        new DefaultMutableTreeNode("The Java Series");
    createNodes(top);
    tree = new JTree(top);*/
		JScrollPane treeScrollPanel = new JScrollPane(productTree);
		treeScrollPanel.setBounds(12, 66, 300, 450);
		// Add to main panel.
		mainPanel.add(treeScrollPanel);
	}
	
	/**
	 * 
	 */
	private void populateProductTable(){
		
	}
	
	/**
	 * This method contains all the button setup code.
	 */
	private void createButtons() {	
		
		// Create New Product Button
		btnNewProduct = new JButton("New Product");
		////////////////////////////////////////////////////////////npd = new NewProductDialog(this);
		
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

}
