package pimp.formelements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

public interface FormElement<T> {
	
	public T getValue(JComponent jc);
	
	public void setValue(JComponent jc, T o);
	
	public JComponent createComponent();
	
	public Type getInputType();

}
