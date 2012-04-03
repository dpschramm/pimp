import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FormBuilder {
	
	private JPanel jp;
	private Class c;
	Map<String, JComponent> fieldToComponentMapping;
	
	public FormBuilder(Class c) {
		this.c = c;
		fieldToComponentMapping = new HashMap<String, JComponent>();
		jp = (JPanel)createForm();
	}
	
	private JComponent createForm(){
		
		Field[] fields = c.getFields();

		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(fields.length, 2);
		panel.setLayout(gl);
		
		for(Field f : fields){
			panel.add(createFieldFormNameComponent(f));
			JComponent input = createInputComponent(f);
			fieldToComponentMapping.put(f.getName(), input);
			panel.add(input);
		}

		return panel;
	}
	
	public JComponent getBlankForm(){
		
		return createForm();
	}
	
	public JComponent fillForm(Object o) throws IllegalArgumentException, IllegalAccessException{
		
		if(!o.getClass().equals(c)){
			throw new IllegalArgumentException("Incompatiable object");
		}
		
		for(Field f : o.getClass().getFields()){		
			JComponent input = fieldToComponentMapping.get(f.getName());
			setInputComponent(input , f, o);
		}
		
		return jp;
	}
	
	public JComponent createInputComponent(Field f){
	
		JComponent input = null;
		Type t = f.getType();
		
		if(t.equals(String.class)){
			input = new JTextField();
		}
		
		if(t.equals(int.class)){
			input = new JTextField();
		}
		
		if(t.equals(double.class)){
			input = new JTextField();
		}
		
		if(t.equals(Date.class)){
			input = new JTextField();
		}
		
		if(t.equals(Color.class)){
			//input = new JColorChooser();
			input = new JTextField();
		}
		
		return input;
	}
	
	public JComponent setInputComponent(JComponent input, Field f, Object o) throws IllegalArgumentException, IllegalAccessException{
		
		Type t = f.getType();
		//System.out.println(f.get(o));
		//System.out.println(input);
		
		if(t.equals(String.class)){
			((JTextField) input).setText((String) f.get(o));
		}
		
		if(t.equals(int.class)){
			((JTextField) input).setText(f.get(o).toString());
		}
		
		if(t.equals(double.class)){
			((JTextField) input).setText(f.get(o).toString());
		}
		
		if(t.equals(Date.class)){
			((JTextField) input).setText(f.get(o).toString());
		}
		
		if(t.equals(Color.class)){
			((JTextField) input).setText(f.get(o).toString());
		}
		
		return input;
	}
	
	public JComponent createFieldFormNameComponent(Field f){
		
		JLabel name = new JLabel();
		name.setText(f.getName());
		return name;
	}
	
}
