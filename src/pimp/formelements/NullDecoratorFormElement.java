package pimp.formelements;

import java.awt.Component;
import java.lang.reflect.Type;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class NullDecoratorFormElement implements FormElement {

	FormElement insideElement;
	
	public NullDecoratorFormElement(FormElement fe){
		
		this.insideElement = fe;
	};
	
	@Override
	public Object getValue(JComponent jc) {
		
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
	public void setValue(JComponent jc, Object o) {
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
