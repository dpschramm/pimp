package pimp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import pimp.Pimp;
import pimp.form.FormBuilder;
import pimp.persistence.DataAccessor;
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
		
		// Create product list.
		products = new ArrayList<Product>();
		
		// Create product tree.
		tree = new ProductTree(this);
		tree.addClassSelectListener(new classChangedListener());
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
		JButton btnLoadProducts = new JButton("Open");
		btnLoadProducts.addActionListener(new OpenButtonListener());
		
		// Create Load Product Button
		JButton btnSaveProducts = new JButton("Export");
		btnSaveProducts.addActionListener(new ExportButtonListener());
		
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
				tree.addProduct(p);
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
			// Remove selected product from tree
			Product product = tree.removeSelectedProduct();
			products.remove(product);
			
			// Erase from xml
			// This is done by overwriting the file with the new, smaller list of products
		}
		
	}	
	
	class classChangedListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			Map<Integer, String> m = controller.getProductsByClass(e.getActionCommand());
			tree.addProduct(m, e.paramString());
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
	class ExportButtonListener implements ActionListener {
		/**
		 * Brings up a dialog to choose/create new database location.
		 * The current database will be copied to the new location, 
		 * and all subsequent database transactions will be performed at
		 * the new location.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			final JFileChooser chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setCurrentDirectory(new File("."));
			
			int returnVal = chooser.showDialog(MainDisplay.this, "Export");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				try {
					DataAccessor.exportDb(selectedFile);
				} catch (Exception e1) {
					System.err.println("Could not export database to " + selectedFile.getName());
					e1.printStackTrace();
					
					JOptionPane.showMessageDialog(getContentPane(), 
												  "Could not export database to this location");
					return;
				}
				JOptionPane.showMessageDialog(getContentPane(), 
											  "Database exported to " + selectedFile.getName() + " successfully");
			}
		}
		
	}
	
	class OpenButtonListener implements ActionListener {
		/**
		 * Brings up a dialog to select the database file
		 * and load products from that database.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//TODO: break this JFileChooser stuff out so it can be reused for the export button.
			final JFileChooser chooser = new JFileChooser();
			
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.setCurrentDirectory(new File("."));
			chooser.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					return f.getName().toLowerCase().endsWith(".db")
							|| f.isDirectory();				
				}
				
				public String getDescription() {
					return "DB files";
				};
			});
			
			int returnVal = chooser.showDialog(MainDisplay.this, "Open");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();
				tree.empty();
				controller.initialiseDB(selectedFile.getName());
				
				JOptionPane.showMessageDialog(getContentPane(), 
											  "Database " + selectedFile.getName() + " opened");
			}
		}
	}
	
	/**
	 * 
	 * @param form
	 */
	public void updateProductForm(Product product) {
		FormBuilder fb = new FormBuilder();
		try {
			if(dynamicForm != null){
				frame.getContentPane().remove(dynamicForm);
			}
			JPanel form = (JPanel) fb.createForm(product);
			dynamicForm = form;
			frame.getContentPane().add(dynamicForm, BorderLayout.CENTER);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//We need both of these
		frame.validate();
		frame.repaint();
	}
	
	public void setClasses(List<Class<?>> classList) {
		tree.addProductStructure(classList);
	}

	public void setProducts(List<Product> products) {
		for (Product p : products){
			tree.addProduct(p);
		}
	}
}
