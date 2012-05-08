package pimp.formelements;

import javax.swing.JLabel;

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
