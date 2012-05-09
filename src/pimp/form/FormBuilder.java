package pimp.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pimp.annotations.FormField;
import pimp.form.elements.ColorFormElement;
import pimp.form.elements.DateFormElement;
import pimp.form.elements.DoubleFormElement;
import pimp.form.elements.FormElement;
import pimp.form.elements.IntFormElement;
import pimp.form.elements.NullDecoratorFormElement;
import pimp.form.elements.StringFormElement;
import pimp.form.elements.UnsupportedTypeFormElement;
import pimp.model.Product;

/**
 * @author Joel
 * 
 *         Form Builder is used to dynamically create Forms. Given a class it
 *         will create a product form for it. It contains a number of form
 *         elements for the default types of int, double, string, date, color.
 *         You are able to add more form elements and you can also add form
 *         elements to override the default form elements. The Form Builder
 *         controls the structural look of the ProductFrom it creates.
 * 
 */
public class FormBuilder {

	private ArrayList<FormElement<? extends Object>> formElements;
	private FormElement<? extends Object> unsuportedTypeElement;

	/**
	 * For a Form Builder, Form Builder is used to create Forms
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FormBuilder() {
		formElements = new ArrayList<FormElement<? extends Object>>();

		// Special form element that is used if asked to create a input for
		// which it does not have form element for
		unsuportedTypeElement = new UnsupportedTypeFormElement();

		// Default Form Builder has double, int, string, color, date
		addFormElement(new NullDecoratorFormElement(new StringFormElement()));
		addFormElement(new IntFormElement());
		addFormElement(new DoubleFormElement());
		addFormElement(new NullDecoratorFormElement(new DateFormElement()));
		addFormElement(new NullDecoratorFormElement(new ColorFormElement()));
	}

	/**
	 * Adds a form element to the form builder which will be used when creating
	 * forms. If a form element is added for which the form builder already has
	 * a form element for that type then the newly added form element overrides
	 * the old for when creating input components for that type. Therefore you
	 * are able to also override the default form elements.
	 * 
	 * @param fe
	 *            the form element to add
	 */
	public void addFormElement(FormElement<? extends Object> fe) {
		if (fe == null) {
			throw new NullPointerException();
		}
		formElements.add(fe);
	}

	/**
	 * Creates the form based on the Class c, using the form elements that it
	 * holds
	 * 
	 * @return a Form that represents the class
	 */
	public Form<? extends Product> createForm(Class<? extends Product> c) {
		// Create form.
		List<FormElement<? extends Object>> formElementsForForm = new ArrayList<FormElement<? extends Object>>();
		formElementsForForm.addAll(formElements);
		Form<? extends Product> form = new Form<Product>(c, formElementsForForm);

		// Add the unsupported form element
		form.setUnsupportedFormElement(unsuportedTypeElement);

		form.setLayout(new GridBagLayout());

		// Get the fields in order so superclass's fields come first
		LinkedHashSet<Field> fields = getFieldsInOrder(c, Product.class);

		JPanel grid = new JPanel(new GridBagLayout());

		// Input constraints.
		GridBagConstraints cInput = new GridBagConstraints();
		cInput.weightx = 1.0;
		cInput.insets = new Insets(5, 5, 5, 5); // T, L, B, R.
		cInput.fill = GridBagConstraints.HORIZONTAL;

		// Label constraints.
		GridBagConstraints cLabel = new GridBagConstraints();
		cLabel.insets = new Insets(5, 5, 5, 5); // T, L, B, R.
		cLabel.anchor = GridBagConstraints.WEST;

		int row = 0;
		for (Field f : fields) {
			// Only add fields with the Form Field Annotation.
			if (f.isAnnotationPresent(FormField.class)) {
				// Update constraints to next row.
				cLabel.gridy = row;
				cInput.gridy = row;
				row++;

				// Create label.
				grid.add(createFieldFormNameComponent(f), cLabel);

				// Create input.
				@SuppressWarnings("rawtypes")
				FormElement fe = form.getFormElements().get(f.getType());
				if (fe == null) { // Default to String Input.
					fe = form.getUnsupportedFormElement();
				}
				JComponent input = fe.createComponent();
				grid.add(input, cInput);

				// Set mapping to input field.
				try {
					form.addFieldToComponentPair(f, input);
				} catch (IllegalArgumentException IAE) {
					// Tried to add a component that is not equal forms
					// associated Form Element Component
				}
			}
		}

		/*
		 * Set the form to expand horizontally and vertically, but only expands
		 * it's contents horizontally. Anchor contents to the top leaving the
		 * area at the bottom empty.
		 */
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		form.add(grid, gbc);

		return form;
	}

	/**
	 * Get the fields in order so the highest parents fields a are first in the
	 * list and so on till the current class
	 * 
	 * @param c
	 * @param upperClass
	 * @return
	 */
	private LinkedHashSet<Field> getFieldsInOrder(Class<?> c,
			Class<?> upperClass) {

		LinkedHashSet<Field> fields = new LinkedHashSet<Field>();

		if (c.equals(upperClass)) {
			// It is the top class so just add its fields
			fields.addAll(Arrays.asList(c.getFields()));
		} else {
			// Add parent fields then add current classes fields
			fields.addAll(getFieldsInOrder(c.getSuperclass(), upperClass));
			fields.addAll(Arrays.asList(c.getFields()));

		}
		return fields;
	}

	/**
	 * Convenience Method which creates a form and fills it with the specified
	 * Product
	 * 
	 * @return a Form that represents the class
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	public Form<? extends Product> createForm(Product product)
			throws IllegalArgumentException, IllegalAccessException {

		@SuppressWarnings("rawtypes")
		Form form = createForm(product.getClass());
		form.setProduct(product);

		return form;
	}

	/**
	 * Given a field will return the label component that will represent that
	 * field
	 * 
	 * @param f
	 *            the field to create the label for
	 * @return the created label
	 */
	private JComponent createFieldFormNameComponent(Field f) {

		JLabel name = new JLabel();
		name.setText(f.getName());
		if (f.isAnnotationPresent(FormField.class)) {
			FormField ff = f.getAnnotation(FormField.class);
			if (!ff.displayName().equals(FormField.NULL)) {
				name.setText(ff.displayName());
			}

		}
		return name;
	}

}
