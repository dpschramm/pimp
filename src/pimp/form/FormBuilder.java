package pimp.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pimp.model.Product;

public class FormBuilder {

	private List<FormElement> formElements;
	private FormElement unsuportedTypeElement;
	
	/**
	 * For a Form Builder, Form Builder is used to create and decode Forms
	 */
	public FormBuilder() {
		formElements = new ArrayList<FormElement>();
		
		unsuportedTypeElement = new UnsupportedTypeFormElement();
		// Default Form Builder has double, int, string, color, date
		addFormElement(new NullDecoratorFormElement(new StringFormElement()));
		addFormElement(new IntFormElement());
		addFormElement(new DoubleFormElement());
		addFormElement(new NullDecoratorFormElement(new DateFormElement()));
		addFormElement(new NullDecoratorFormElement(new ColorFormElement()));
	}

	public void addFormElement(FormElement fe) {
		if(fe == null){
			throw new NullPointerException();
		}
		formElements.add(fe);
	}

	/**
	 * Creates the form based on the Class c
	 * 
	 * @return a Form that represents the class
	 */
	public Form createForm(Class<? extends Product> c) {
		// Create form.
		List<FormElement> formElementsForForm = new ArrayList<FormElement>();
		formElementsForForm.addAll(formElements);
		Form form = new Form(c, formElementsForForm);

		form.setUnsupportedFormElement(unsuportedTypeElement);	
			
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
				FormElement fe = form.getFormElements().get(f.getType());
				if (fe == null) {	// Default to String Input.
					fe = form.getUnsupportedFormElement();
				}
				JComponent input = fe.createComponent();
				grid.add(input, cInput);
				
				// Set mapping to input field.
				try{
					form.addFieldToComponentPair(f, input);
				} catch(IllegalArgumentException IAE){
					// Tried to add a component that is not equal forms associated Form Element Component
				}
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
	 * Convience Method which creates a form and fills it with the specified Product
	 * 
	 * @return a Form that represents the class
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Form createForm(Product product) throws IllegalArgumentException, IllegalAccessException {

		Form form = createForm(product.getClass());
		form.setProduct(product);
		
		return form;
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
