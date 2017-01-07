/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.util;

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

/**
 * Basic XML static utils.
 * 
 * @author Toshiki Iga
 */
public class IgapyonXmlUtil {
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
