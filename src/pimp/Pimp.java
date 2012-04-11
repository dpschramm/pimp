/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import pimp.gui.MainDisplay;
import pimp.gui.NewProductDialog;
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
	
	public MainDisplay gui; // Why is this static, can we change? -DS
	private List<Product> products;
	private ProductLoader loader;
	private XmlProductLoader xmlLoader;//It would be nice if this was declared as the abstract ProductLoader, 
									   //but that will have to wait until we don't have two ProductLoaders
	
	/** 
	 * Pimp is essentially the Controller
	 * This involves applying appropriate ActionListeners to the given View
	 */
	public Pimp(MainDisplay gui) {
		String dir = "test.xml";
		// Create empty product list.
		//products = new ArrayList<Product>(); //should be initialised in  loadProducts
		loader = new ProductLoader("directory"); //perhaps directory will have to be a cmd argument
		xmlLoader = new XmlProductLoader();
		this.gui = gui;
		gui.setVisible(true);
		gui.addNewProductListener(new newProductListener());
		
		/** TODO add code to automatically load previous product list. */
		loadProducts();
		
		/**
		 * The code below needs to be commented and refactored -DS
		 */
		FormBuilder fb = new FormBuilder(TestClass.class);
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
		//String dir = "/local/test.xml";
		//products = xmlLoader.loadAllProducts(dir);
		
		//Code to manually create a set of products, because
		//xml is acting weird
		
		products = new ArrayList<Product>();
		//create products
		Car honda = new Car();
		honda.setColour("grey");
		honda.setMake("Honda");
		honda.setModel("Civic");
		honda.setYear(1992);
		honda.setName("Ellie's Honda is boring");
		honda.setQuantity(1);
		//save
		products.add(honda);
		
		Car nissan = new Car();
		nissan.setColour("red");
		nissan.setMake("Nissan");
		nissan.setModel("Primera");
		nissan.setYear(1998);
		nissan.setName("Nissan Primera");
		nissan.setQuantity(5);
		products.add(nissan);
			
		Jacket orangeJacket = new Jacket();
		orangeJacket.setBrand("Generic Brand");
		orangeJacket.setColour("orange");
		orangeJacket.setIsWaterproof(true);
		orangeJacket.setName("Orange Jacket");
		orangeJacket.setSize("L");
		orangeJacket.setQuantity(7);
		products.add(orangeJacket);
				
		Jacket purpleJacket = new Jacket();
		purpleJacket.setBrand("Generic Brand");
		purpleJacket.setColour("purple");
		purpleJacket.setIsWaterproof(false);
		purpleJacket.setName("Purple Jacket");
		purpleJacket.setSize("M");
		purpleJacket.setQuantity(9);
		products.add(purpleJacket);
		
		//has public attributes so that field builder will work
		Drink liftPlus = new Drink();
		liftPlus.name = "Lift Plus";
		liftPlus.capacity = "440ml";
		liftPlus.flavour = "Fizzy Lemony Tang";
		liftPlus.quantity = 4;
		liftPlus.setName("Lift Plus");
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
		NewProductDialog npd = null;
		Product newProduct;
		@Override
		public void actionPerformed(ActionEvent e) {
			if(npd == null){
				//The action has been triggered by the main gui
				// create new product dialog.
				List<Class<? extends Product>> cl = loader.getClassList();
				npd = new NewProductDialog(gui, cl);
				//Adds a listener to the 'Create' button on the NewProductDialog to 
				//return the selected list item/class
				npd.addNewProductListener(this); //please work please work
			}
			else{
				//The event has been triggered by the new product dialog. 
				//This means we can go ahead and create the product now.
				//TODO this is terrible we probably shouldn't do this
				Class<? extends Product> c = (Class<? extends Product>) npd.getList().getSelectedValue();
				try {
					newProduct = c.newInstance();
					newProduct.setName("New " + c.getSimpleName());
					products.add(newProduct);
					//refresh product table
					//gui.createProductTable(products);
					gui.addToProductTable(newProduct);
					npd.dispose();
					npd = null;
					//TODO save product to db
				} catch (InstantiationException e1) {
					// This gets fired when abstract classes are instantiated.
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
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
			Class c = selectedObject.getClass();
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
