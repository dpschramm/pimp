package pimp.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

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
	private File xmlFile;
	
	
	public XmlProductSaver(String xmlFilePath) {
		try {
			this.xmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
		
			//Overwrite existing xml each time we create a saver.
			this.xml = db.newDocument();
			this.xml.appendChild(xml.createElement("Products"));	//Create root node
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		

	@Override
	protected boolean save(Product product) {
		Class<?> c = product.getClass();
		Field[] fields = c.getFields();
		
		Element root = xml.getDocumentElement();
		
		Element productElement = xml.createElement(c.getName());
		root.appendChild(productElement);
		
		for (Field field : fields) {
			Element objectAttributeElement = xml.createElement(field.getName());
			objectAttributeElement.setAttribute("type", field.getType().getSimpleName());
			String value = "";
			try {
				value = field.get(product).toString();
			} catch (Exception e) {
				System.out.println("Error retrieving object field. May not have been set.");
			}
			
			objectAttributeElement.setTextContent(value);
			productElement.appendChild(objectAttributeElement);
		}
		
		return writeXml();
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
