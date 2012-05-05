package pimp.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import pimp.Pimp;
import pimp.form.CompanionForm;
import pimp.form.Form;
import pimp.form.FormBuilder;
import pimp.model.ProductModel;
import pimp.productdefs.Product;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	// Controller
	private Pimp controller;
	
	// Views
	private JFrame frame;
	private ProductTree tree; // TODO make this private, encapsulate.
	private JScrollPane treeScrollPanel;
	
	// A reference to the form builder, we use this to create forms and retrieve objects from forms. 
	private FormBuilder fb;
	private Form dynamicForm; /* Keeping this reference to the dynamic form
								means we can remove it before replacing it with a new one. */
	private CompanionForm cForm;
	
	private Product selectedProduct;
	
	/** 
	 * Constructor
	 */
	public MainDisplay(Pimp controller, ProductModel model) {
		
		// Setup controller.
		this.controller = controller;
		
		// Setup observation of model.
		model.addProductsAddedListener(new productAddedListener());
		model.addProductsDeletedListener(new productDeletedListener());
		model.addProductUpdatedListener(new productUpdatedListener());
		
		// Setup view.
		frame = new JFrame();
		// Exit application when close button clicked.
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Create product tree.
		tree = new ProductTree();
		tree.setPreferredSize(new Dimension(150, 18));
		tree.addClassSelectListener(new classChangedListener());
		tree.addProductSelectedListener(new productSelectedListener());
		tree.addProductEditedListener(new productEditedListener());
		treeScrollPanel = new JScrollPane(tree);
		treeScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		treeScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		// Add panels.
		frame.getContentPane().add(treeScrollPanel, BorderLayout.WEST);
		frame.getContentPane().add(createButtonPanel(), BorderLayout.NORTH);
		
		// Initialise form builder
		fb = new FormBuilder();
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
		btnNew.addActionListener(new ActionListener() {
			@Override()
			public void actionPerformed(ActionEvent e) {
				controller.createNewProduct();	
			}
		});

		// Create Copy Button
		JButton btnCopy = new JButton("Copy");
		btnCopy.addActionListener(new ActionListener() {


			@Override()
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		// Create Open Database Button
		JButton btnOpenProducts = new JButton("Open");
		btnOpenProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.open();

				// Create new copy of product, with different name
				// Send this in an event to the controller's listener.
				
			}
		});
		
		// Create Delete Button
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO getSelectedProduct returns a list -DS
				controller.removeFromCache(tree.getSelectedProduct());
			}
		});
		
		// Create Copy Product Button
		JButton btnCopyProduct = new JButton("Copy");
		btnCopyProduct.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product c = getCurrentProductState();
				controller.createNewProduct(c);
				
			}
		});
		
		// Create Save As Database Button
		JButton btnSaveAsProducts = new JButton("Save As");
		btnSaveAsProducts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.saveAs();
			}
		});
		
		// Add buttons to panels.
		JPanel leftPanel = new JPanel(new FlowLayout());
		leftPanel.add(btnNew);
		leftPanel.add(btnCopyProduct);
		//leftPanel.add(btnSave);
		leftPanel.add(btnDelete);
		
		JPanel rightPanel = new JPanel(new FlowLayout());
		rightPanel.add(btnOpenProducts);
		rightPanel.add(btnSaveAsProducts);
		
		// Add panels to button panel.
		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.add(leftPanel, BorderLayout.WEST);
		buttonPanel.add(rightPanel, BorderLayout.EAST);
	
		return buttonPanel;
	}
		
	/*
	 * This checks whether we are currently used a form or a companion form, 
	 * and returns the object representing the current form state. 
	 * 
	 **/
	public Product getCurrentProductState(){
		Product c;	
		try {
			if(cForm != null){
				c = (Product) cForm.getObject();
			}
			else{
				c = (Product) dynamicForm.getProduct();
			}
		return c;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * This is called by the product tree on valueChange so that when a new tree item is 
	 * selected, any edits made to the previously selected product will be saved. 
	 * The current object state is retrieved by passing the form/companion form through
	 * the form builder.
	 * */	

	
	class classChangedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			controller.getProductsByClass(e.getActionCommand());
		}
	}
	
	class productEditedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Product p = selectedProduct;
				Product c = getCurrentProductState();
				ArrayList<Product> l = new ArrayList<Product>();
				l.add(0, p);
				l.add(1, c);
				controller.updateCacheItem(l);
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	class productSelectedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Update form
			selectedProduct = (Product) e.getSource();
			updateProductForm((Product) e.getSource());
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
			Class<?> companionClass = product.getCompanionFormClass();
			if(companionClass != null){
				Class c = product.getClass();
				Constructor<?> constr = companionClass.getConstructor(c);
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
		// Need to get rid of any companion form classes in the directory.
		tree.addProductStructure(classList);
	}
	
	/* Listeners to observe view. */
	
	class productAddedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Code to update the tree here.
			List<Product> products = (List<Product>) e.getSource();
			for (Product p : products){
				tree.addProduct(p);
			}
		}
	}
	
	class productDeletedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			//Remove from tree;
			List<Product> products = (List<Product>) e.getSource();
			for (Product p : products){
				tree.removeProduct(p);
			}
		}
	}

	class productUpdatedListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent u) {
			tree.updateNode((Product) u.getSource());
		}
	}


	public void empty() {
		tree.empty();
	}
	

}
