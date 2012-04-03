package pimp;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FormBuilder {

	private JPanel jp;
	private Class c;
	Map<String, JComponent> fieldToComponentMapping;

	/**
	 * Constructor takes a Class for which it can build forms for
	 * 
	 * @param c
	 */
	public FormBuilder(Class c) {
		this.c = c;
		fieldToComponentMapping = new HashMap<String, JComponent>();
		jp = (JPanel) createForm();
	}

	/**
	 * Creates the form based on the Class c
	 * 
	 * @return a JPanel form that represents the class
	 */
	private JComponent createForm() {

		Field[] fields = c.getFields();

		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(fields.length, 2);
		panel.setLayout(gl);

		for (Field f : fields) {
			panel.add(createFieldFormNameComponent(f));
			JComponent input = createInputComponent(f);
			fieldToComponentMapping.put(f.getName(), input);
			panel.add(input);
		}

		return panel;
	}

	/**
	 * Clears the current form
	 * 
	 * @return a cleared form
	 */
	public JComponent getBlankForm() {

		return createForm();
	}

	/**
	 * Will fill the formBuilder form based on the object given and return the
	 * form
	 * 
	 * @param o
	 *            the object to fill the form with
	 * @return the form filled with the objects field values
	 * 
	 * @throws IllegalArgumentException
	 *             if the given object is not the same type as the class the
	 *             form builder was created with
	 * @throws IllegalAccessException
	 */
	public JComponent fillForm(Object o) throws IllegalArgumentException,
			IllegalAccessException {

		if (!o.getClass().equals(c)) {
			throw new IllegalArgumentException("Incompatiable object");
		}

		for (Field f : o.getClass().getFields()) {
			JComponent input = fieldToComponentMapping.get(f.getName());
			setInputComponent(input, f, o);
		}

		return jp;
	}

	/**
	 * Will update the given object o so that its values correspond to the
	 * values in the form
	 * 
	 * @param o
	 *            the object to update
	 * @throws IllegalArgumentException
	 *             if the given object is not the same type as the class the
	 *             form builder was created with
	 * @throws IllegalAccessException
	 */
	public void updateObjectBasedFromForm(Object o)
			throws IllegalArgumentException, IllegalAccessException {

		if (!o.getClass().equals(c)) {
			throw new IllegalArgumentException("Incompatiable object");
		}

		for (Field f : o.getClass().getFields()) {
			JComponent input = fieldToComponentMapping.get(f.getName());
			f.set(o, getInputComponentValue(input, f));
		}
	}

	/**
	 * Will create and return an object that corresponds to the values in the
	 * form
	 * 
	 * @return an object that will represent the current state of the form
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Object getObjectFromForm() throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		Object o;
		o = c.newInstance();

		for (Field f : o.getClass().getFields()) {
			JComponent input = fieldToComponentMapping.get(f.getName());
			f.set(o, getInputComponentValue(input, f));
		}

		return o;
	}

	/**
	 * Called when creating the input field, is used to determine what kind of
	 * Component is used for the classes given field type
	 * 
	 * @param f
	 *            the field for which the Component is needed
	 * @return input component that will be used for the given field
	 */
	public JComponent createInputComponent(Field f) {

		JComponent input = null;
		Type t = f.getType();

		if (t.equals(String.class)) {
			input = new JTextField();
		}

		if (t.equals(int.class)) {
			input = new JTextField();
		}

		if (t.equals(double.class)) {
			input = new JTextField();
		}

		if (t.equals(Date.class)) {
			input = new JTextField();
		}

		if (t.equals(Color.class)) {
			// input = new JColorChooser();
			input = new JTextField();
		}

		return input;
	}

	/**
	 * Used for updating the form based on an object. Will set the given input
	 * field to be consistent with the given field in the given object
	 * 
	 * @param input
	 *            the input component on the form to update
	 * @param f
	 *            the field of the object
	 * @param o
	 *            the object to update the form with
	 * @return the updated input component
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public JComponent setInputComponent(JComponent input, Field f, Object o)
			throws IllegalArgumentException, IllegalAccessException {

		Type t = f.getType();

		if (t.equals(String.class)) {
			((JTextField) input).setText((String) f.get(o));
		}

		if (t.equals(int.class)) {
			((JTextField) input).setText(f.get(o).toString());
		}

		if (t.equals(double.class)) {
			((JTextField) input).setText(f.get(o).toString());
		}

		if (t.equals(Date.class)) {
			((JTextField) input).setText(f.get(o).toString());
		}

		if (t.equals(Color.class)) {
			((JTextField) input).setText(f.get(o).toString());
		}

		return input;
	}

	/**
	 * Returns the value of the given input component
	 * 
	 * @param input
	 *            the input component to get the value from
	 * @param f
	 *            the field that this input component corresponds to
	 * @return the value of the input component
	 */
	public Object getInputComponentValue(JComponent input, Field f) {

		Type t = f.getType();
		Object o = null;

		if (t.equals(String.class)) {
			o = ((JTextField) input).getText();
		}

		if (t.equals(int.class)) {
			o = Integer.parseInt(((JTextField) input).getText());
		}

		if (t.equals(double.class)) {
			o = Double.parseDouble(((JTextField) input).getText());
		}

		if (t.equals(Date.class)) {
			// Hack until there is a date component that will return a date
			// instead of text

			o = new Date(2012, 2, 2);
		}

		if (t.equals(Color.class)) {
			// Hack until there is a color component that will return a color
			// instead of text
			o = Color.PINK;
			// o = ((JTextField) input).getText();
		}

		return o;
	}

	/**
	 * Given a field will return the input component that will represent that
	 * field
	 * 
	 * @param f
	 * @return
	 */
	public JComponent createFieldFormNameComponent(Field f) {

		JLabel name = new JLabel();
		name.setText(f.getName());
		return name;
	}

}
