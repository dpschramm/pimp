package pimp.form;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class StringFormElement implements FormElement {

	public StringFormElement(){};
	
	@Override
	public String getValue(JComponent jc) {
		return ((JTextField)jc).getText();
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
		return String.class;
	}
}
