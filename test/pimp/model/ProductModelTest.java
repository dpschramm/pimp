package pimp.model;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pimp.productdefs.Drink;
import pimp.productdefs.Jacket;
import pimp.productdefs.Product;

public class ProductModelTest {

	ProductModel pm;
	
	List<Product> products;
	Jacket jacket;
	
	/**
	 * For each test, recreate the instance variables.
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		pm = new ProductModel();
		
		// Create the only product for the model.
		jacket = new Jacket();
		jacket.size = "Huge";
		jacket.brand = "Mac Pac";
		jacket.isWaterproof = true;
		jacket.colour = null;
		jacket.quantity = 7;
		jacket.name = "Joel's Jacket";
		
		// Setup an add listener.
		products = new ArrayList<Product>();
		pm.addProductsAddedListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				products.addAll((List<Product>) e.getSource());
			}
		});
		pm.addProductsDeletedListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				products.removeAll((List<Product>) e.getSource());
			}
		});
	}

	@Test
	public void testAddAndDeleteListOfProduct() {
		assertTrue(products.size() == 0);
		assertFalse(products.contains(jacket));
		
		Jacket jacket1 = new Jacket();
		jacket1.size = "Small";
		jacket1.brand = "Huffer";
		jacket1.isWaterproof = false;
		jacket1.colour = null;
		jacket1.quantity = 3;
		jacket1.name = "Ellie's Jacket";
		
		List<Product> jackets = new ArrayList<Product>();
		jackets.add(jacket);
		jackets.add(jacket1);
		
		pm.load(jackets);
		assertTrue(products.size() == 2);
		assertTrue(products.contains(jacket));
		assertTrue(products.contains(jacket1));
		
		pm.delete(jackets);
		assertTrue(products.size() == 0);
		assertFalse(products.contains(jacket));
		assertFalse(products.contains(jacket1));
	}

	@Test
	public void testAddProduct() {
		assertTrue(products.size() == 0);
		assertFalse(products.contains(jacket));
		
		pm.add(jacket);
		assertTrue(products.size() == 1);
		assertTrue(products.contains(jacket));
	}

	@Test
	public void testGet() {
		Drink d1 = new Drink();
		d1.name = "Fanta";
		Drink d2 = new Drink();
		d2.name = "Coke";
		
		List<Product> drinks = new ArrayList<Product>();
		drinks.add(d1);
		drinks.add(d2);
		
		assertTrue(products.size() == 0);
		assertFalse(products.contains(jacket));
		
		pm.load(drinks);
		assertTrue(products.size() == 2);
		assertTrue(products.contains(d1));
		assertTrue(products.contains(d2));
		
		pm.add(jacket);
		assertTrue(products.size() == 3);
		assertTrue(products.contains(jacket));
		
//		List<Product> drinks2 = pm.get(Drink.class.toString());
//		assertTrue(drinks2.size() == 2);
//		assertTrue(drinks2.containsAll(drinks));
	}

	@Test
	public void testUpdate() {
		// Add the initial jacket.
		pm.add(jacket);
		
		Jacket newJacket = new Jacket();
		newJacket.size = "Tiny";
		newJacket.brand = "Calvin Klein";
		newJacket.isWaterproof = false;
		newJacket.colour = null;
		newJacket.quantity = 1;
		newJacket.name = "Daniel's Jacket";
		
		// Update with the new jacket.
		pm.update(jacket, newJacket);
		
		assertTrue(products.size() == 1);
		assertFalse(products.contains(newJacket));
		
		assertTrue(jacket.name.equals(newJacket.name));
		assertTrue(jacket.brand.equals(newJacket.brand));
		assertTrue(jacket.quantity == newJacket.quantity);
	}

	@Test
	public void testAddToClassesLoaded() {
		pm.addToClassesLoaded(Jacket.class.toString());
		assertTrue(pm.isLoaded(Jacket.class.toString()));
	}

}
