package pimp.form;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import pimp.formelements.FormElement;
import pimp.formelements.UnsupportedTypeFormElement;
import pimp.model.Product;

public class Form<T extends Product> extends ProductForm<T>{
	
	private static final long serialVersionUID = 1L;
	private Class<?> c;
	private Map<String, JComponent> fieldToComponentMapping;
	private Map<Type, FormElement<? extends Object>> typeToFormElementMapping;
	FormElement<? extends Object> unsupportedFormElement;
	
	public Form(Class<? extends T> c, List<FormElement<? extends Object>> formElementsForForm) {
		super();
		this.c = c;
		typeToFormElementMapping = new HashMap<Type, FormElement<? extends Object>>();
		fieldToComponentMapping = new HashMap<String, JComponent>();
		unsupportedFormElement = new UnsupportedTypeFormElement();
		for(FormElement<? extends Object> fe : formElementsForForm){
			addFormElement(fe);
		}
	}
	
	private void addFormElement(FormElement<? extends Object> fe) {
		typeToFormElementMapping.put(fe.getInputType(), fe);
	}
	
	public Class<?> getFormClass(){
		return c;
	}
	
	public void addFieldToComponentPair(Field f, JComponent jc){
		
		FormElement<? extends Object> fe = typeToFormElementMapping.get(f.getType());
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
	
	public Map<Type, FormElement<? extends Object>> getFormElements(){
		return Collections.unmodifiableMap(typeToFormElementMapping);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setProduct(T product) throws IllegalArgumentException, IllegalAccessException {
		
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
	
	public T getProduct() throws InstantiationException, IllegalAccessException {

		@SuppressWarnings("unchecked")
		T p = (T) this.getFormClass().newInstance();

		Map<String, JComponent> fieldNameToComponent = this.getFieldToComponentMapping();

		for (Map.Entry<String, JComponent> entry : fieldNameToComponent.entrySet()) {

			JComponent input = entry.getValue();
			Field field;
			try {
				field = this.getFormClass().getField(entry.getKey());
				FormElement<?> fe = typeToFormElementMapping.get(field.getType());
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

		return p;
	}
	
	public void setUnsupportedFormElement(FormElement<? extends Object> fe) {
		this.unsupportedFormElement = fe;
	}
	
	public FormElement<? extends Object> getUnsupportedFormElement() {
		return unsupportedFormElement;
	}
	
}
