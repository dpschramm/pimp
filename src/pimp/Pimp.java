/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pimp.gui.MainDisplay;
import pimp.gui.SelectProductDialog;
import pimp.productdefs.Car;
import pimp.productdefs.Drink;
import pimp.productdefs.Jacket;
import pimp.productdefs.Product;
import pimp.testdefs.TestClass;
import pimp.persistence.*;

/**
 * The Pimp class acts as the controller for our inventory management program.
 */
public class Pimp {
	
	// Database stuff
	private String databaseDir = "test.xml";
	
	// Product classes.
	private DirectoryClassLoader pcf;
	private String productPackage = "pimp.productdefs";
	private String productDir = "products1"; /* Not sure what format this should take
												may need to be a cmd argument. */ 
	
	private MainDisplay gui;
	private List<Product> products;
	
	/** 
	 * Pimp is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public Pimp(MainDisplay gui) {
		
		// Could change this to be a static singleton, like the DataAccessor -DS.
		pcf = new DirectoryClassLoader(productDir, productPackage);

		this.gui = gui;
		gui.setVisible(true);
		gui.addNewProductListener(new newProductListener());
		
		/** TODO add code to automatically load previous product list. */
		loadProducts();
		
		/**
		 * The code below needs to be commented and refactored -DS
		 */
		FormBuilder fb = new FormBuilder(TestClass.class);
		fb.addFormElement(new StringFormElement());
		fb.addFormElement(new DoubleFormElement());
		fb.addFormElement(new IntFormElement());
		fb.addFormElement(new DateFormElement());
		fb.addFormElement(new ColorFormElement());
		fb.createForm();

		@SuppressWarnings("deprecation")
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", new Date(2012, 4, 3), Color.BLUE);
		JPanel newForm;
		try {
			newForm = (JPanel) fb.fillForm(tc1);
			setDynamicProductForm(newForm);
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {}
		
		
	}

	public void loadProducts(){	
		// Load products from databaseDir.
		DataAccessorMock.initialize(databaseDir);
		products = DataAccessorMock.load();
		
		//has public attributes so that field builder will work
		Drink liftPlus = new Drink();
		liftPlus.name = "Lift Plus";
		liftPlus.capacity = "440ml";
		liftPlus.flavour = "Fizzy Lemony Tang";
		liftPlus.quantity = 4;
		liftPlus.name  = "Lift Plus";
		products.add(liftPlus);
		
		//do stuff to init list in gui
		gui.createProductTable(products);
		gui.addTreeSelectionListener(new productTreeListener());
	}
	
	public void setDynamicProductForm(JPanel form){
		//Where should this kind of logic be? Oh this is terribly confusing.
		form.setBounds(0, 0, gui.dynamicPanel.getWidth(), gui.dynamicPanel.getHeight());
		gui.dynamicPanel.removeAll();
		gui.dynamicPanel.add(form);
		form.setVisible(true);
		gui.validate();
		gui.setVisible(true);
		gui.repaint();
	}
	
	/**
	 * This ActionListener is applied to the New button on the main gui. 
	 * When clicked it needs to launch a NewProductDialog, retrieve the input
	 * from that and create a product of the returned type
	 */
	class newProductListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// Create and show product dialog.
			SelectProductDialog selectDialog = new SelectProductDialog(gui, 
					pcf.getClassList());
			
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
		public void valueChanged(TreeSelectionEvent arg0) {
			TreePath path = gui.productTree.getSelectionPath();
			DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)
                    path.getLastPathComponent();
			Object selectedObject = selectedNode.getUserObject();
			Class<?> c = selectedObject.getClass();
			
			//Checking that selected class isn't abstract and isn't just a String
			//(the "Product" root node is currently a string.
			if(!Modifier.isAbstract(c.getModifiers()) && c != "".getClass()){
				try {
					Product selectedProduct = (Product)selectedObject;
					FormBuilder fb = new FormBuilder(selectedProduct.getClass());
					JPanel newForm = (JPanel) fb.fillForm(selectedProduct);
					setDynamicProductForm(newForm);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		}
	}
}
