package pimp.formelements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

public class UnsupportedTypeFormElement implements FormElement<Object> {
	
	public UnsupportedTypeFormElement(){};
	
	@Override
	public Object getValue(JComponent jc) {
		return ((UnsupportedTypeLabel)jc).getObject();
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((UnsupportedTypeLabel)jc).setObject(o);
		((UnsupportedTypeLabel)jc).setText(o.toString());
	}

	@Override
	public JComponent createComponent() {
		return new UnsupportedTypeLabel();
	}
	
	@Override
	public Type getInputType() {
		return null;
	}

}
