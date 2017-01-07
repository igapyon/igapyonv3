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

package jp.igapyon.diary.v3.migration.html2md;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

/**
 * 
 * 
 * @author Toshiki Iga
 */
public class IgapyonV2Html2MdUtil {
	public static void convertV2Html2Md(final File origFile) throws IOException {
		final File file = origFile.getCanonicalFile();

		String source = FileUtils.readFileToString(file, "UTF-8");
		if (file.getName().startsWith("memo")) {
			source = FileUtils.readFileToString(file, "Windows-31J");
		}
		try {
			// Normalize
			source = SimpleTagSoupUtil.formatHtml(source);

			final File newFile = new File(file.getParentFile(),
					file.getName().substring(0, file.getName().length() - "-orig.html".length()) + ".html.src.md");
			System.out.println("convert from " + file.getName() + " to " + newFile.getName());

			final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			final SAXParser parser = saxFactory.newSAXParser();

			final IgapyonV2Html2MdParser htmlparser = new IgapyonV2Html2MdParser();

			parser.parse(new InputSource(new StringReader(source)), htmlparser);

			String markdownHeader = "[old-v2](" + file.getName() + ")\n\n";

			FileUtils.writeStringToFile(newFile, markdownHeader + htmlparser.getMarkdownString().trim(), "UTF-8");
		} catch (SAXException e) {
			System.out.println("変換失敗: " + e.toString());
			System.out.println("Formatted html: [" + source + "]");
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
	}
}
