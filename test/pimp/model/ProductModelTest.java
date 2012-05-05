package pimp.model;

import static org.junit.Assert.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
	}
	
	@Test
	public void testProductModel() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddListOfProduct() {
		fail("Not yet implemented");
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
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
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
		
		System.out.println(jacket.name);
		assertTrue(jacket.name.equals(newJacket.name));
	}

	@Test
	public void testAddToClassesLoaded() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLoaded() {
		fail("Not yet implemented");
	}

}
