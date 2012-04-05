package pimp.gui;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JButton;

import pimp.Pimp;
import pimp.productdefs.Product;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
/**
 * Provides the user interface for selecting a new product class to create.
 * 
 * This extends JDialog to create a dialog window.
 * 
 * My first step will be to create a new window that pops up whenever users
 * click the new  button.
 * 
 * @author Daniel Schramm
 *
 */
public class NewProductDialog extends JDialog {
	
	private static String title = "Create new product";
	public JButton btnCreate;
	private JList list;
	
	public NewProductDialog(JFrame frame, List<Class <? extends Product>> classList) {
		super(frame, title);
		
		//Because Swing is awful, let's turn this List
		//into an Object[]. It's the logical thing to do.
		Class<? extends Product>[] jListValues = new Class[classList.size()];
		int i = 0;
		for(Class<? extends Product> c: classList){
			jListValues[i] = c;
			i++;
		}
		
		setPreferredSize(new Dimension(300, 400));
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("This is the new product dialog box"));
		
		getContentPane().add(panel);
		
		list = new JList(jListValues);
		list.setPreferredSize(new Dimension(250, 300));
		panel.add(list);
		
		btnCreate = new JButton("Create");

		panel.add(btnCreate);
		pack();
		this.setVisible(true);
	}

	public JList getList(){
		return list;
	}
	
	public void addNewProductListener(ActionListener npl){
		btnCreate.addActionListener(npl);
	}	
}
