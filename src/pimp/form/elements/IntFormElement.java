package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class IntFormElement implements FormElement {
	
	public IntFormElement(){};

	@Override
	public Integer getValue(JComponent jc) {
		int value = 0;
		try{
			value = Integer.parseInt(((JTextField)jc).getText());	
		} catch (NumberFormatException nfe){
			// Fall through and return 0.
		}
		return value;
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((JTextField)jc).setText(o.toString());
	}

	@Override
	public JComponent createComponent() {
		return new JTextField();
	}
	
	@Override
	public Type getInputType() {
		return int.class;
	}

}
