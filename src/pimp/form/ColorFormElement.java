package pimp.form;

import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;

import pimp.gui.ColorButton;

public class ColorFormElement implements FormElement {
		
	public ColorFormElement(){};
	
	@Override
	public Color getValue(JComponent jc) {
		return ((ColorButton)jc).getBackground();
	}
	
	@Override
	public void setValue(JComponent jc, Object o) {
		((ColorButton)jc).setBackground((Color)o);
	}
	
	@Override
	public JComponent createComponent() {
		return new ColorButton(null);
	}
	
	@Override
	public Type getInputType() {
		return Color.class;
	}
	
}
