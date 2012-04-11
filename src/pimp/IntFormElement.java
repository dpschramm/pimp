package pimp;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class IntFormElement implements FormElement {
	
	public IntFormElement(){};

	@Override
	public Integer getValue(JComponent jc) {
		return Integer.parseInt(((JTextField)jc).getText());
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
