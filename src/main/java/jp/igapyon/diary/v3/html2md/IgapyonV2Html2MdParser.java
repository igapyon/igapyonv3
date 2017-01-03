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

package jp.igapyon.diary.v3.html2md;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * いがぴょんの日記v2 コンテンツ専用の HTML から Markdown へのパーサー。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV2Html2MdParser extends DefaultHandler {
	protected StringBuilder markdownBuffer = new StringBuilder();

	protected StringBuilder charactersBuffer = new StringBuilder();

	protected boolean isInV2TdTitleMarker = false;

	/**
	 * インデックスに戻る以降のところがボディ。
	 */
	protected boolean isContentBody = false;

	protected String recentHrefString = null;

	/**
	 * 処理の結果得られた markdown 文字列を取得します。
	 * 
	 * @return
	 */
	public String getMarkdownString() {
		return markdownBuffer.toString();
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (charactersBuffer.length() > 0) {
			fireCharacters(charactersBuffer.toString());
			charactersBuffer = new StringBuilder();
		}

		if (isContentBody) {
			System.out.println("<" + qName + ">");
		}

		final Map<String, String> attrMap = new HashMap<String, String>();
		for (int indexAttr = 0; indexAttr < attributes.getLength(); indexAttr++) {
			attrMap.put(attributes.getQName(indexAttr), attributes.getValue(indexAttr));

			if (isContentBody) {
				System.out
						.println("  " + attributes.getQName(indexAttr) + "=\"" + attributes.getValue(indexAttr) + "\"");
			}
		}

		if (qName.equals("address")) {
			isContentBody = false;
		} else if (qName.equals("a")) {
			recentHrefString = "" + attrMap.get("href");
		} else if (qName.equals("p")) {
			if (isContentBody) {
				markdownBuffer.append("\n");
			}
		} else if (qName.equals("td")) {
			// System.out.println(attrMap.get("bgcolor"));
			if (attrMap.get("bgcolor") != null && attrMap.get("bgcolor").equals("#ff9900")) {
				isInV2TdTitleMarker = true;
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (charactersBuffer.length() > 0) {
			fireCharacters(charactersBuffer.toString());
			charactersBuffer = new StringBuilder();
		}

		if (qName.equals("a")) {
			recentHrefString = null;
		}
		if (qName.equals("td")) {
			// tdを抜けたら、有無を言わさずoff化。
			isInV2TdTitleMarker = false;
		}

		if (isContentBody) {
			System.out.println("</" + qName + ">");
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		charactersBuffer.append(new String(ch, start, length));
	}

	protected void fireCharacters(final String characters) {

		if (characters.equals("インディックスページへ戻る")) {
			// これ以降がようやく本体。
			isContentBody = true;
			return;
		}

		if (isContentBody == false) {
			return;
		}

		if (isInV2TdTitleMarker) {
			markdownBuffer.append("\n## ");
		}

		if (recentHrefString == null) {
			System.out.println(characters);
			markdownBuffer.append(characters);
		} else {
			markdownBuffer.append("[" + characters + "](" + recentHrefString + ")");
		}

		if (isInV2TdTitleMarker)

		{
			markdownBuffer.append("\n");
		}
	}
}
