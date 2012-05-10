package pimp.shoe;

import javax.swing.JFrame;

import org.junit.Before;

import pimp.shoe.Shoe;
import pimp.shoe.ShoeCompainionForm;

public class ShoeCompanionFormTest extends junit.framework.TestCase{
	
	Shoe s;
	
	@Before
	public void setUp() throws Exception {
		s = new Shoe();
		s.name = "Nike Air Ones";
		s.quantity = 0;

	}
	
	public void testSetValue() {
		
		ShoeCompainionForm<Shoe> scf = new ShoeCompainionForm<Shoe>();
		scf.setProduct(s);
	}

	public void testGetValue() {
		
		ShoeCompainionForm<Shoe> scf = new ShoeCompainionForm<Shoe>();
		scf.setProduct(s);
		JFrame jf = new JFrame();
		jf.add(scf);
		jf.setVisible(true);
		
		System.out.println(scf.getProduct());
		scf.setProduct(scf.getProduct());
		
	}
	
	public void testGetValueAfterSetting() {
		
		ShoeCompainionForm<Shoe> scf = new ShoeCompainionForm<Shoe>();
		s.shoeSize = 10.0;
		s.sizingSystem = "NZ";
		scf.setProduct(s);
		
		System.out.println(scf.getProduct());
		
	}
}
