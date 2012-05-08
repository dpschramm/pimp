package pimp.form.elements;

import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class DateFormElement implements FormElement {
	
	/**
	 * 
	 * TO-D0: Implement this properly when there is a Date JComponent
	 * 
	 */
	
	public DateFormElement(){};
	
	@Override
	public Date getValue(JComponent jc) {
		return ((JDateChooser)jc).getDate();
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((JDateChooser)jc).setDate((Date) o);
	}

	@Override
	public JComponent createComponent() {
		return new JDateChooser();
	}
	
	@Override
	public Type getInputType() {
		return Date.class;
	}

}
