package pimp.shoe;

import javax.swing.JButton;
import javax.swing.JFrame;

import pimp.form.CompanionForm;
import pimp.model.Product;
import pimp.shoe.ShoeCompainionForm;

@CompanionForm(form="pimp.shoe.ShoeCompainionForm")
public class Shoe extends Product {
	
	public double shoeSize;
	public String sizingSystem;
	
	public Shoe() {
		super();
	}
	
	
	@Override
	public String toString() {
		return this.name;
	}
}
