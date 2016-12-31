package jp.igapyon.diary.v3.hatena;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class SimpleXmlUtil {
	/**
	 * Convert XML string to Element.
	 * 
	 * @param inputXml
	 *            XML string.
	 * @return Element
	 */
	public static Element stringToElement(final String inputXml) throws IOException {
		try {
			final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			final Document document = builder.parse(new ByteArrayInputStream(inputXml.getBytes("UTF-8")));
			return document.getDocumentElement();
		} catch (ParserConfigurationException ex) {
			throw new IOException("IgapyonXmlUtil#stringToElement: Fail to configure xml parser: ", ex);
		} catch (SAXException ex) {
			throw new IOException("IgapyonXmlUtil#stringToElement: Fail to process xml: ", ex);
		}
	}

	/**
	 * Convert Element to XML string.
	 * 
	 * @param rootElement
	 *            Element.
	 * @return XML string.
	 */
	public static String elementToString(final Element rootElement) throws IOException {
		try {
			final StringWriter writer = new StringWriter();
			final Transformer transformer = TransformerFactory.newInstance().newTransformer();
			final DOMSource source = new DOMSource(rootElement);
			final StreamResult target = new StreamResult(writer);
			transformer.transform(source, target);
			writer.flush();
			return writer.toString();
		} catch (TransformerConfigurationException ex) {
			throw new IOException("IgapyonXmlUtil#elementToString: Fail to configure xml transformer: ", ex);
		} catch (TransformerException ex) {
			throw new IOException("IgapyonXmlUtil#elementToString: Fail to transform xml: ", ex);
		}
	}
}