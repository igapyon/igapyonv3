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
package jp.igapyon.diary.v3.converter.hatenadiary;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jp.igapyon.diary.v3.util.IgapyonV3Util;
import jp.igapyon.util.IgapyonXmlUtil;

import org.w3c.dom.Element;

/**
 * Convert HatenaDiary xml to md files.
 */
public class IgapyonHatenaDiary2Md {
	public static void main(final String[] args) throws IOException {
		new IgapyonHatenaDiary2Md().processFile(new File(
				"./test/data/src/hatena/diary/001/test901.xml"), new File(
				"./test/data/output/hatena/diary/001/"));
	}

	public void processFile(final File sourceXml, final File targetMdDir)
			throws IOException {

		final String inputXmlString = IgapyonV3Util.readTextFile(sourceXml);
		final Element rootElement = IgapyonXmlUtil
				.stringToElement(inputXmlString);

		final List<IgapyonHatenaDiaryItem> diaryItemList = IgapyonHatenaDiaryXmlUtil
				.parseRoot(rootElement);
		for (IgapyonHatenaDiaryItem item : diaryItemList) {
			System.out.println(item.getString());
		}

		String outputHtmlWriter = "";

		// if (IgapyonV3Util.checkWriteNecessary("hatenadiary2md",
		// outputHtmlWriter.toString(), targetHtml) == false) {
		// // no need to write
		// return;
		// }
		//
		// IgapyonV3Util.writeHtmlFile(outputHtmlWriter.toString(), targetHtml);
	}
}
