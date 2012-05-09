package pimp.shoe;

import javax.swing.JButton;
import javax.swing.JFrame;

import pimp.annotations.CompanionForm;
import pimp.model.Product;
import pimp.shoe.ShoeCompainionForm;

/**
 * @author Joel Harrison
 * 
 *         Demonstration of how would specify a companion form
 * 
 */
@CompanionForm(form = "pimp.shoe.ShoeCompainionForm")
public class Shoe extends Product {

	public Double shoeSize;
	public String sizingSystem;

	public Shoe() {
		super();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
