package pimp.form.elements;

import java.awt.Color;
import java.lang.reflect.Type;

import javax.swing.JComponent;


/**
 * Form element for a Color
 * 
 * @author Joel Harrison, Joel Mason
 *
 */
public class ColorFormElement implements FormElement<Color> {
		
	public ColorFormElement(){};
	
	@Override
	public Color getValue(JComponent jc) {
		return ((ColorButton)jc).getBackground();
	}
	
	@Override
	public void setValue(JComponent jc, Color o) {
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
