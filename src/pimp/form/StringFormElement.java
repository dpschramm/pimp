package pimp.form;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class StringFormElement implements FormElement {

	public StringFormElement(){};
	
	@Override
	public String getValue(JComponent jc) {
		String value = ((JTextField)jc).getText();
		if(value.equals("null")){
			value = null;
		}
		return value;
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		// A way of dealing with null is the string is null since you cannot set
		// a JTextField to null
		if(o == null){
			o = "null";
		}
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
