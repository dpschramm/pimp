package pimp.form;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

import pimp.productdefs.Product;

public class Form extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Class c;
	Map<String, JComponent> fieldToComponentMapping;
	
	
	public Form(Class c) {
		super();
		this.c = c;
		System.out.println("In Form " + c);
		fieldToComponentMapping = new HashMap<String, JComponent>();
		// TODO Auto-generated constructor stub
	}

	public Map<String, JComponent> getFieldToComponentMapping(){
		
		return Collections.unmodifiableMap(fieldToComponentMapping);
	}
	
	public void addFieldToComponentPair(String name, JComponent jc){	
		fieldToComponentMapping.put(name, jc);
	}
	
	public Class getFormClass(){
		return c;
	}
	
	public void emptyForm(){
		
	}

}
