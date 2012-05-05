package pimp.gui;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;

import pimp.Pimp;
import pimp.form.CompanionForm;
import pimp.form.Form;
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
	private JFrame frame;
	
	// Models.
	private ProductTree tree;
	private List<Product> products;
	
	// Controller.
	private Pimp controller;
	
	// A reference to the form builder, we use this to create forms and retrieve objects from forms. 
	private FormBuilder fb;
	private Form dynamicForm; /* Keeping this reference to the dynamic form
								means we can remove it before replacing it with a new one. */
	private CompanionForm cForm;
	
	private Product currentProduct;
	
	private JScrollPane treeScrollPanel;
	
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
		
		// Init. form builder
		fb = new FormBuilder();
		
		// Create product tree.
		tree = new ProductTree();
		tree.setPreferredSize(new Dimension(150, 18));
		tree.addClassSelectListener(new classChangedListener());
		treeScrollPanel = new JScrollPane(tree);
		treeScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		treeScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		
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
	 * When clicked it needs to launch a NewProductDialog, retrieve the input
	 * from that and create a product of the returned type
	 */
	class newProductListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Get selected class (will be null if they clicked cancel).

			controller.createNewProduct();
		}
	}
	
	/*
	 * This is called by the product tree on valueChange so that when a new tree item is 
	 * selected, any edits made to the previously selected product will be saved. 
	 * The current object state is retrieved by passing the form/companion form through
	 * the form builder.
	 * */	
	public Product saveCurrentChanges(){
		try {
			/*Object currentProductState = null;
			if(dynamicForm != null){
				currentProductState = fb.getProductFromForm(dynamicForm);
			}
			else if(cForm != null){
				currentProductState = cForm.getObject();
			}
			//This check shouldn't be necessary but whatever
			if(currentProductState instanceof Product){
				//controller.saveChangesToProduct((Product)currentProductState);
			}
			return (Product) currentProductState;*/
			Object currentFormState = fb.getProductFromForm(dynamicForm);
			//asdf The current form state needs to be copied over to the current product here
			//there is no easy way to do this. There should be. 
			

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * Delete the specified product.
	 * 
	 * @param p
	 */
	class deleteButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ArrayList<Product> products = tree.getSelectedProduct();
			//Fire the list up to the controller
			controller.remove(products);
		}
	}	
	
	class classChangedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getProductsByClass(e.getActionCommand());
		}		
	}
	
	class copyButtonListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(getContentPane(), 
			"Not yet implemented.");
		}	
	}
	
	/**
	 * Creates the file chooser used for exporting and opening, and asks
	 * the user what file to open.
	 * 
	 * @return the selected file.
	 */
	private File getFile(String windowText) {
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setCurrentDirectory(new File("."));
		
		// Filter out non database files.
		chooser.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".db") 
					|| f.isDirectory();
			}
			
			public String getDescription() {
				return "DB files";
			};
		});
		
		// Show dialog.
		int result = chooser.showDialog(MainDisplay.this, windowText);
		if (result == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		}
		
		return null;
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
			File file = getFile("Export");
			if (file != null) {
				if (controller.exportDatabase(file)) {
					JOptionPane.showMessageDialog(getContentPane(), 
							"Database exported to " + file.getName() + " successfully");
				}
				else {
					JOptionPane.showMessageDialog(getContentPane(), 
						"Could not export database to this location");
				}
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
			File file = getFile("Open");
			if (file != null) {
				tree.empty();
				controller.initialiseDB(file.getName());
				JOptionPane.showMessageDialog(getContentPane(), 
						"Database " + file.getName() + " opened");
			}
		}
	}
	
	/**
	 * 
	 * @param form
	 */
	public void updateProductForm(Product product) {
		try {
			if(dynamicForm != null){
				frame.getContentPane().remove(dynamicForm);
			}
			else if(cForm != null){
				frame.getContentPane().remove((JPanel)cForm.getForm());
			}
			currentProduct = product;
			Class<?> companionClass = product.getCompanionFormClass();
			if(companionClass != null){
				Constructor<?> constr = companionClass.getConstructor(product.getClass());
				cForm = (CompanionForm) constr.newInstance(product);
				frame.getContentPane().add(cForm.getForm(), BorderLayout.CENTER);
				dynamicForm = null;
			}
			else{
				dynamicForm = fb.createForm(product);
				frame.getContentPane().add(dynamicForm, BorderLayout.CENTER);
				cForm = null;
			}

			//dynamicForm = form;
			//frame.getContentPane().add(dynamicForm, BorderLayout.CENTER);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
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

	public void removeProduct(List<Product> products){
		for (Product p : products){
		tree.removeProduct(p);
		}
	}
}
