package pimp.form.elements;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Joel Harrison
 * 
 *         Input component used for the null form element decorator, consists of
 *         an input component and a check box
 * 
 */
public class NullDecoratorInput extends JPanel {

	private static final long serialVersionUID = 1L;

	JComponent input;
	JCheckBox nullCheckBox;

	public NullDecoratorInput(JComponent input) {
		super();
		this.input = input;
		// Create Check box, and add listner to disable or enable the input
		// component based on the null checkbox
		nullCheckBox = new JCheckBox("Null");
		nullCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				enableInputField(!nullCheckBox.isSelected());
			}
		});
		input.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				enableInputField(true);
				setNullSelected(false);

			}
		});
		this.setLayout(new BorderLayout());
		this.add(input, BorderLayout.CENTER);
		this.add(nullCheckBox, BorderLayout.EAST);
	}

	public JComponent getInputComponent() {
		return input;
	}

	public boolean isNullSelected() {
		return nullCheckBox.isSelected();
	}

	public void setNullSelected(boolean isNull) {
		nullCheckBox.setSelected(isNull);
	}

	protected void enableInputField(boolean enable) {
		input.setEnabled(enable);
	}

}
