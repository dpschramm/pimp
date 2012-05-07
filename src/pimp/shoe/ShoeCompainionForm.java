package pimp.shoe;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pimp.form.ProductForm;
import pimp.model.Product;

public class ShoeCompainionForm extends ProductForm{
	
	public String name;
	
	JComboBox shoeSizeSelector;
	JComboBox shoeSizingSystemSelector;
	JTextField nameInput;
	JTextField quantityInput;
	
	String[] shoeSizeSystem = {
	         "US",
	         "EU",
	         "NZ",
	         "UK"
	};
	
	Double[] shoeSizes = {
	         8.0,
	         8.5,
	         9.0,
	         9.5,
	         10.0,
	         10.5,
	         11.0,
	         11.5,
	         12.0
	};
	
	/*
	 * Constructor needs to take a shoe object
	 * */
	public ShoeCompainionForm() {

		shoeSizeSelector = new JComboBox(shoeSizes);
		shoeSizingSystemSelector = new JComboBox(shoeSizeSystem);
		nameInput = new JTextField();
		quantityInput = new JTextField();
		
		GridLayout gl = new GridLayout(4, 2);
		setLayout(gl);
		
		add(new JLabel("Name"));
		add(nameInput);
		add(new JLabel("Quantity"));
		add(quantityInput);
		add(new JLabel("Shoe Size"));
		add(shoeSizeSelector);
		add(new JLabel("Shoe Sizing System"));
		add(shoeSizingSystemSelector);		
		
	}
	
	/*
	 * Constructor needs to take a shoe object
	 * */
	public ShoeCompainionForm(Shoe shoe) {
			this();
			setProduct(shoe);
	}

	public Shoe getProduct() {
		Shoe formShoe = new Shoe();
		formShoe.name = nameInput.getText();
		formShoe.quantity = Integer.parseInt(quantityInput.getText());
		formShoe.sizingSystem = (String) shoeSizingSystemSelector.getSelectedItem();
		System.out.println((Double) shoeSizeSelector.getSelectedItem());
		return formShoe;
	}

	public void setProduct(Product o) {
		
		if(!(o instanceof Shoe)){
			throw new IllegalArgumentException("Provided Object is not a Shoe");
		}
		
		Shoe s = (Shoe) o;
		nameInput.setText(s.name);
		quantityInput.setText("" +  s.quantity);
		shoeSizingSystemSelector.setSelectedItem(s.sizingSystem);
		shoeSizeSelector.setSelectedItem(s.shoeSize);
	}
}
