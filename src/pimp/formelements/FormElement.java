package pimp.formelements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

public interface FormElement {
	
	public Object getValue(JComponent jc);
	
	public void setValue(JComponent jc, Object o);
	
	public JComponent createComponent();
	
	public Type getInputType();

}
