package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

/**
 * @author Joel Harrison
 * 
 *         Form Element is a wrapper around a type and a input component for
 *         that type. Form Elements are used by the form builder for when
 *         creating form, also used by the forms for getting nd setting a
 *         product for the form.
 * 
 * @param <T>
 */
public interface FormElement<T> {
	
	/**
	 * Given a input component will get the value from it.
	 * 
	 * @param jc the input component
	 * @return the value represented in the input component
	 */
	public T getValue(JComponent jc);
	
	/**
	 * Given a input component and a value will set the input component to represent the value given.
	 * 
	 * @param jc the input component
	 * @param o the value the input component should represent
	 */
	public void setValue(JComponent jc, T o);
	
	/**
	 * Creates a input component used for this form element
	 * 
	 * @return input component
	 */
	public JComponent createComponent();
	
	/**
	 * @return the type this form element represents
	 */
	public Type getInputType();

}
