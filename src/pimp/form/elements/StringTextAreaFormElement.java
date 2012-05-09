package pimp.form.elements;

import java.awt.Color;
import java.lang.reflect.Type;

import javax.swing.JComponent;
import javax.swing.JTextArea;

public class StringTextAreaFormElement implements FormElement<String> {

	public StringTextAreaFormElement(){};
	
	@Override
	public String getValue(JComponent jc) {
		return ((JTextArea)jc).getText();
	}

	@Override
	public void setValue(JComponent jc, String o) {
		((JTextArea)jc).setText(o.toString());
	}

	@Override
	public JComponent createComponent() {
		JTextArea jta = new JTextArea();
		jta.setBackground(Color.CYAN);
		jta.setForeground(Color.MAGENTA);
		jta.setRows(3);
		return jta;
	}
	
	@Override
	public Type getInputType() {
		return String.class;
	}
}
