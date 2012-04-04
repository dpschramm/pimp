package pimp;

import java.util.ArrayList;
import java.util.List;

/**
 * This class monitors a folder and return a list of classes that can
 * be created.
 * 
 * @author Daniel Schramm
 *
 */
public class ProductLoader {
	
	private String directory;
	
	/**
	 * 
	 * @param directory	the directory to monitor.
	 */
	public ProductLoader(String directory) {
		this.directory = directory;
	}
	
	/**
	 * 
	 * @return a List of class types in the directory.
	 */
	public List<Class> getClassList() {
		
		List<Class> classList = new ArrayList<Class>();
		
		return classList;
	}
}
