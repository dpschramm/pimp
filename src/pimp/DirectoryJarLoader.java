package pimp;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class loads all the classes from the jars in a specified folder.
 * The list of these classes can be retrieved using getClassList.
 * 
 * @author Daniel Schramm
 */
public class DirectoryJarLoader {
	
	private List<Class<?>> classList;
	
	/**
	 * A DirectoryJarLoader loads all classes in a directory.
	 * 
	 * The package name is required in order to load the class, however 
	 * this should not cause issues as a folder should theoretically 
	 * only contain classes from a single package.
	 * 
	 * We could potentially use the Singleton pattern for this class.
	 * 
	 * @param directoryName	the directory to monitor.
	 */
	public DirectoryJarLoader(String directoryName) {
		// Clear the previous class list.
		classList = new ArrayList<Class<?>>();
		
		// Create a string pointing to the product folder.
		final ClassLoader loader = this.getClass().getClassLoader();
		
		URL directoryUrl = loader.getResource(directoryName+File.separator);
		if (directoryUrl == null) {
			System.out.println("Could not find the '" + directoryName + 
					"' folder in " + System.getProperty("user.dir"));
			return; // Could not find the product directory.
		}
		
		// Get a list of files in the directory.
		List<File> jarFiles = new ArrayList<File>();
		String decodedUrl = "";
		try {
			decodedUrl = URLDecoder.decode(directoryUrl.getFile(), "UTF-8");
			File jarFolder = new File(decodedUrl);
			jarFiles = Arrays.asList(jarFolder.listFiles());
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Loop through each in the .
		System.out.println("Searching in: '" + decodedUrl + "'");
	    for(File file: jarFiles){
		    // Check to make sure it is a .class file.
		    if (file.getName().endsWith(".jar")) {
			    System.out.println("Checking jar file: " + file.getName());
		      	// Load all the classes in the jarFile.
		        classList.addAll(loadClassesFromJar(file));
		    }
	    }
	}
	
	private List<Class<?>> loadClassesFromJar(File file) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		try {
			// Create the ClassLoader for this Jar.
		    URLClassLoader classLoader = new URLClassLoader( new URL[] {file.toURI().toURL()} );
			
			JarFile jar = new JarFile(file);
			// Check each entry in the Jar to see if it is a class.
			for (JarEntry entry: Collections.list(jar.entries())) {
				if (entry.getName().endsWith(".class")) {
					String className = entry.getName();
					className = className.replace("/", ".").replace(".class","");
					// Load the class.
					classes.add(classLoader.loadClass(className));
					System.out.println("Class loaded: " + className);
				}
	    	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return classes;
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