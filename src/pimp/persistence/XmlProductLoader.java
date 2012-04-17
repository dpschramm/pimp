package pimp.persistence;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import pimp.productdefs.Product;

public class XmlProductLoader extends ProductLoader {

	private String xmlFilePath;
	
	public XmlProductLoader(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
	}
	
	
	@Override
	protected List<Product> load() {
		Document xml = loadXml();
		
		List<Product> products = new ArrayList<Product>();
		
		Element root = xml.getDocumentElement();
		
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
	
	private Document loadXml() {
		Document xml = null;
		try {			
			File xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			if (xmlFile.exists()) {
				xml = db.parse(xmlFile);
			}
		} catch (IOException ioe) {
			System.out.println("Cannot find the file " + xmlFilePath);
		} catch (Exception e) {
			System.out.println("Problem parsing file " + xmlFilePath);
			e.printStackTrace();
		}
		
		return xml;
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
	
	
	private static Class<?> getClassName(Element element) throws ClassNotFoundException {
		String className = element.getNodeName();
		
		Class<?> c = Class.forName(className);
		
		return c;
	}
	
	
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

}
