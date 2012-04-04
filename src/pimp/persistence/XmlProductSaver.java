package pimp.persistence;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pimp.productdefs.Product;

public class XmlProductSaver extends ProductSaver {
	
	private Document xml;
	private String filePath;

	public XmlProductSaver(String xmlFilePath) {
		try {
			this.filePath = xmlFilePath;
			File xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			if (xmlFile.exists()) {
				xml = db.parse(xmlFile);
			} else {
				xml = db.newDocument();
				xml.appendChild(xml.createElement("Products"));	//Create root node
			}
			
		} catch (IOException ioe) {
			System.out.println("Cannot find the file " + xmlFilePath);
		} catch (Exception e) {
			System.out.println("Problem parsing file " + xmlFilePath);
		}
	}
	
	private boolean writeToXml() {
		try { 
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			DOMSource src = new DOMSource(xml);
			StreamResult result = new StreamResult(new File(filePath));
			
			t.transform(src, result);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean save(Product prod) {
		List<Method> getters = getGetters(prod.getClass());
		Element root = xml.getDocumentElement();
		
		Element productElement = xml.createElement("Product");
		root.appendChild(productElement);
		
		for (Method method : getters) {
			Element objectAttributeElement = xml.createElement(method.getName().substring(3));
			String value = "";
			try {
				value = method.invoke(prod, new Object[]{}).toString();
			} catch (Exception e) {
				System.out.println("Error retrieving object field");
				return false;
			}
			
			objectAttributeElement.setTextContent(value);
			productElement.appendChild(objectAttributeElement);
		}
		
		return writeToXml();
	}

}
