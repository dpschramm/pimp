/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

// Form
import pimp.form.ColorFormElement;
import pimp.form.DateFormElement;
import pimp.form.DoubleFormElement;
import pimp.form.FormBuilder;
import pimp.form.IntFormElement;
import pimp.form.StringFormElement;

// Gui
import pimp.gui.MainDisplay;
import pimp.gui.NodeItem;
import pimp.gui.SelectProductDialog;

// Other
import pimp.persistence.DataAccessor;
import pimp.persistence.DataAccessorMock;
import pimp.persistence.DataAccessorNew;
import pimp.persistence.ProductLoader;
import pimp.persistence.ProductSaver;
import pimp.persistence.XmlProductLoader;
import pimp.persistence.XmlProductSaver;
import pimp.productdefs.Car;
import pimp.productdefs.Drink;
import pimp.productdefs.Jacket;
import pimp.productdefs.Jeans;
import pimp.productdefs.Product;
import pimp.testdefs.TestClass;


/**
 * The Pimp class acts as the controller for our inventory management program.
 * 
 * @author Daniel Schramm, Ellie Rasmus
 */
public class Pimp {
	
	// Database stuff
	private String databaseDir = "products.xml";
	
	// Product classes.
	private DirectoryClassLoader dcl;
	private String productPackage = "pimp.productdefs";
	private String productDir = "products"; /* Not sure what format this should take
												may need to be a cmd argument. */ 
	
	private MainDisplay gui;
	private List<Product> products;
	
	/**
	 * Main method just creates a new Pimp object.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new Pimp();
	}
	
	/** 
	 * Pimp is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public Pimp() {
		
		// Load class definitions.
		dcl = new DirectoryClassLoader(productDir, productPackage);
		
		// Initialize Gui
		gui = new MainDisplay();
		gui.addNewProductListener(new newProductListener());
		gui.addTreeSelectionListener(new productTreeListener());
		
		ProductLoader loader = new XmlProductLoader(databaseDir);
		ProductSaver saver = new XmlProductSaver(databaseDir);
		DataAccessor.initialise(loader, saver);
		
		// Load extisting products.
		loadProducts();
		
		// Make form.
		createForm();

		gui.display();
	}

	private void loadProducts(){	
		// Load products from databaseDir.
					
		products = DataAccessor.load();
		
		//do stuff to init list in gui
		gui.addProductStructure(dcl.getClassList());
		gui.addToProductTable(products);
	}
	
	private void createForm() {
		// A dummy form.
		FormBuilder fb = new FormBuilder(TestClass.class);
		fb.addFormElement(new StringFormElement());
		fb.addFormElement(new DoubleFormElement());
		fb.addFormElement(new IntFormElement());
		fb.addFormElement(new DateFormElement());
		fb.addFormElement(new ColorFormElement());
		fb.createForm();
		
		// Fill the form.
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", Date.valueOf("2012-04-03"), Color.BLUE);
		JPanel form = null;
		try {
			form = (JPanel) fb.fillForm(tc1);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Update the form displayed by the GUI.
		if (form != null) gui.updateProductForm(form);
	}
	
	/**
	 * This ActionListener is applied to the New button on the main gui. 
	 * When clicked it needs to launch a NewProductDialog, retriegui.updateProductForm(form);ve the input
	 * from that and create a product of the returned type
	 */
	class newProductListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// Create and show product dialog.
			SelectProductDialog selectDialog = new SelectProductDialog(gui, 
					dcl.getClassList());
			
			// Get selected class (will be null if they clicked cancel).
			Class<? extends Product> c = selectDialog.getSelectedClass();
			
			try {
				// Check to make sure user made a selection.
				if (c != null) {
					Product p = c.newInstance();
					products.add(p);
					gui.addToProductTable(p);
					
					// Debug.
					System.out.println("You selected to create a " + p.getClass().getName());
				}
				else System.out.println("No selection.");
				
				//TODO Other things that will need to happen here
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/** 
	 * Save products to file.
	 */
	class saveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**
	 * Load products from file.
	 */
	class loadButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	class productTreeListener implements TreeSelectionListener{

		/* This is triggered when a product is selected in the product tree.
		 * It retrieves the selected object and creates a new dynamic form to
		 * display the product's attributes.
		 * 
		 * Currently these product forms will only work with public class attributes
		 */
		@Override
		public void valueChanged(TreeSelectionEvent event) {
			TreePath path = event.getNewLeadSelectionPath();
			NodeItem selectedNode = (NodeItem) path.getLastPathComponent();
			System.out.println(selectedNode.getID());
			Object selectedObject = selectedNode.getUserObject();
			Class<?> c = selectedObject.getClass();
			
			//Checking that selected class isn't abstract and isn't just a String
			//(the "Product" root node is currently a string.
			if(!Modifier.isAbstract(c.getModifiers()) && c != "".getClass()){
				try {
					Product selectedProduct = (Product)selectedObject;
					FormBuilder fb = new FormBuilder(selectedProduct.getClass());
					fb.addFormElement(new StringFormElement());
					fb.addFormElement(new DoubleFormElement());
					fb.addFormElement(new IntFormElement());
					fb.addFormElement(new DateFormElement());
					fb.addFormElement(new ColorFormElement());
					fb.createForm();
					JPanel newForm = (JPanel) fb.fillForm(selectedProduct);
					if(newForm != null){
						gui.updateProductForm(newForm);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
	}
}
