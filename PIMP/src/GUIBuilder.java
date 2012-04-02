import java.awt.Color;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GUIBuilder {

	public GUIBuilder() {}

	public JPanel createGUI(Field[] fields){
		
		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(fields.length, 3);
		panel.setLayout(gl);
		
		for(Field f : fields){
			
			JLabel name = new JLabel();
			name.setText(f.getName());
			panel.add(name);
			
			Type t = f.getType();
			
			JLabel type = new JLabel();
			type.setText(t.toString());
			panel.add(type);
			
			if(t.equals(String.class)){
				JTextField jtf = new JTextField();
				panel.add(jtf);
			}
			
			if(t.equals(int.class)){
				JTextField jtf = new JTextField();
				jtf.setText("0");
				panel.add(jtf);
			}
			
			if(t.equals(double.class)){
				JTextField jtb = new JTextField();
				jtb.setText("0.0");
				panel.add(jtb);
			}
			
			if(t.equals(Date.class)){
				JTextField jtb = new JTextField();
				jtb.setText("02 / 02/ 2012");
				panel.add(jtb);
			}
			
			if(t.equals(Color.class)){
				JColorChooser jtb = new JColorChooser();
				//JTextField jtb = new JTextField();
				panel.add(jtb);
			}

		}

		return panel;
		
		
	}
	
	
}
