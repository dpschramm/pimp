package pimp.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class provides a way to dynamically load jars at run time. Methods are
 * available to load all jars in a folder, or a specific jar file.
 * 
 * To use the loaded classes they must first be retrieved using the getClass or
 * getClassList methods.
 * 
 * Each class can only be loaded once, and can not be unloaded.
 * 
 * @author Daniel Schramm
 */
public class JarLoader {
	
	/**
	 * Loaded classes are mapped from name to class for fast retrieval.
	 */
	private static Map<String,Class<?>> classes = 
		new HashMap<String, Class<?>>();
	
	/**
	 * The private constructor makes it impossible to create an instance of
	 * a JarLoader. This was done because all methods are static, and
	 * it is therefore not necessary to instantiate an object of this class.
	 */
	private JarLoader() {}
	
	/**
	 * Get a loaded class by name.
	 * 
	 * @param className The fully qualified name of the class. This should be
	 * equal to the result of calling toString on the class.
	 * @return The class if it has been loaded, otherwise null.
	 */
	public static Class<?> getClass(String className) {
		return classes.get(className);
	}
	
	/**
	 * Get a set of all the classes that have been loaded.
	 * 
	 * The returned set is not backed by the JarLoader and will 
	 * not automatically update if more classes are loaded. You must make 
	 * another call to getClasses to get the newly loaded classes.
	 * 
	 * A set is returned to make it clear to developers that there can
	 * only be one of each class loaded at a time.
	 * 
	 * @return a new set containing the loaded classes.
	 */
	public static Set<Class<?>> getClasses() {
		return new HashSet<Class<?>>(classes.values());
	}
	
	/**
	 * Similar to getClasses() but only returns classes that extend a
	 * specified superclass.
	 * 
	 * @param superClass the class that all returned classes must subclass.
	 * @return a set of classes that subclass superClass.
	 */
	public static Set<Class<?>> getClasses(Class<?> superClass) {
		Set<Class<?>> subclasses = new HashSet<Class<?>>();
		// Find subclasses.
		for (Class<?> c : classes.values()) {
			if(isSubclass(c, superClass)) {
				subclasses.add(c);
				//System.out.println("Found class: " + className);
			}
		}
		return subclasses;
	}
	
	/**
	 * Checks if a class is the subclass of another.
	 * 
	 * @param subClass the class we are checking.
	 * @param superClass the super class that the class needs to extend.
	 * @return true if subClass extends superClass, otherwise false.
	 */
	private static boolean isSubclass(Class<?> subClass, Class<?> superClass) {
		Class<?> s = subClass.getSuperclass();
		while (s != null){
			if (s == superClass) return true;
			s = s.getSuperclass();
		}
		return false;
	}
	
	/**
	 * Allow classes to be loaded by specifying a directory name. The specified
	 * directory must be the current working directory.
	 * 
	 * @param dirName The child directory to search for .jar files.
	 */
	public static void load(String dirName) {
		// Get the resource.
		URL url = JarLoader.class.getClassLoader().getResource(dirName);
		if (url == null) {
			System.out.println("Could not find '" + dirName + 
					"' in " + System.getProperty("user.dir"));
			return;
		}
		try {
			URI uri = new URI("file", url.getPath(), null);
			load(uri);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	/**
	 * A convenience method to load all jar files in a particular directory,
	 * specified by a URI. 
	 * 
	 * The URI must be escaped by the calling function.
	 * 
	 * @param dir An escaped URI pointing to the directory to load from.
	 */
	public static void load(URI dir) {
		// Get a list of files in the directory.
		File jarFolder = new File(dir);
		List<File> jarFiles = Arrays.asList(jarFolder.listFiles());
		
		// Loop through each file checking for .jar files.
		System.out.println("Loading jar files in: " + dir);
	    for(File file: jarFiles){
		    if (file.getName().endsWith(".jar")) {
		        load(file);
		    }
	    }
	    return;
	}
	
	/**
	 * Load all classes in the specified file. If this file is not a jar file,
	 * no classes will be loaded.
	 * 
	 * All loaded classes are added to the internal list of loaded classes so
	 * they can be retrieved later.
	 * 
	 * @param file The file to load classes from.
	 */
	public static void load(File file) {
		try {
			// Get the entries from the jar file.
			JarFile jar = new JarFile(file);
			List<JarEntry> entries = Collections.list(jar.entries());
			
			// Ensure the jar has entries.	
			if (entries.size() == 0) {
				System.out.println("No entries found in " + 
						file.getAbsolutePath());
				System.out.println("It may not be a valid jar file.");
				return;
			}
			
			System.out.println("Loading from: " + file.getAbsolutePath());
			
			// Create the ClassLoader for this Jar.
			URLClassLoader classLoader =
				new URLClassLoader(new URL[] {file.toURI().toURL()});

			// Check each entry in the Jar to see if it is a class.
			for (JarEntry entry : entries) {
				String name = entry.getName();
				if (name.endsWith(".class")) {	
					// Turn into qualified class name.
					name = name.replace("/", ".").replace(".class","");
					// Load the class.
					Class<?> c = classLoader.loadClass(name);
					classes.put(name, c);
					System.out.println("Loaded class: " + name);
				}
	    	}
			
			System.out.println("Finished loading: " + file.getAbsolutePath());
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}