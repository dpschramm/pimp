package pimp;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
	private Map<String, JComponent> fieldToComponentMapping;
	private Map<Type, FormElement> typeToFormElementMapping;

	/**
	 * Constructor takes a Class for which it can build forms for
	 * 
	 * @param c
	 */
	public FormBuilder(Class c) {
		this.c = c;
		fieldToComponentMapping = new HashMap<String, JComponent>();
		typeToFormElementMapping = new HashMap<Type, FormElement>();

		addFormElement(new StringFormElement()); // Default to string if form
													// builder type added
		// jp = createForm();
	}

	public void addFormElement(FormElement fe) {
		typeToFormElementMapping.put(fe.getInputType(), fe);
	}

	/**
	 * Creates the form based on the Class c
	 * 
	 * @return a JPanel form that represents the class
	 */
	public void createForm() {

		Field[] fields = c.getFields();

		JPanel panel = new JPanel();
		GridLayout gl = new GridLayout(fields.length, 2);
		panel.setLayout(gl);

		// For Public Fields just need to check if the Form Field Annotation is
		// present
		for (Field f : fields) {
			if (f.isAnnotationPresent(FormField.class)) {
				panel.add(createFieldFormNameComponent(f));
				FormElement fe = typeToFormElementMapping.get(f.getType());
				if (fe == null) {
					// Default to String Input
					fe = typeToFormElementMapping.get(String.class);
				}
				JComponent input = fe.createComponent();
				fieldToComponentMapping.put(f.getName(), input);
				panel.add(input);
			}
		}

		jp = panel;
	}

	/**
	 * Clears the current form
	 * 
	 * @return a cleared form
	 */
	public JComponent getBlankForm() {
		createForm();
		return jp;
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
			if (f.isAnnotationPresent(FormField.class)) {
				JComponent input = fieldToComponentMapping.get(f.getName());
				FormElement fe = typeToFormElementMapping.get(f.getType());
				fe.setValue(input, f.get(o));
			}
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
			if (f.isAnnotationPresent(FormField.class)) {
				JComponent input = fieldToComponentMapping.get(f.getName());
				FormElement fe = typeToFormElementMapping.get(f.getType());
				f.set(o, fe.getValue(input));
			}
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
			if (f.isAnnotationPresent(FormField.class)) {
				JComponent input = fieldToComponentMapping.get(f.getName());
				FormElement fe = typeToFormElementMapping.get(f.getType());
				f.set(o, fe.getValue(input));
			}
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
	private JComponent createFieldFormNameComponent(Field f) {

		JLabel name = new JLabel();
		name.setText(f.getName());
		return name;
	}

}
