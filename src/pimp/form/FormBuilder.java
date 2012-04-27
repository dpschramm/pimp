package pimp.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pimp.productdefs.Product;

public class FormBuilder {

	private Map<Type, FormElement> typeToFormElementMapping;
	private FormElement unsuportedTypeElement;
	
	/**
	 * For a Form Builder, Form Builder is used to create and decode Forms
	 */
	public FormBuilder() {
		typeToFormElementMapping = new HashMap<Type, FormElement>();
		
		unsuportedTypeElement = new UnsupportedTypeFormElement();
		// Default Form Builder has double, int, string, color, date
		addFormElement(new StringFormElement());
		addFormElement(new IntFormElement());
		addFormElement(new DoubleFormElement());
		addFormElement(new DateFormElement());
		addFormElement(new ColorFormElement());
	}

	public void addFormElement(FormElement fe) {
		typeToFormElementMapping.put(fe.getInputType(), fe);
	}

	/**
	 * Creates the form based on the Class c
	 * 
	 * @return a Form that represents the class
	 */
	public Form createForm(Class<?> c) {
		// Create form.
		Form form = new Form(c);
		form.setLayout(new GridBagLayout());
	
		// Get all public fields for the class.
		Field[] fields = c.getFields();
		
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
				FormElement fe = typeToFormElementMapping.get(f.getType());
				if (fe == null) {	// Default to String Input.
					fe = unsuportedTypeElement;
				}
				JComponent input = fe.createComponent();
				grid.add(input, cInput);
				
				// Set mapping to input field.
				form.addFieldToComponentPair(f.getName(), input);
			}
		}
		
		/* Set the form to expand horizontally and vertically, but only expands
		 * it's contents horizontally. Anchor contents to the top leaving the 
		 * area at the bottom empty. */
		GridBagConstraints gbc = new GridBagConstraints();	
		gbc.weighty = 1.0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH; 
		form.add(grid, gbc);
		
		return form;
	}
	
	/**
	 * Creates the form based on the Class c
	 * 
	 * @return a Form that represents the class
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Form createForm(Object product) throws IllegalArgumentException, IllegalAccessException {

		Form form = createForm(product.getClass());
		fillForm(form, product);
		
		return form;
	}

	/**
	 * Will fill the given Form based on the object given 
	 * 
	 * @param o
	 *            the object to fill the form with
	 * @return the form filled with the objects field values
	 * 
	 * @throws IllegalArgumentException
	 *             if the given object is not the same type as the class the
	 *             form was created with
	 * @throws IllegalAccessException
	 */
	public void fillForm(Form form, Object product) throws IllegalArgumentException,
			IllegalAccessException {
		System.out.println("In Fill Form");
		if (!form.getFormClass().equals(product.getClass())) {
			throw new IllegalArgumentException("Incompatiable object");
		}
		
		Map<String, JComponent> fieldNameToComponent = form.getFieldToComponentMapping();
		
		for(Map.Entry<String, JComponent> entry : fieldNameToComponent.entrySet()){
			
			JComponent input = entry.getValue();
			Field field;
			try {
				field = product.getClass().getField(entry.getKey());
				FormElement fe = typeToFormElementMapping.get(field.getType());
				if(fe == null){
					fe = unsuportedTypeElement;
				} 
				fe.setValue(input, field.get(product));

			} catch (SecurityException e) {
				// Fall through and do not set the form input value
			} catch (NoSuchFieldException e) {
				// Fall through and do not set the form input value
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
	public Object getProductFromForm(Form form) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		Object p = form.getFormClass().newInstance();

		Map<String, JComponent> fieldNameToComponent = form.getFieldToComponentMapping();

		for (Map.Entry<String, JComponent> entry : fieldNameToComponent.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = form.getFormClass().getField(entry.getKey());
				FormElement fe = typeToFormElementMapping.get(field.getType());
				if(fe == null){
					fe = unsuportedTypeElement;
				}
				Object inputValue = fe.getValue(input);
				field.set(p, inputValue);
				
			} catch (SecurityException e) {
				// Fall through and do nothing as field does not exist in Product
			} catch (NoSuchFieldException e) {
				// Fall through and do nothing as field does not exist in Product
			}
			
		}

		return p;
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
		if (f.isAnnotationPresent(FormField.class)) {
			FormField ff = f.getAnnotation(FormField.class);
			if(!ff.displayName().equals(FormField.NULL)){
				name.setText(ff.displayName());	
			}
			
		}
		return name;
	}

}
