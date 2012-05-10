package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

/**
 * @author Joel Harrison
 * 
 *         Is a decorator for form elements so they are able to be null. Adds a
 *         null check box alongside the input component.
 * 
 * @param <T>
 */
public class NullDecoratorFormElement<T> implements FormElement<T> {

	FormElement<T> insideElement;
	
	public NullDecoratorFormElement(FormElement<T> fe){
		
		this.insideElement = fe;
	};
	
	@Override
	public T getValue(JComponent jc) {
		
		if(!(jc instanceof NullDecoratorInput)){
			throw new IllegalArgumentException();
		}
		NullDecoratorInput ndc = (NullDecoratorInput)jc;
		// Check the null check box
		if(ndc.isNullSelected()){
			// is checked so return null
			return null;
		} else {
			// not checked so return the value
			return insideElement.getValue(ndc.getInputComponent());	
		}
	}

	@Override
	public void setValue(JComponent jc, T o) {
		if(!(jc instanceof NullDecoratorInput)){
			throw new IllegalArgumentException();
		}
		NullDecoratorInput ndc = (NullDecoratorInput)jc;
		// Check is value to set is null
		if(o == null){
			// Is null so check checkbox
			ndc.setNullSelected(true);
		} else {
			// Not null so uncheck box and set input value
			ndc.setNullSelected(false);
			insideElement.setValue(ndc.getInputComponent(), o);	
		}
	}

	@Override
	public JComponent createComponent() {
		return new NullDecoratorInput(insideElement.createComponent());
	}
	
	@Override
	public Type getInputType() {
		return insideElement.getInputType();
	}
}
