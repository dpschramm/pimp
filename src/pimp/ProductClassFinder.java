package pimp;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Product;

/**
 * This class monitors a folder and return a list of classes that can
 * be created.
 * 
 * @author Daniel Schramm
 *
 */
public class ProductClassFinder {
	
	private List<Class <? extends Product>> classList;
	
	/**
	 * Each ProductLoader is only setup to monitor a single directory. This
	 * could be an example where we could use the Singleton design pattern.
	 * 
	 * @param directory	the directory to monitor.
	 */
	public ProductClassFinder(String directoryString) {
		
		// Build the initial class list.
		readDirectory(directoryString);
	}
	
	/** 
	 * This method extracts the classes from the specified directory. Call
	 * it whenever you want to update the class list.
	 * 
	 * See this Stack Overflow question for possible solutions:
	 * http://stackoverflow.com/questions/1456930/how-do-i-read-all-classes-from-a-java-package-in-the-classpath
	 */
	private void readDirectory(String directoryString) {
		// Clear the previous class list.
		classList = new ArrayList<Class <? extends Product>>();
		
		// Create a string pointing to the product folder.
		URL directoryUrl = ClassLoader.getSystemResource(directoryString);
		System.out.println("Searching for products in: " + directoryUrl.getPath());
		
		// Create the Class Loader which will read from the directory.
		URL[] classUrls = { directoryUrl };
		URLClassLoader ucl = new URLClassLoader(classUrls);
		
		// Get a list of files in the directory.
		File productFolder = new File(directoryUrl.getPath());
		File[] productFiles = productFolder.listFiles();
		
		// Loop through each file, adding it to the list of classes.
		String className;
        for(File file: productFiles){
        	// Get the class name without the .class at the end.
        	className = file.getName();
        	className = className.substring(0, className.lastIndexOf('.'));
        	
        	// Load the classes.
    		try {
    			Class c = ucl.loadClass("pimp.productdefs." + className);
    	        classList.add(c);
    		} catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
        }
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
