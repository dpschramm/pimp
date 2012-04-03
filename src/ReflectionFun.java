import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.System.out;

public class ReflectionFun {

	/**
	 * test commit with eclipse
	 * 
	 * @param
	 *  args
	 */
	
	public static MainDisplay display;
	public static void main(String[] args) {

		/***
		List<String> classes = null;
		try {
			classes = read("src/productdefs.txt");
			// System.out.println(classes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		***/
		
		//Ellie's test code ------------------------------------------------------------------------
		display = new MainDisplay();
		display.setVisible(true);
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
		
		
		//Joel's test code ------------------------------------------------------------------------
		/*JFrame mainInterface = new JFrame();
		FormBuilder fb = new FormBuilder(TestClass.class);

		@SuppressWarnings("deprecation")
		TestClass tc1 = new TestClass(10, 12.0, "PIMP", new Date(2012, 4, 3), Color.BLUE);
		try {
			JPanel showObjectArea = (JPanel) fb.fillForm(tc1);
			for(Component jc : showObjectArea.getComponents()){
				if(jc instanceof JTextField){
					System.out.println(jc.toString());
					System.out.println(((JTextField) jc).getText());
				}
					
			}
			mainInterface.add(showObjectArea);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		mainInterface.pack();
		mainInterface.setVisible(true);*/
	}
	
	/*
	 * I don't know where this should happen - probably where ever our controller
	 * ends up being. Too much code in the gui classes could end up being disastrous.
	 * */
	public void setProductList(){
		//display.
	} 
	
	/* See comment above
	 * */
	public static void setDynamicProductForm(JPanel form){
		form.setBounds(0, 0, display.dynamicPanel.getWidth(), display.dynamicPanel.getHeight());//display.dynamicPanel.getBounds());
		display.dynamicPanel.removeAll();
		display.dynamicPanel.add(form);
		form.setVisible(true);
		display.validate();
		display.setVisible(true);
		display.repaint();
	}
	
}
