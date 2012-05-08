package pimp.form.elements;

import java.lang.reflect.Type;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class UnsupportedTypeFormElement implements FormElement {
	
	public UnsupportedTypeFormElement(){};
	
	@Override
	public Object getValue(JComponent jc) {
		return ((UnsupportedTypeLabel)jc).getObject();
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((UnsupportedTypeLabel)jc).setObject(o);
		((UnsupportedTypeLabel)jc).setText(o.toString());
	}

	@Override
	public JComponent createComponent() {
		return new UnsupportedTypeLabel();
	}
	
	@Override
	public Type getInputType() {
		return null;
	}

}
