package pimp;

import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class DateFormElement implements FormElement {
	
	/**
	 * 
	 * TO-D0: Implement this properly when there is a Date JComponent
	 * 
	 */
	
	public DateFormElement(){};
	
	@Override
	public Date getValue(JComponent jc) {
		return new Date();
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
		return Date.class;
	}

}
