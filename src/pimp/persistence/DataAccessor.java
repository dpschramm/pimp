package pimp.persistence;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import pimp.productdefs.*;

public class DataAccessor {
	private static DataAccessor instance = null;
	private Document xml;
	private File xmlFile;
	
	private DataAccessor(String xmlFilePath) {
		try {
			this.xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			if (this.xmlFile.exists()) {
				this.xml = db.parse(this.xmlFile);
			} else {
				this.xml = db.newDocument();
				this.xml.appendChild(xml.createElement("Products"));	//Create root node
			}
			
		} catch (IOException ioe) {
			System.out.println("Cannot find the file " + xmlFilePath);
		} catch (Exception e) {
			System.out.println("Problem parsing file " + xmlFilePath);
			e.printStackTrace();
		}
	}
	
	public static void initialise(String filePath) {
		if (instance == null) {
			instance = new DataAccessor(filePath);
		}
	}
	
	public static List<Product> load() {
		if (instance == null) {
			return null;
		}
		
		
		List<Product> products = new ArrayList<Product>();
		
		Element root = instance.xml.getDocumentElement();
		
		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) { //Could implement an iterator for NodeLists?
			Element element = (Element)nodes.item(i);
			
			try {
				Product product = createProductFromXmlElement(element);
				products.add(product);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return products;
	}
	
	private static Product createProductFromXmlElement(Element element) throws Exception {
		Product product = null;
		
		NodeList nodes = element.getChildNodes();
		Class<?> className = getClassName(element);
		Constructor<?> constructor = className.getConstructor();
		product = (Product)constructor.newInstance();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Element attributeElement = (Element)nodes.item(i);
			String attributeName = attributeElement.getNodeName();
			String attributeType = attributeElement.getAttribute("type");
			
			if (!attributeType.equals("Class")) {
				String rawValue = attributeElement.getTextContent();
				Object value = getValue(attributeType, rawValue);
				
				Field field = className.getField(attributeName);
				field.set(product, value);
			}
		}
		
		return product;
	}
	
	//Must be a better way to do this. Switching on strings is available in java 7, but I don't have that...
	private static Object getValue(String attributeType, String rawValue) throws Exception {
		if (attributeType.equals("String")) {
			return new String(rawValue);
		} else if (attributeType.equals("int")) {
			return new Integer(rawValue);
		} else if (attributeType.equals("double")) {
			return new Double(rawValue);
		} else if (attributeType.equals("Date")) {
			return new Date(rawValue);
		} else if (attributeType.equals("Color")) {
			return new Color(new Integer(rawValue));
		}
		throw new Exception("Unknown attribute type!");
	}

	private static Class<?> getClassName(Element element) throws ClassNotFoundException {
		String className = element.getNodeName();
		
		Class<?> c = Class.forName(className);
		
		return c;
	}
	
	public static boolean save(Product product) {
		if (instance == null) {
			System.err.println("DataAccessor has not been initialised.");
			return false;
		}
		
		Class<?> c = product.getClass();
		Field[] fields = c.getFields();
		
		Element root = instance.xml.getDocumentElement();
		
		Element productElement = instance.xml.createElement(c.getName());
		root.appendChild(productElement);
		
		for (Field field : fields) {
			Element objectAttributeElement = instance.xml.createElement(field.getName());
			objectAttributeElement.setAttribute("type", field.getType().getSimpleName());
			String value = "";
			try {
				value = field.get(product).toString();
			} catch (Exception e) {
				System.out.println("Error retrieving object field");
				return false;
			}
			
			objectAttributeElement.setTextContent(value);
			productElement.appendChild(objectAttributeElement);
		}
		
		return instance.writeXml();
	}
	
	private boolean writeXml() {
		try { 
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource src = new DOMSource(xml);
			FileOutputStream outStream = new FileOutputStream(xmlFile);
			StreamResult result = new StreamResult(outStream);
			
			t.transform(src, result);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
