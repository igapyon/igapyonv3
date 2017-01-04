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

package jp.igapyon.diary.v3.html;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

/**
 * 既存 HTML からタイトル一覧を取得します。
 * 
 * 既存 HTML のタイトルが、所定の日記形式テキストから開始されていることが大前提となります。
 * また、ディレクトリ構造が年付きの構造になっていることも重要です。（いがぴょんの日記v2 形式）
 * 
 * @author Toshiki Iga
 */
public class IndexDiaryHtmlParser {
	private List<DiaryItemInfo> diaryItemInfoList = new ArrayList<DiaryItemInfo>();

	private IgapyonV3Settings settings = null;

	public IndexDiaryHtmlParser(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public List<DiaryItemInfo> processDir(final File dir, String path) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return diaryItemInfoList;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file, path + "/" + file.getName());
			} else if (file.isFile()) {
				if (file.getName().startsWith("ig") && file.getName().endsWith(".html")
						&& false == file.getName().endsWith(".src.html")
						&& false == file.getName().endsWith("-orig.html")) {
					processFile(file, path);
				}
			}
		}

		Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator());

		return diaryItemInfoList;
	}

	void processFile(final File file, final String path) throws IOException {
		String source = FileUtils.readFileToString(file, "UTF-8");
		try {
			source = SimpleTagSoupUtil.formatHtml(source);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String title = "N/A";
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			final Document document = documentBuilder.parse(new InputSource(new StringReader(source)));

			final XPath xpath = XPathFactory.newInstance().newXPath();

			title = (String) xpath.evaluate("/html/head/title/text()", document, XPathConstants.STRING);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("html:" + source);
			throw new IllegalArgumentException("BREAK!");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			// タイトル先頭部の日付形式をハイフンに変換
			String titleDate = title.substring(0, 10);
			titleDate = StringUtils.replaceChars(titleDate, "/", "-");
			title = titleDate + title.substring(10);
		}

		final String url = "https://igapyon.github.io/diary" + path + "/" + file.getName();

		final DiaryItemInfo diaryItemInfo = new DiaryItemInfo();
		diaryItemInfo.setUri(url);

		diaryItemInfo.setTitle(title);

		diaryItemInfoList.add(diaryItemInfo);
	}
}
