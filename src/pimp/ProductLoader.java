package pimp;

import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Car;
import pimp.productdefs.Product;

/**
 * This class monitors a folder and return a list of classes that can
 * be created.
 * 
 * @author Daniel Schramm
 *
 */
public class ProductLoader {
	
	private String directory;
	private List<Class <? extends Product>> classList;
	
	/**
	 * Each ProductLoader is only setup to monitor a single directory. This
	 * could be an example where we could use the Singleton design pattern.
	 * 
	 * @param directory	the directory to monitor.
	 */
	public ProductLoader(String directory) {
		
		this.directory = directory;
		
		// Build the initial class list.
		readDirectory();
	}
	
	/** 
	 * This method extracts the classes from the specified directory. Call
	 * it whenever you want to update the class list.
	 * 
	 * See this Stack Overflow question for possible solutions:
	 * http://stackoverflow.com/questions/1456930/how-do-i-read-all-classes-from-a-java-package-in-the-classpath
	 */
	private void readDirectory() {
		
		classList = new ArrayList<Class <? extends Product>>();
		
		/** TODO */
		
		// Temporary test classes.
		classList.add(Car.class);
		classList.add(Product.class);
	}
	
	/**
	 * Returns the list of product classes that are defined in the specified
	 * directory. Generics are used to ensure all classes extend the Product 
	 * class, although they are not guaranteed to be concrete.
	 * 
	 * @return a List of class types in the directory.
	 */
	public List<Class <? extends Product>> getClassList() {
		return classList;
	}
}
