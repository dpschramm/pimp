package pimp.form;

import javax.swing.JComponent;

import pimp.model.Product;

public abstract class ProductForm<T extends Product> extends JComponent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public abstract T getProduct() throws InstantiationException, IllegalAccessException;
	
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
	public abstract void setProduct(T p) throws IllegalArgumentException, IllegalAccessException;

}
