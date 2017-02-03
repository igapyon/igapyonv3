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

package jp.igapyon.diary.igapyonv3.migration.hatena2md;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.igapyon.diary.igapyonv3.item.DiaryItemInfo;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.util.IgXmlUtil;

/**
 * Migration tool. Convert Hatena exported XML into separated text files.
 * 
 * Basically, this is for migration, run at once. This has no regards for
 * re-run.
 * 
 * @author Toshiki Iga
 */
public class HatenaXml2SeparatedTextConverter {
	private IgapyonV3Settings settings = null;

	public HatenaXml2SeparatedTextConverter(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void processFile(final File sourceXml, final File targetMdDir) throws IOException {
		String inputXmlString = FileUtils.readFileToString(sourceXml, "UTF-8");
		inputXmlString = inputXmlString.replace('\u001c', '−');
		inputXmlString = inputXmlString.replace('\u001a', '?');
		final Element rootElement = IgXmlUtil.stringToElement(inputXmlString);

		final List<DiaryItemInfo> diaryItemList = parseRoot(rootElement);
		for (DiaryItemInfo item : diaryItemList) {
			System.out.println(item.getTitle() /* + ":" + item.getBody() */);
			final String yyyymmdd = item.getTitle().substring(0, 4) + item.getTitle().substring(5, 7)
					+ item.getTitle().substring(8, 10);
			final File yearDir = new File(targetMdDir, yyyymmdd.substring(0, 4));
			yearDir.mkdirs();
			final File targetFile = new File(yearDir, "ig" + yyyymmdd.substring(2) + ".src.hatenadiary");
			FileUtils.write(targetFile, item.getTitle() + "\n" + item.getBody(), "UTF-8");
		}
	}

	public static List<DiaryItemInfo> parseRoot(final Element rootElement) throws IOException {
		final List<DiaryItemInfo> result = new ArrayList<DiaryItemInfo>();
		final NodeList nodeList = rootElement.getElementsByTagName("day");
		for (int index = 0; index < nodeList.getLength(); index++) {
			final Element look = (Element) nodeList.item(index);
			result.add(parseDay(look));
		}
		return result;
	}

	public static DiaryItemInfo parseDay(final Element dayElement) throws IOException {
		final DiaryItemInfo item = new DiaryItemInfo();

		String dateString = dayElement.getAttribute("date");
		item.setTitle(dateString + " " + dayElement.getAttribute("title"));

		final NodeList nodeList = dayElement.getElementsByTagName("body");
		for (int index = 0; index < nodeList.getLength(); index++) {
			final Element look = (Element) nodeList.item(index);
			item.setBody((item.getBody() == null ? "" : item.getBody()) + look.getTextContent());
		}

		// Drop comments.

		return item;
	}

	/**
	 * Entry point for this class.
	 * 
	 * @param args
	 *            args.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static final void main(final String[] args) throws IOException {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		new HatenaXml2SeparatedTextConverter(settings).processFile(new File("/tmp/igapyon.xml"), new File("."));
	}
}
