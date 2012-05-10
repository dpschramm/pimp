/**
 * 
 */
package pimp.form;


import java.awt.Color;
import java.lang.reflect.Field;

import javax.swing.JFrame;

import org.junit.Before;

import pimp.form.Form;
import pimp.form.FormBuilder;
import pimp.form.elements.StringFormElement;
import pimp.productdefs.Drink;
import pimp.productdefs.Jacket;

/**
 * @author Joel
 *
 */
public class FormBuilderTest extends junit.framework.TestCase{

	Jacket jacket;
	Drink drink;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		jacket = new Jacket();
		jacket.brand = "Ed Hardy";
		jacket.colour = Color.RED;
		jacket.isWaterproof = false;
		jacket.name = "My Sweet Jacket";
		jacket.quantity = 23;
		
		drink = new Drink();
		drink.capacity = "Large";
		drink.flavour = "Blue";
		drink.name = "Gatorade";
		drink.quantity = 40;
	}
	
	/**
	 * Test the form builder with an object that has a boolean which is not one
	 * of the default field types
	 */
	public void testObjectWithUnknownFieldType(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form<?> f = fb.createForm(jacket);
			JFrame testFrame = new JFrame();
			testFrame.add(f);
			testFrame.setVisible(true);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Create form threw an exception");
		}
		
		System.out.println("test");
	}
	
	/**
	 * Test Form Builder with object that only contains the default Field types
	 * (String, Int, Double, Color, Date)
	 */
	public void testObjectWithDefaultFormTypeFields(){
		
		FormBuilder fb = new FormBuilder();
		try {
			fb.createForm(drink);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Create form threw an exception");
		}
	}
	
	/**
	 * Test that if a Form is given an object that contains only the default
	 * field types that it will be able to return the same object from the form
	 */
	public void testObjectReturnedIsSameAsFormWasGiven(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form<?> f = fb.createForm(drink);
			Drink drinkFromForm = (Drink) f.getProduct();
			System.out.println(drinkFromForm);
			System.out.println(drink);
			for(Field field : drink.getClass().getFields()){	
				if(field.get(drink) != null){
					assertTrue(field.get(drinkFromForm).equals(field.get(drink)));
				}
			}
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Create form threw an exception");
		} catch (InstantiationException e) {
			fail("Get Product from From failed");
		}
	}

	/**
	 * Test that if a Form is given an object that contains only the default
	 * field types that it will be able to return the same object from the form
	 */
	public void testObjectReturnedIsSameAsFormWasGivenWithUnknownFields(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form<?> f = fb.createForm(jacket);
			Jacket jacketFromForm = (Jacket) f.getProduct();
			System.out.println(jacketFromForm);
			System.out.println(jacket);
			for(Field field : jacket.getClass().getFields()){	
				if(field.get(jacket) != null){
					assertTrue(field.get(jacketFromForm).equals(field.get(jacket)));
				}
			}
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Create form threw an exception");
		} catch (InstantiationException e) {
			fail("Get Product from From failed");
		}
	}
	
	
	public void testCreateFormDoesNotThrowExceptionWithDefaultTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			fb.createForm(Drink.class);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		}
	}

	public void testCreateFormDoesNotThrowExceptionWithUnknownTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			fb.createForm(Jacket.class);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void testFillFormDoesNotThrowExceptionWithDefaultTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {	
			Form<Drink> f = (Form<Drink>) fb.createForm(Drink.class);
			f.setProduct(drink);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Fill Form threw an exception");
		}
	}

	@SuppressWarnings("unchecked")
	public void testFillFormDoesNotThrowExceptionWithUnknownTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form<Jacket> f = (Form<Jacket>) fb.createForm(Jacket.class);
			f.setProduct(jacket);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Fill Form threw an exception");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void testFillFormThrowsExceptionTryingToFillWithDifferentClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(Jacket.class);
			f.setProduct(drink);
			fail("Expected Exception");
		} catch (IllegalArgumentException e) {
			// Expected
		} catch (IllegalAccessException e) {
			fail("Fill Form threw an unexpected exception");
		}
	}
	
	public void testAddingNullFormElement(){
		
		FormBuilder fb = new FormBuilder();
		try {
			fb.addFormElement(null);
			fail("Expected Exception");
		} catch (NullPointerException npe){
			// Expected
		}
	}
	
	public void testAddingFormElement(){
		
		FormBuilder fb = new FormBuilder();
		fb.addFormElement(new StringFormElement());
	}
	
	
}
