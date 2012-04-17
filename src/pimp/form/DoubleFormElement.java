package pimp.form;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class DoubleFormElement implements FormElement {
	
	public DoubleFormElement(){};
	
	@Override
	public Double getValue(JComponent jc) {
		return Double.parseDouble(((JTextField)jc).getText());
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
		return double.class;
	}

}
