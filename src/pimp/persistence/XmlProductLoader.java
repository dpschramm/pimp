package pimp.persistence;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pimp.productdefs.Product;

/*
 * Need to reassess ProductLoader class hierarchy once working.
 */
public class XmlProductLoader extends ProductLoader {

	@Override
	public boolean load() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public XmlProductLoader() {
		
	}
	
	public List<Product> loadAllProducts(String xmlFilePath) {
		//TODO: remove code duplication between this and save()
		List<Product> products = new ArrayList<Product>();
		
		try {
			File xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			
			if (xmlFile.exists()) {
				Document xmlDoc = db.parse(xmlFile);
				products = loadFromDocument(xmlDoc);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return products;
	}

	private List<Product> loadFromDocument(Document xmlDoc) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		List<Product> products = new ArrayList<Product>();
		
		Element root = xmlDoc.getDocumentElement();
		
		NodeList nodes = root.getElementsByTagName("Products");
		for (int i = 0; i < nodes.getLength(); i++) { //Could implement an iterator for NodeLists?
			Element element = (Element)nodes.item(i);
			
			Product product = createProductFromXmlElement(element);
			
			products.add(product);
		}
		
		
		return products;
	}

	private Product createProductFromXmlElement(Element element) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Product product = null;
		
		NodeList nodes = element.getChildNodes();
		Class<?> className = getClassName(element);
		Constructor constructor = className.getConstructor();
		product = (Product)constructor.newInstance();
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Element attributeElement = (Element)nodes.item(i);
			String attributeName = attributeElement.getNodeName();
			
			Method getter = getAttributeGetter(className, attributeName);
			getter.invoke(product);
		}
		
		return product;
	}

	private Class<?> getClassName(Element element) throws ClassNotFoundException {
		Element classNameAttribute = (Element)element.getElementsByTagName("Class").item(0);
		String className = classNameAttribute.getTextContent();
		
		Class<?> c = Class.forName(className);
		
		return c;
	}

	//Need to account for getters with same name but diff. return type?
	private Method getAttributeGetter(Class<?> className, String attributeName) {
		Method[] methods = className.getMethods();
		
		for (Method method : methods) {
			if (method.getName().substring(3).equals(attributeName)) {
				return method;
			}
		}
		
		return null;
	}

}
