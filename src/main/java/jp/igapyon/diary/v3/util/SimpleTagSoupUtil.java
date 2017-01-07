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

package jp.igapyon.diary.v3.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.ccil.cowan.tagsoup.XMLWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * TagSoup を利用して HTML を正常化するためのシンプルなサンプル。 TagSoup 1.1 で動作確認。
 * 
 * @author Toshiki Iga
 */
public class SimpleTagSoupUtil {
	public static void main(final String[] args) throws IOException, SAXException {
		final XMLReader parser = new Parser();

		final HTMLSchema schema = new HTMLSchema();
		parser.setProperty(Parser.schemaProperty, schema);

		final StringWriter output = new StringWriter();

		final XMLWriter serializer = new XMLWriter(output);
		parser.setContentHandler(serializer);

		// TODO 確認した時点では、これが期待通りには機能せず。
		// parser.setDTDHandler(serializer);
		// 仕方が無いので、以下の記述により DOCTYPE を強制的に出力させる。(html用)
		// serializer.setOutputProperty(XMLWriter.DOCTYPE_PUBLIC, "-//W3C//DTD
		// HTML 4.01 Transitional//EN");

		// <html> に名前空間をあらわす属性が付かないようにする。
		parser.setFeature(Parser.namespacesFeature, false);

		final InputSource input = new InputSource();
		input.setCharacterStream(new StringReader("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"
				+ "<HTML><head>\n" + "<title>this is title\n"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=Windows-31J\">\n" + "<body>\n"
				+ "<p>aaa<br>bbb<p>あいうAAA&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;BBB      CCC<uso>\n"
				+ "</html>"));

		// 出力を (xhtmlではなく) html にセットします。
		serializer.setOutputProperty(XMLWriter.METHOD, "xhtml");

		// XML宣言の出力を抑制します。
		serializer.setOutputProperty(XMLWriter.OMIT_XML_DECLARATION, "yes");

		// 属性へのデフォルト付与を抑制させます。
		parser.setFeature(Parser.defaultAttributesFeature, false);

		// 出力先の文字エンコーディングを指定します。
		serializer.setOutputProperty(XMLWriter.ENCODING, "Windows-31J");

		// 知らない名前の要素について寛大に処理します。(jsp対策において必要と想定)
		parser.setFeature("http://www.ccil.org/~cowan/tagsoup/features/ignore-bogons", false);

		// パースを実施。
		parser.parse(input);

		System.out.println("HTML正常化結果: [" + output.toString() + "]");
	}

	/**
	 * 与えられた html ファイルを、TagSoup の能力により正規化します。
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public static String formatHtml(final String source) throws IOException, SAXException {
		final XMLReader parser = new Parser();

		final HTMLSchema schema = new HTMLSchema();
		parser.setProperty(Parser.schemaProperty, schema);

		final StringWriter output = new StringWriter();

		final XMLWriter serializer = new XMLWriter(output);
		parser.setContentHandler(serializer);

		// TODO 確認した時点では、これが期待通りには機能せず。
		// parser.setDTDHandler(serializer);
		// 仕方が無いので、以下の記述により DOCTYPE を強制的に出力させる。(html用)
		// serializer.setOutputProperty(XMLWriter.DOCTYPE_PUBLIC, "-//W3C//DTD
		// HTML 4.01 Transitional//EN");

		// <html> に名前空間をあらわす属性が付かないようにする。
		parser.setFeature(Parser.namespacesFeature, false);

		final InputSource input = new InputSource();
		input.setCharacterStream(new StringReader(source));

		// 出力を (xhtmlではなく) html にセットします。
		serializer.setOutputProperty(XMLWriter.METHOD, "xhtml");

		// XML宣言の出力を抑制します。
		serializer.setOutputProperty(XMLWriter.OMIT_XML_DECLARATION, "yes");

		// 属性へのデフォルト付与を抑制させます。
		parser.setFeature(Parser.defaultAttributesFeature, false);

		// 出力先の文字エンコーディングを指定します。
		serializer.setOutputProperty(XMLWriter.ENCODING, "Windows-31J");

		// 知らない名前の要素について寛大に処理します。(jsp対策において必要と想定)
		parser.setFeature("http://www.ccil.org/~cowan/tagsoup/features/ignore-bogons", false);

		// パースを実施。
		parser.parse(input);

		return output.toString();
	}
}