package pimp.gui;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	
	
	public NewProductDialog(JFrame frame) {
		super(frame, title);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel("This is the new product dialog box"));
		
		add(panel);
		pack();
	}
	
	public void showDialog() {
		setVisible(true);
	}
	
}
