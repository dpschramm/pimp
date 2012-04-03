import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;


public class MainDisplay extends JFrame {
	private JTable productTable;
	public JScrollPane dynamicPanel;
	
	//Needs to be passed list of objects from database
	public MainDisplay() {
		setResizable(false);
		initComponents();		
	}
	
	void initComponents(){
		this.setBounds(new Rectangle(0, 0, 800, 550));
		getContentPane().setBounds(new Rectangle(0, 0, 800, 550));
		getContentPane().setLayout(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 786, 516);
		getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JPanel tablePanel = new JPanel();
		tablePanel.setBounds(12, 66, 300, 450);
		mainPanel.add(tablePanel);
		
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
		productTable.getColumnModel().getColumn(0).setResizable(false);
		productTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		productTable.getColumnModel().getColumn(0).setMinWidth(100);
		productTable.getColumnModel().getColumn(1).setPreferredWidth(190);
		productTable.getColumnModel().getColumn(1).setMinWidth(190);
		tablePanel.add(productTable);
		
		JButton btnNewProduct = new JButton("New Product");
		btnNewProduct.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnNewProduct.setBounds(12, 17, 144, 25);
		mainPanel.add(btnNewProduct);
		
		JButton btnLoadProducts = new JButton("Load Products");
		btnLoadProducts.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnLoadProducts.setBounds(168, 17, 144, 25);
		mainPanel.add(btnLoadProducts);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnEdit.setBounds(363, 17, 117, 25);
		mainPanel.add(btnEdit);
		
		JButton btnCopy = new JButton("Copy");
		btnCopy.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnCopy.setBounds(492, 17, 117, 25);
		mainPanel.add(btnCopy);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnDelete.setBounds(621, 17, 117, 25);
		mainPanel.add(btnDelete);
		
		dynamicPanel = new JScrollPane();
		dynamicPanel.setBounds(343, 66, 416, 450);
		mainPanel.add(dynamicPanel);
	}
	
	void populateProductTable(){
		
	}
}