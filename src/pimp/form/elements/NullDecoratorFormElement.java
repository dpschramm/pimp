package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

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
		if(ndc.isNullSelected()){
			return null;
		} else {
			return insideElement.getValue(ndc.getInputComponent());	
		}
	}

	@Override
	public void setValue(JComponent jc, T o) {
		if(!(jc instanceof NullDecoratorInput)){
			throw new IllegalArgumentException();
		}
		NullDecoratorInput ndc = (NullDecoratorInput)jc;
		if(o == null){
			ndc.setNullSelected(true);
		} else {
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
