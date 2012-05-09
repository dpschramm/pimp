package pimp.form;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import pimp.form.elements.FormElement;
import pimp.form.elements.UnsupportedTypeFormElement;
import pimp.model.Product;

/**
 * @author Joel
 * 
 *         Form is a subclass of Product From and is what the FormBuilder
 *         creates when wanting a dynamic Form
 * 
 */
public class Form<T extends Product> extends ProductForm<T> {

	private static final long serialVersionUID = 1L;
	private Class<?> c;
	private Map<String, JComponent> fieldToComponentMapping;
	private Map<Type, FormElement<? extends Object>> typeToFormElementMapping;
	FormElement<? extends Object> unsupportedFormElement;

	/**
	 * Constructor, takes a list of the FormElements that were used to create
	 * the form so that it is able to decode itself to a Product.
	 * 
	 * @param c
	 *            the class that is being used in the Form
	 * @param formElementsForForm
	 *            the form elements that were used to create the form
	 */
	public Form(Class<? extends T> c,
			List<FormElement<? extends Object>> formElementsForForm) {
		super();
		this.c = c;
		typeToFormElementMapping = new HashMap<Type, FormElement<? extends Object>>();
		fieldToComponentMapping = new HashMap<String, JComponent>();
		unsupportedFormElement = new UnsupportedTypeFormElement();
		// Add each of the Form Elements to the Map
		for (FormElement<? extends Object> fe : formElementsForForm) {
			addFormElement(fe);
		}
	}

	/**
	 * Adds a form element to the type to form element map.
	 * 
	 * @param fe
	 *            the form element to add
	 */
	private void addFormElement(FormElement<? extends Object> fe) {
		typeToFormElementMapping.put(fe.getInputType(), fe);
	}

	/**
	 * Get the class of the product that the form is representing.
	 * 
	 * @return the class of the product that the form is representing
	 */
	public Class<?> getFormClass() {
		return c;
	}

	/**
	 * Adds a field to input component pair to the Forms field to input Map.
	 * These are need to know what field in the product maps to which input
	 * component which is later used when getting and setting a product
	 * 
	 * @param f
	 *            the products field
	 * @param jc
	 *            the input component that represents the field in the form
	 */
	public void addFieldToComponentPair(Field f, JComponent jc) {

		FormElement<? extends Object> fe = typeToFormElementMapping.get(f
				.getType());
		if (fe == null) {
			fe = unsupportedFormElement;
		}
		if (!jc.getClass().equals(fe.createComponent().getClass())) {
			throw new IllegalArgumentException(
					"The Added Component must be the same type of component supported by the associated Form Element");
		}
		fieldToComponentMapping.put(f.getName(), jc);
	}

	/**
	 * @return an unmodifiable map of the Field to input component Mapping for
	 *         this form
	 */
	public Map<String, JComponent> getFieldToComponentMapping() {
		return Collections.unmodifiableMap(fieldToComponentMapping);
	}

	/**
	 * @return an unmodifiable map of Type to form element for this form
	 */
	public Map<Type, FormElement<? extends Object>> getFormElements() {
		return Collections.unmodifiableMap(typeToFormElementMapping);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pimp.form.ProductForm#setProduct(pimp.model.Product)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setProduct(T product) throws IllegalArgumentException,
			IllegalAccessException {

		if (!this.getFormClass().equals(product.getClass())) {
			throw new IllegalArgumentException("Incompatiable object");
		}

		Map<String, JComponent> fieldNameToComponent = this
				.getFieldToComponentMapping();

		// For each Field in the product that has a input component mapping
		for (Map.Entry<String, JComponent> entry : fieldNameToComponent
				.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = product.getClass().getField(entry.getKey());

				// Get the form element for this type
				FormElement fe = typeToFormElementMapping.get(field.getType());

				if (fe == null) {
					// Do not have a form element for this type so use the
					// unsupported form element
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see pimp.form.ProductForm#getProduct()
	 */
	public T getProduct() throws InstantiationException, IllegalAccessException {

		@SuppressWarnings("unchecked")
		T p = (T) this.getFormClass().newInstance();

		Map<String, JComponent> fieldNameToComponent = this
				.getFieldToComponentMapping();

		// For each Field in the product that has a input component mapping
		for (Map.Entry<String, JComponent> entry : fieldNameToComponent
				.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = this.getFormClass().getField(entry.getKey());
				
				// Get the form element for this type
				FormElement<?> fe = typeToFormElementMapping.get(field
						.getType());
				if (fe == null) {
					// Do not have a form element for this type so use the
					// unsupported form element
					fe = unsupportedFormElement;
				}
				Object inputValue = fe.getValue(input);
				
				// Set the field for the Product to return
				field.set(p, inputValue);

			} catch (SecurityException e) {
				// Fall through and do nothing as field does not exist in
				// Product
			} catch (NoSuchFieldException e) {
				// Fall through and do nothing as field does not exist in
				// Product
			}

		}

		return p;
	}

	/**
	 * Set the special form element that was used when a type was not supported,
	 * needed so is able to encode and decode for types that do not have a form
	 * element for
	 * 
	 * @param fe
	 *            unsupported form element
	 */
	public void setUnsupportedFormElement(FormElement<? extends Object> fe) {
		this.unsupportedFormElement = fe;
	}

	/**
	 * @param fe
	 *            the special form element that is used when a type is not
	 *            supported
	 */
	public FormElement<? extends Object> getUnsupportedFormElement() {
		return unsupportedFormElement;
	}

}
