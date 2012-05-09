package pimp.form.elements;

import javax.swing.JLabel;

/**
 * @author Joel Harrison
 * 
 *         Special JLabel that is used by the unsupportedTypeFormElement, is a
 *         JLabel that also hold a object.
 * 
 */
public class UnsupportedTypeLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	Object object;
	
	public UnsupportedTypeLabel(Object object) {
		super();
		this.object = object;
	}

	public UnsupportedTypeLabel() {
		super();
		this.object = null;
		this.setText("[Unsupported Type]");
	}

	public Object getObject(){
		return object;
	}
	
	public void setObject(Object object){
		this.object = object;
	}
}
