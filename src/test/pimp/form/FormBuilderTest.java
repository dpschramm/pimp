/**
 * 
 */
package test.pimp.form;


import java.awt.Color;
import java.lang.reflect.Field;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.junit.After;
import org.junit.Before;

import pimp.form.Form;
import pimp.form.FormBuilder;
import pimp.form.StringFormElement;
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
			Form f = fb.createForm(jacket);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Create form threw an exception");
		}
	}
	
	/**
	 * Test Form Builder with object that only contains the default Field types
	 * (String, Int, Double, Color, Date)
	 */
	public void testObjectWithDefaultFormTypeFields(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(drink);
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
			Form f = fb.createForm(drink);
			Drink drinkFromForm = (Drink) fb.getProductFromForm(f);
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
			Form f = fb.createForm(jacket);
			Jacket jacketFromForm = (Jacket) fb.getProductFromForm(f);
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
			Form f = fb.createForm(Drink.class);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		}
	}

	public void testCreateFormDoesNotThrowExceptionWithUnknownTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(Jacket.class);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		}
	}
	
	public void testFillFormDoesNotThrowExceptionWithDefaultTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(Drink.class);
			fb.fillForm(f, drink);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Fill Form threw an exception");
		}
	}

	public void testFillFormDoesNotThrowExceptionWithUnknownTypesClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(Jacket.class);
			fb.fillForm(f, jacket);
		} catch (IllegalArgumentException e) {
			fail("Create form threw an exception");
		} catch (IllegalAccessException e) {
			fail("Fill Form threw an exception");
		}
	}
	
	public void testFillFormThrowsExceptionTryingToFillWithDifferentClass(){
		
		FormBuilder fb = new FormBuilder();
		try {
			Form f = fb.createForm(Jacket.class);
			fb.fillForm(f, drink);
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
