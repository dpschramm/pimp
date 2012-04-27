package pimp.testdefs;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pimp.form.CompanionForm;
import pimp.ConctreteCompanionFormExample;

public class ShoeCompainionForm implements CompanionForm{
	
	public String name;
	
	JPanel form;
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
	
	public ShoeCompainionForm() {

		shoeSizeSelector = new JComboBox(shoeSizes);
		shoeSizingSystemSelector = new JComboBox(shoeSizeSystem);
		nameInput = new JTextField();
		quantityInput = new JTextField();
		
		form = new JPanel();
		GridLayout gl = new GridLayout(4, 2);
		form.setLayout(gl);
		
		form.add(new JLabel("Name"));
		form.add(nameInput);
		form.add(new JLabel("Quantity"));
		form.add(quantityInput);
		form.add(new JLabel("Shoe Size"));
		form.add(shoeSizeSelector);
		form.add(new JLabel("Shoe Sizing System"));
		form.add(shoeSizingSystemSelector);		
		
	}

	@Override
	public JComponent getForm() {
		return form;
	}

	@Override
	public Shoe getObject() {
		Shoe formShoe = new Shoe();
		formShoe.name = nameInput.getText();
		formShoe.quantity = Integer.parseInt(quantityInput.getText());
		formShoe.shoeSize = (Double) shoeSizeSelector.getSelectedItem();
		formShoe.sizingSystem = (String) shoeSizingSystemSelector.getSelectedItem();
		return formShoe;
	}

	@Override
	public void updateForm(Object o) {
		if(!(o instanceof Shoe)){
			throw new IllegalArgumentException("Provided Object is not a Shoe");
		}
		Shoe s = (Shoe) o;
		nameInput.setText(s.name);
		quantityInput.setText("" +  s.quantity);
		shoeSizeSelector.setSelectedItem(s.shoeSize);
		shoeSizeSelector.setSelectedItem(s.sizingSystem);
	}
	
	
}
