package pimp.shoe;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import pimp.form.ProductForm;

/**
 * @author Joel Harrison
 * 
 *         Demonstration of how a companion form would be created.
 * 
 * @param <T>
 */
public class ShoeCompainionForm<T extends Shoe> extends ProductForm<Shoe> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String name;

	JComboBox shoeSizeSelector;
	JComboBox shoeSizingSystemSelector;
	JTextField nameInput;
	JTextField quantityInput;

	String[] shoeSizeSystem = { "US", "EU", "NZ", "UK" };

	Double[] shoeSizes = { 8.0, 8.5, 9.0, 9.5, 10.0, 10.5, 11.0, 11.5, 12.0 };

	/*
	 * Constructor needs to take a shoe object
	 */
	public ShoeCompainionForm() {

		shoeSizeSelector = new JComboBox(shoeSizes);
		shoeSizeSelector.setSelectedIndex(0);
		shoeSizingSystemSelector = new JComboBox(shoeSizeSystem);
		shoeSizingSystemSelector.setSelectedIndex(0);
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
	 */
	public ShoeCompainionForm(T shoe) {
		this();
		setProduct(shoe);
	}

	public Shoe getProduct() {
		Shoe formShoe = new Shoe();
		formShoe.name = nameInput.getText();
		try {
			formShoe.quantity = Integer.parseInt(quantityInput.getText());
		} catch (NumberFormatException nfe) {
			formShoe.quantity = 0;
		} catch (NullPointerException npe) {
			formShoe.quantity = 0;
		}

		formShoe.sizingSystem = (String) shoeSizingSystemSelector
				.getSelectedItem();

		if (formShoe.sizingSystem == null) {
			formShoe.sizingSystem = "US";
		}

		if (shoeSizeSelector.getSelectedItem() != null) {
			try {
				formShoe.shoeSize = Double.parseDouble(shoeSizeSelector
						.getSelectedItem().toString());
			} catch (NumberFormatException nfe) {
				formShoe.shoeSize = 8.0;
			} catch (NullPointerException npe) {
				formShoe.shoeSize = 8.0;
			}
		} else {
			formShoe.shoeSize = 8.0;
		}
		return formShoe;
	}

	public void setProduct(Shoe o) {

		if (!(o instanceof Shoe)) {
			throw new IllegalArgumentException("Provided Object is not a Shoe");
		}

		Shoe s = (Shoe) o;
		nameInput.setText(s.name);
		quantityInput.setText("" + s.quantity);
		shoeSizingSystemSelector.setSelectedItem(s.sizingSystem);
		if (s.sizingSystem == null) {
			s.sizingSystem = "US";
			shoeSizingSystemSelector.setSelectedItem(s.sizingSystem);
		}
		shoeSizeSelector.setSelectedItem(s.shoeSize);
		if (s.shoeSize == null) {
			s.shoeSize = 8.0;
			shoeSizeSelector.setSelectedItem(s.shoeSize);
		}
	}
}
