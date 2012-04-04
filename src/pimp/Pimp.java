/**
 * This is the main class for the PIMP program.
 */

package pimp;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.JPanel;

import pimp.gui.MainDisplay;
import pimp.testdefs.TestClass;

public class Pimp {
	
	public static MainDisplay gui;
	
	public static void main(String[] args) {
		gui = new MainDisplay();
		gui.setVisible(true);
		FormBuilder fb = new FormBuilder(TestClass.class);
		@SuppressWarnings("deprecation")
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", new Date(2012, 4, 3), Color.BLUE);
		JPanel newForm;
		try {
			newForm = (JPanel) fb.fillForm(tc1);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			newForm = new JPanel();
			newForm.setBackground(Color.RED);
			newForm.setSize(new Dimension(200, 200));
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			newForm = new JPanel();
			newForm.setBackground(Color.RED);
			newForm.setSize(new Dimension(200, 200));
			//e.printStackTrace();
		}
		setDynamicProductForm(newForm);
		
	}
	
	/**
	 * I don't know where this should happen - probably where ever our controller
	 * ends up being. Too much code in the gui classes could end up being disastrous.
	 * */
	public void setProductList(){
		//display.
	} 
	
	/**
	 * See comment above
	 */
	public static void setDynamicProductForm(JPanel form){
		form.setBounds(0, 0, gui.dynamicPanel.getWidth(), gui.dynamicPanel.getHeight());//display.dynamicPanel.getBounds());
		gui.dynamicPanel.removeAll();
		gui.dynamicPanel.add(form);
		form.setVisible(true);
		gui.validate();
		gui.setVisible(true);
		gui.repaint();
	}
}
