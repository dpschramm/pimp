package pimp.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class NullDecoratorInput extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	JComponent input;
	JCheckBox nullCheckBox;
	
	public NullDecoratorInput(JComponent input) {
		super();
		this.input = input;
		nullCheckBox = new JCheckBox();
		nullCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				enableInputField(nullCheckBox.isSelected());
			}
		    });		
	}

	public JComponent getInputComponent(){
		return input;
	}
	
	public boolean isNullSelected(){
		return nullCheckBox.isSelected();
	}
	
	public void setNullSelected(boolean isNull){
		nullCheckBox.setSelected(true);
	}
	
	protected void enableInputField(boolean enable) {
		input.setEnabled(enable);
	}
	
}
