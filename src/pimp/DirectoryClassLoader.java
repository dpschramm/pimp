package pimp;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class examines a specified folder and loads the classes inside it.
 * The list of these classes can be retrieved using getClassList.
 * 
 * @author Daniel Schramm
 */
public class DirectoryClassLoader {
	
	private List<Class<?>> classList;
	
	/**
	 * A DirectoryClassLoader loads all classes in a directory.
	 * 
	 * The package name is required in order to load the class, however 
	 * this should not cause issues as a folder should theoretically 
	 * only contain classes from a single package.
	 * 
	 * We could potentially use the Singleton pattern for this class.
	 * 
	 * @param directoryName	the directory to monitor.
	 * @param packageName the package that the classes belong to.
	 */
	public DirectoryClassLoader(String directoryName, String packageName) {
		// Clear the previous class list.
		classList = new ArrayList<Class<?>>();
		
		// Create a string pointing to the product folder.
		URL directoryUrl = ClassLoader.getSystemResource(directoryName);
		if (directoryUrl == null) {
			System.out.println("Could not find the '" + directoryName + 
					"' folder in " + System.getProperty("user.dir"));
			return; // Could not find the product directory.
		}
		
		// Create the Class Loader which will read from the directory.
		URL[] classUrls = { directoryUrl };
		URLClassLoader ucl = new URLClassLoader(classUrls);
		
		// Get a list of files in the directory.
		File classFolder = new File(directoryUrl.getPath());
		File[] classFiles = classFolder.listFiles();
		
		// Loop through each file, adding it to the list of classes.
		System.out.println("Searching in: " + directoryUrl.getPath());
        for(File file: classFiles){
        	// Get the class name without the .class at the end.
        	String className = file.getName();
        	className = className.substring(0, className.lastIndexOf('.'));
        	
        	// Load the classes.
    		try {
    			Class<?> c = ucl.loadClass(packageName + "." + className);
    	        classList.add(c);
    	        System.out.println("Class found: " + className);
    		} catch (ClassNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}	
        }
	}
	
	/**
	 * Returns a list of the classes loaded.
	 * 
	 * @return the list of classes found in the directory.
	 */
	public List<Class<?>> getClassList() {
		return classList;
	}
}
