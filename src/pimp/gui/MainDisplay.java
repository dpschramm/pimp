package pimp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pimp.Pimp;
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
	
	// Views.
	private JPanel dynamicForm; /* Keeping this reference to the dynamic form
	means we can remove it before replacing it with a new one. */
	private JFrame frame;
	
	// Models.
	private ProductTree tree;
	private List<Product> products;
	
	// Controller.
	private Pimp controller;
	
	/** 
	 * Constructor
	 */
	public MainDisplay(Pimp controller) {
		
		this.controller = controller;
		
		frame = new JFrame();
		
		// Exit application when close button clicked.
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create product tree.
		tree = new ProductTree();
		JScrollPane treeScrollPanel = new JScrollPane(tree);
		
		// Add panels.
		frame.getContentPane().add(treeScrollPanel, BorderLayout.WEST);
		frame.getContentPane().add(createButtonPanel(), BorderLayout.NORTH);
	}
	
	/**
	 * Show the gui.
	 */
	public void display() {
		frame.pack();							// Size the window to fit contents.
		frame.setLocationRelativeTo(null); 		// Center frame on screen.
		frame.setVisible(true); 				// Show the gui.
	}
	
	/**
	 * This method creates and returns the button panel.
	 */
	private JPanel createButtonPanel() {	
		
		// Create New Button
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new newProductListener());
		
		// Create Copy Button
		JButton btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new copyButtonListener());
		
		// Create Delete Button
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new deleteButtonListener());
		
		// Create Load Products Button
		JButton btnLoadProducts = new JButton("Load Products");
		btnLoadProducts.addActionListener(new loadButtonListener());
		
		// Create Load Product Button
		JButton btnSaveProducts = new JButton("Save Products");
		btnSaveProducts.addActionListener(new saveButtonListener());
		
		// Add buttons to panels.
		JPanel leftPanel = new JPanel(new FlowLayout());
		leftPanel.add(btnNew);
		leftPanel.add(btnCopy);
		leftPanel.add(btnDelete);
		
		JPanel rightPanel = new JPanel(new FlowLayout());
		rightPanel.add(btnLoadProducts);
		rightPanel.add(btnSaveProducts);
		
		// Add panels to button panel.
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(leftPanel, BorderLayout.WEST);
		buttonPanel.add(rightPanel, BorderLayout.EAST);
		return buttonPanel;
	}
	
	/**
	 * This ActionListener is applied to the New button on the main gui. 
	 * When clicked it needs to launch a NewProductDialog, retriegui.updateProductForm(form);ve the input
	 * from that and create a product of the returned type
	 */
	class newProductListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Get selected class (will be null if they clicked cancel).
			Product p = controller.getProduct();
			// Check to make sure user made a selection.
			if (p != null) {
				tree.addToProductTable(p);
				products.add(p);
				// Debug.
				System.out.println("You selected to create a " + p.getClass().getName());
			}
			else System.out.println("No selection.");
		}
	}
	
	/**
	 * Delete the specified product.
	 * 
	 * @param p
	 */
	class deleteButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// Get selected product from tree
			TreePath selectionPath = tree.getSelectionPath();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
					selectionPath.getLastPathComponent();
			Object selectedObject = selectedNode.getUserObject();
			Class<?> c = selectedObject.getClass();
			//if the selected object is not an abstract class
			if(!Modifier.isAbstract(c.getModifiers())){
				tree.removeTreeItem(selectedNode);
			}
			// Remove object from collection in memory
			products.remove((Product)selectedObject);
			
			// Erase from xml
			// This is done by overwriting the file with the new, smaller list of products	
		}
		
	}	
	
	class copyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(getContentPane(), 
			"Not yet implemented.");
			
			// Get selected product from tree
			
			// Create new copy of product, with different name	
		}	
	}
	
	/** 
	 * Save products to file.
	 */
	class saveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(getContentPane(), 
					"Not yet implemented.");
		}
		
	}
	
	/**
	 * Load products from file.
	 */
	class loadButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(getContentPane(), 
					"Not yet implemented.");
			//Needs to bring up dialog for xml file selection
		}
		
	}
	
	/**
	 * 
	 * @param form
	 */
	public void updateProductForm(JPanel form) {
		if(dynamicForm != null){
			frame.getContentPane().remove(dynamicForm);
		}
		dynamicForm = form;
		frame.getContentPane().add(dynamicForm, BorderLayout.CENTER);
		//We need both of these
		frame.validate();
		frame.repaint();
	}
	
	public void setClasses(List<Class<?>> classList) {
		// TODO Auto-generated method stub
		tree.addProductStructure(classList);
	}

	public void setProducts(List<Product> products) {
		// TODO Auto-generated method stub
		tree.addToProductTable(products);
	}
}
