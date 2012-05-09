package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class DoubleFormElement implements FormElement<Double> {
	
	public DoubleFormElement(){};
	
	@Override
	public Double getValue(JComponent jc) {
		double value = 0.0;
		try{
			value = Double.parseDouble(((JTextField)jc).getText());
		} catch (NumberFormatException nfe){
			// Fall through and do nothing
		}
		return value;
	}

	@Override
	public void setValue(JComponent jc, Double o) {
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
