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

package jp.igapyon.diary.v3.parser;

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
 * 既存 HTML (いがぴょんの日記v2形式) からタイトル一覧を取得します。
 * 
 * 既存 HTML のタイトルが、所定の日記形式テキストから開始されていることが大前提となります。
 * また、ディレクトリ構造が年付きの構造になっていることも重要です。（いがぴょんの日記v2 形式）
 * 
 * 処理の過程で XPath をもちいてタイトル要素のテキストを取得します。
 * 
 * @author Toshiki Iga
 */
public class IgapyonHtmlV2TitleParser {
	private List<DiaryItemInfo> diaryItemInfoList = new ArrayList<DiaryItemInfo>();

	/**
	 * 日記エンジン用設定。現時点では利用していません。
	 */
	private IgapyonV3Settings settings = null;

	/**
	 * コンストラクタ。
	 * 
	 * @param settings
	 */
	public IgapyonHtmlV2TitleParser(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	/**
	 * 指定ディレクトリを処理します。
	 * 
	 * @param dir
	 * @param path
	 *            /2017, /2016 など
	 * @return
	 * @throws IOException
	 */
	public List<DiaryItemInfo> processDir(final File dir, final String path) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return diaryItemInfoList;
		}

		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file, path + "/" + file.getName());
			} else if (file.isFile()) {
				if (isTargetFile(file.getName())) {
					processFile(file, path);
				}
			}
		}

		Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

		return diaryItemInfoList;
	}

	/**
	 * 各ファイルを処理します。
	 * 
	 * @param file
	 * @param path
	 *            /2017, /2016 など
	 * @throws IOException
	 */
	void processFile(final File file, final String path) throws IOException {
		String source = FileUtils.readFileToString(file, "UTF-8");
		try {
			source = SimpleTagSoupUtil.formatHtml(source);
		} catch (SAXException e) {
			throw new IOException(e);
		}

		final String title = getTitleString(source);
		final String url = settings.getBaseurl() + path + "/" + file.getName();

		final DiaryItemInfo diaryItemInfo = new DiaryItemInfo();
		diaryItemInfo.setUri(url);
		diaryItemInfo.setTitle(title);

		diaryItemInfoList.add(diaryItemInfo);
	}

	/**
	 * 処理対象のファイルかどうかをファイル名から判定します。
	 * 
	 * @param fileName
	 * @return
	 */
	boolean isTargetFile(final String fileName) {
		return (fileName.startsWith("ig") && fileName.endsWith(".html") && false == fileName.endsWith(".src.html")
				&& false == fileName.endsWith("-orig.html"));
	}

	/**
	 * 与えられた html から title のテキストを取得します。そしてそれを v3 形式に加工します。
	 * 
	 * XPath をもちいて、ソースからタイトルを取得します。このソースはあらかじめ xml 妥当になっている必要があります。
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	String getTitleString(final String source) throws IOException {
		String title = "N/A";
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			final Document document = documentBuilder.parse(new InputSource(new StringReader(source)));

			final XPath xpath = XPathFactory.newInstance().newXPath();

			// XPath をもちいてタイトル要素のテキストを取得します。
			title = (String) xpath.evaluate("/html/head/title/text()", document, XPathConstants.STRING);
			if (title==null) {
				title = "<title> not found.";
			}
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		} catch (XPathExpressionException e) {
			throw new IOException(e);
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("html:" + source);
			throw new IOException("BREAK!");
		}

		{
			// タイトル先頭部の日付形式をハイフンに変換
			// これは、いがぴょんの日記v2 形式のものを v3 形式で利用している ISO 形式に変換する処理に当たる。
			String titleDate = title.substring(0, Math.min(10, title.length()));
			titleDate = StringUtils.replaceChars(titleDate, "/", "-");
			title = titleDate + title.substring(10);
		}

		return title;
	}
}
