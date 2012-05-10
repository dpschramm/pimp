package pimp.form.elements;

import java.lang.reflect.Type;

import javax.swing.JComponent;

/**
 * @author Joel Harrison
 * 
 *         Special form element that is used when wanting to show values of a
 *         product but the form builder does not have a form element for. Will
 *         show a uneditable JLabel but will also hold the object that it is
 *         representing so are able to decode the form back to the orginal
 *         product.
 * 
 */
public class UnsupportedTypeFormElement implements FormElement<Object> {

	public UnsupportedTypeFormElement() {
	};

	@Override
	public Object getValue(JComponent jc) {
		return ((UnsupportedTypeLabel) jc).getObject();
	}

	@Override
	public void setValue(JComponent jc, Object o) {
		((UnsupportedTypeLabel) jc).setObject(o);
		((UnsupportedTypeLabel) jc).setText(o.toString());
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
