package pimp.form;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import pimp.productdefs.Product;

public class Form extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private Class<?> c;
	private Map<String, JComponent> fieldToComponentMapping;
	private Map<Type, FormElement> typeToFormElementMapping;
	FormElement unsupportedFormElement;
	
	public Form(Class<?> c, List<FormElement> formElements) {
		super();
		this.c = c;
		System.out.println("In Form " + c);
		typeToFormElementMapping = new HashMap<Type, FormElement>();
		fieldToComponentMapping = new HashMap<String, JComponent>();
		unsupportedFormElement = new UnsupportedTypeFormElement();
		for(FormElement fe : formElements){
			addFormElement(fe);
		}
	}
	
	private void addFormElement(FormElement fe) {
		typeToFormElementMapping.put(fe.getInputType(), fe);
	}
	
	public Class<?> getFormClass(){
		return c;
	}
	
	public void addFieldToComponentPair(Field f, JComponent jc){
		
		FormElement fe = typeToFormElementMapping.get(f.getType());
		if(fe == null){
			fe = unsupportedFormElement;
		}
		if(!jc.getClass().equals(fe.createComponent().getClass())){
			throw new IllegalArgumentException("The Added Component must be the same type of component supported by the associated Form Element");
		}
		fieldToComponentMapping.put(f.getName(), jc);
	}

	public Map<String, JComponent> getFieldToComponentMapping(){
		return Collections.unmodifiableMap(fieldToComponentMapping);
	}
	
	public Map<Type, FormElement> getFormElements(){
		return Collections.unmodifiableMap(typeToFormElementMapping);
	}
	
	/**
	 * Will fill the given Form based on the Product given
	 * 
	 * @param product
	 *            the product to fill the form with
	 * @return the form filled with the product's field values
	 * 
	 * @throws IllegalArgumentException
	 *             If the given product is not the same type as the class the
	 *             form was created with
	 * @throws IllegalAccessException
	 *             If a field in the product has Java access control, and the
	 *             underlying field is inaccessible, the method throws an
	 *             IllegalAccessException.
	 * 
	 */
	public void setProduct(Product product) throws IllegalArgumentException, IllegalAccessException {
		
		if (!this.getFormClass().equals(product.getClass())) {
			throw new IllegalArgumentException("Incompatiable object");
		}

		Map<String, JComponent> fieldNameToComponent = this.getFieldToComponentMapping();

		for (Map.Entry<String, JComponent> entry : fieldNameToComponent
				.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = product.getClass().getField(entry.getKey());
				FormElement fe = typeToFormElementMapping.get(field.getType());
				if (fe == null) {
					fe = unsupportedFormElement;
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
	 * @throws IllegalAccessException
	 *             If this Field object enforces Java language access control,
	 *             and the underlying field is inaccessible, the method throws
	 *             an IllegalAccessException
	 * @throws InstantiationException
	 *             If the class or its nullary constructor is not accessible.
	 */
	public Product getProduct() throws InstantiationException, IllegalAccessException {

		Object p = this.getFormClass().newInstance();

		Map<String, JComponent> fieldNameToComponent = this.getFieldToComponentMapping();

		for (Map.Entry<String, JComponent> entry : fieldNameToComponent.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = this.getFormClass().getField(entry.getKey());
				FormElement fe = typeToFormElementMapping.get(field.getType());
				if(fe == null){
					fe = unsupportedFormElement;
				}
				Object inputValue = fe.getValue(input);
				field.set(p, inputValue);
				
			} catch (SecurityException e) {
				// Fall through and do nothing as field does not exist in Product
			} catch (NoSuchFieldException e) {
				// Fall through and do nothing as field does not exist in Product
			}
			
		}

		return (Product) p;
	}
	
	public void setUnsupportedFormElement(FormElement fe) {
		this.unsupportedFormElement = fe;
	}
	
	public FormElement getUnsupportedFormElement() {
		return unsupportedFormElement;
	}
	
}
