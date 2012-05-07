package pimp.classloader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.util.jar.JarFile;

/**
 * CloseableClassLoader based on the fix suggested here:
 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4167874
 * 
 * @author Daniel Schramm
 *
 */
public class CloseableClassLoader extends URLClassLoader {
	
	JarURLConnection jarConnection;
	
	/**
	 * Unlike a normal URLClassLoader, this constructor only takes a single 
	 * URL. This must be a URL for a jar file as we create a jarConnection 
	 * from it.
	 * 
	 * @param jarUrl a URL to a jar file.
	 */
	public CloseableClassLoader(URL jarUrl) {
	    super(new URL[] {jarUrl});
	    
		try {
			System.out.println("Loading jar file: " + jarUrl.getFile());	
			String jarString = "jar:file:" + jarUrl.getFile() + "!/";
			URL url = new URL(jarString);
			jarConnection = (JarURLConnection) url.openConnection();
			jarConnection.setUseCaches(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (jarConnection != null) {
			try {
				JarFile jarFile = jarConnection.getJarFile();
				jarFile.close();
				System.out.println("Closed jar file: " + jarFile.getName());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			jarConnection = null;
		}
	}
}
