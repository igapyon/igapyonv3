/**************************************************************************
 * Copyright (c) 2015, Toshiki Iga, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************** */
/**************************************************************************
 * Copyright 2015 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *********************************************************************** */
package jp.igapyon.util;

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

public class IgapyonXmlUtil {
	/**
	 * Convert XML string to Element.
	 * 
	 * @param inputXml
	 *            XML string.
	 * @return Element
	 */
	public static Element stringToElement(final String inputXml) {
		try {
			final DocumentBuilder builder = DocumentBuilderFactory
					.newInstance().newDocumentBuilder();
			final Document document = builder.parse(inputXml);
			return document.getDocumentElement();
		} catch (ParserConfigurationException ex) {
			throw new IllegalArgumentException("XML read exception: "
					+ ex.toString());
		} catch (IOException ex) {
			throw new IllegalArgumentException("XML read exception: "
					+ ex.toString());
		} catch (SAXException ex) {
			throw new IllegalArgumentException("XML read exception: "
					+ ex.toString());
		}
	}

	/**
	 * Convert Element to XML string.
	 * 
	 * @param rootElement
	 *            Element.
	 * @return XML string.
	 */
	public static String elementToString(final Element rootElement) {
		try {
			final StringWriter writer = new StringWriter();
			final Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			final DOMSource source = new DOMSource(rootElement);
			final StreamResult target = new StreamResult(writer);
			transformer.transform(source, target);
			writer.flush();
			return writer.toString();
		} catch (TransformerConfigurationException ex) {
			throw new IllegalArgumentException("XML write exception: "
					+ ex.toString());
		} catch (TransformerException ex) {
			throw new IllegalArgumentException("XML write exception: "
					+ ex.toString());
		}
	}
}
