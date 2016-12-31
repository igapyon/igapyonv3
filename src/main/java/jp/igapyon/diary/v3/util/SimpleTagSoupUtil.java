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