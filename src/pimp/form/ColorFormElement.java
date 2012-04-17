package pimp.form;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class ColorFormElement implements FormElement {
		
	public ColorFormElement(){};
	
	@Override
	public Color getValue(JComponent jc) {
		return ((JColorChooser)jc).getColor();
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((JColorChooser)jc).setColor((Color)o);
	}

	@Override
	public JComponent createComponent() {
		return new JColorChooser();
	}
	
	@Override
	public Type getInputType() {
		return Color.class;
	}

}
