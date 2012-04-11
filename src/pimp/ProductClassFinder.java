package pimp;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import pimp.productdefs.Car;
import pimp.productdefs.Jacket;
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
		// Create the new empty list of classes.
		classList = new ArrayList<Class <? extends Product>>();
		
		// Create a file pointer to the specified product folder.
		File productFolder = 
			new File(ClassLoader.getSystemResource(directoryString).getPath());
		System.out.println("Searching product folder: " + productFolder.getAbsolutePath());
		
		// Get the list of files in that folder.
		File[] productFiles = productFolder.listFiles();
		
		// Loop through each file, adding it to the list of class names.
		String className;
        for(File file: productFiles){
        	className = file.getName();
        	
        	// Remove .class from file name.
        	className = className.substring(0, className.lastIndexOf('.'));
        	
        	// Load the classes.
    		try {
    			// Add the trailing '/' so that URLClassLoader knows this is a directory.
    			URL classUrl = new URL(productFolder.toURI().toURL().toString() + "/");
    			URL[] classUrls = { classUrl };
    	        URLClassLoader ucl = new URLClassLoader(classUrls);
    	        Class c = ucl.loadClass("pimp.productdefs." + className);
    	        classList.add(c);
    		} catch (MalformedURLException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
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
