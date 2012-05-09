package pimp.formelements;

import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;

import com.toedter.calendar.JDateChooser;

public class DateFormElement implements FormElement<Date> {
	
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
	public void setValue(JComponent jc, Date o) {
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
