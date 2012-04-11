package pimp;

import javax.swing.JComponent;

public interface CompanionForm {
	
	JComponent getForm();
	
	Object getObject();
	
	void updateForm(Object o);

}
