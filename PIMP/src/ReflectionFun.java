import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.System.out;

public class ReflectionFun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> classes = null;
		try {
			classes = read("src/productdefs.txt");
			// System.out.println(classes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		JFrame inter = new JFrame();
		JTabbedPane tabbedPane = new JTabbedPane();
		
		for (String className : classes) {
			try {
				Class<?> c = Class.forName(className);
				System.out.println("Class: " + c.getCanonicalName());
				
				Package p = c.getPackage();
				System.out.println("Package: "
						+ (p != null ? p.getName() : "-- No Package --"));

				printMembers(c.getConstructors(), "Constuctors");
				printMembers(c.getDeclaredFields(), "Fields");
				printMembers(c.getMethods(), "Methods");
				printClasses(c);
				
				GUIBuilder gb = new GUIBuilder();
				JPanel fields = gb.createGUI(c.getDeclaredFields());

				tabbedPane.addTab(className, fields);
				

				// production code should handle these exceptions more
				// gracefully
			} catch (ClassNotFoundException x) {
				x.printStackTrace();
			}
		}
		
		inter.add(tabbedPane);
		
		inter.pack();
		inter.setVisible(true);
	}

	private static void printMembers(Member[] mbrs, String s) {
		out.format("%s:%n", s);
		for (Member mbr : mbrs) {
			if (mbr instanceof Field)
				out.format("  %s%n", ((Field) mbr).toGenericString());
			else if (mbr instanceof Constructor)
				out.format("  %s%n", ((Constructor) mbr).toGenericString());
			else if (mbr instanceof Method)
				out.format("  %s%n", ((Method) mbr).toGenericString());
		}
		if (mbrs.length == 0)
			out.format("  -- No %s --%n", s);
		out.format("%n");
	}

	private static void printClasses(Class<?> c) {
		out.format("Classes:%n");
		Class<?>[] clss = c.getClasses();
		for (Class<?> cls : clss)
			out.format("  %s%n", cls.getCanonicalName());
		if (clss.length == 0)
			out.format("  -- No member interfaces, classes, or enums --%n");
		out.format("%n");
	}

	/** Read the contents of the given file. */
	private static List<String> read(String fileName) throws IOException {

		List<String> returnList = new ArrayList<String>();
		Scanner scanner = new Scanner(new FileInputStream(fileName));
		try {
			while (scanner.hasNextLine()) {
				returnList.add(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
		return returnList;
	}

}
