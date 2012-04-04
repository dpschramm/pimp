package pimp.gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

/**
 * The main user interface window.
 * 
 * @author Daniel Schramm, Joel Harrison, Ellie Rasmus
 *
 */
public class MainDisplay extends JFrame {
	private JTable productTable;
	public JScrollPane dynamicPanel; // I don't think this should be public, we need getters/setters.
	private JPanel mainPanel;
	
	// New Product Dialog
	private NewProductDialog npd = new NewProductDialog(this);
	
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
		
		// Create table.
		productTable = new JTable();
		productTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null},
				{null, null},
				{null, null},
			},
			new String[] {
				"Type", "Name"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		// Settings
		productTable.getColumnModel().getColumn(0).setResizable(false);
		productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		productTable.getColumnModel().getColumn(0).setMinWidth(100);
		productTable.getColumnModel().getColumn(1).setPreferredWidth(190);
		productTable.getColumnModel().getColumn(1).setMinWidth(190);
		
		// Create table panel
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(12, 66, 300, 450);
		tablePanel.add(productTable);
		
		// Add to main panel.
		mainPanel.add(tablePanel);
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
		JButton btnNewProduct = new JButton("New Product");
		btnNewProduct.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!npd.isVisible()) {
	               npd.setVisible(true);
	            }
			}
		});
		btnNewProduct.setBounds(12, 17, 144, 25);
		
		// Create Load Product Button
		JButton btnLoadProducts = new JButton("Load Products");
		btnLoadProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JOptionPane.showMessageDialog(getContentPane(), 
						"Not yet implemented.");
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

}
