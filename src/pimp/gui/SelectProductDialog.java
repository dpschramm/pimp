package pimp.gui;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JButton;

import pimp.model.Product;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * This class provides a dialog window that allows users to select a product
 * class.
 * 
 * @author Daniel Schramm
 *
 */
public class SelectProductDialog {
	
	// Instance variables.
	private JDialog dialog;
	private JList list;
	private Map<String, Class<?>> classMap;
	
	/**
	 * Constructor to create the formatted dialog box.
	 * 
	 * @param frame the parent frame.
	 * @param classArray an array of product classes.
	 */
	public SelectProductDialog(JFrame frame, 
			List<Class<?>> classList) {
		// Create dialog.
		dialog = new JDialog(frame, "Create new product", true);
		
		// Build a map of class name to classes.
		classMap = new HashMap<String, Class<?>>();
		for (Class<?> c : classList) {
			// Ensure we don't give the option of creating abstract classes.
			if(!Modifier.isAbstract(c.getModifiers())){
				// Strip the package name.
				String name = c.toString();
				name = name.substring(name.lastIndexOf('.') + 1);
				// Add to map.
				classMap.put(name, c);
			}
		}
		
		// Create JList
		list = new JList(classMap.keySet().toArray());
		
		// Add content to dialog
		dialog.getContentPane().add(createPanel());
		
		// Adjust dialog settings.
		dialog.setPreferredSize(new Dimension(300, 400));
		dialog.setLocationRelativeTo(frame);
		dialog.pack();
		
		// Show the dialog.
		dialog.setVisible(true);
	}
	
	/**
	 * Create dialog content based on the array of Product classes that users 
	 * can select from.
	 * 
	 * @param classArray an array of product classes. 
	 * @return the content panel of the dialog.
	 */
	private JPanel createPanel() {
		// Main panel.
		JPanel panel = new JPanel();
		panel.add(new JLabel("This is the new product dialog box"));
		
		// Add list.
		list.setPreferredSize(new Dimension(250, 300));
		panel.add(list);
		
		// Create button.
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		panel.add(btnCreate);
		
		// Cancel button.
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.clearSelection();
				dialog.setVisible(false);
			}
		});
		panel.add(btnCancel);
		
		// Make button the default.
		dialog.getRootPane().setDefaultButton(btnCreate);
		
		return panel;
	}
	
	/**
	 * Returns the class selected by the user, or null if no class was
	 * selected (i.e. the user clicked cancel).
	 * 
	 * @return the selected class.
	 */
	public Class<? extends Product> getSelectedClass() {
		
		Object selection = list.getSelectedValue();
		
		if (selection == null) {
			return null;
		}
		
		Class<?> c = classMap.get(selection);
		
		return (Class<? extends Product>) c;
	}
}
