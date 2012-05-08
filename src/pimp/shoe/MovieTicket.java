package pimp.shoe;

import java.util.Date;

import pimp.annotations.FormField;
import pimp.model.Product;

public class MovieTicket extends Product {

	@FormField(displayName="Time")
	public Date time;
	
	@FormField(displayName="Theatre")
	public int Theatre;
	
	@Override
	public String toString() {
		return this.name;
	}
}
