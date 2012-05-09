package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * @author Joel Harrison
 * 
 *         Form Element for a string
 * 
 */
public class StringFormElement implements FormElement<String> {

	public StringFormElement() {
	};

	@Override
	public String getValue(JComponent jc) {
		return ((JTextField) jc).getText();
	}

	@Override
	public void setValue(JComponent jc, String o) {
		((JTextField) jc).setText(o.toString());
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
