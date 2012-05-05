package pimp.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class DatabaseSelector extends JFileChooser {
	
	public DatabaseSelector() {
		setCurrentDirectory(new File("."));
		setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		// Filter out non database files.
		setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".db") 
					|| f.isDirectory();
			}
			
			public String getDescription() {
				return "DB files";
			};
		});
	}
	
	/**
	 * Opens the database selector window with the specified title.
	 * 
	 * @return the selected file.
	 */
	public File getFile(String title) {	
		int result = showDialog(this.getParent(), title);
		if (result == JFileChooser.APPROVE_OPTION) {
			return getSelectedFile();
		}
		return null;
	}
}
