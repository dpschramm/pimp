package pimp.form.elements;

import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;

import com.toedter.calendar.JDateChooser;

/**
 * Form element for a Date
 * 
 * @author Joel Harrison, Joel Mason
 *
 */
public class DateFormElement implements FormElement<Date> {
	
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
