package jp.igapyon.diary.v3;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

public class SimpleSandbox {
	@Test
	public void test() throws Exception {
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setLogTemplateExceptions(false);

		// set my custom template loader.
		config.setTemplateLoader(new TemplateLoader() {
			private long lastModified = System.currentTimeMillis();

			private static final String MY_TEMPLATE_STRING = "My name is ${user} desu.";

			@Override
			public Object findTemplateSource(final String name) throws IOException {
				System.out.println("TemplateName:" + name);
				return MY_TEMPLATE_STRING;
			}

			@Override
			public Reader getReader(final Object templateSource, final String encoding) throws IOException {
				return new StringReader(MY_TEMPLATE_STRING);
			}

			@Override
			public void closeTemplateSource(final Object templateSource) throws IOException {
				// do nothing.
			}

			@Override
			public long getLastModified(final Object templateSource) {
				return lastModified;
			}
		});

		final Map<String, String> templateData = new HashMap<String, String>();
		templateData.put("user", "Taro Yamada");

		final Template templateBase = config.getTemplate("basic");
		templateBase.process(templateData, new OutputStreamWriter(System.out));
	}

	@Test
	public void test2() throws Exception {
		String source = FileUtils.readFileToString(new File("./test/data/v2html/ig100102.html"), "UTF-8");
		try {
			source = SimpleTagSoupUtil.formatHtml(source);
			// System.out.println(source);

			final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			final SAXParser parser = saxFactory.newSAXParser();
			parser.parse(new InputSource(new StringReader(source)), new MyHandler());

		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	class MyHandler extends DefaultHandler {
		protected StringBuilder markdownBuffer = new StringBuilder();

		protected StringBuilder charactersBuffer = new StringBuilder();

		protected boolean isInV2TdTitleMarker = false;

		/**
		 * インデックスに戻る以降のところがボディ。
		 */
		protected boolean isContentBody = false;

		protected String recentHrefString = null;

		@Override
		public void endDocument() throws SAXException {
			System.out.println("Markdown:");
			System.out.println(markdownBuffer.toString().trim());
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
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
					System.out.println(
							"  " + attributes.getQName(indexAttr) + "=\"" + attributes.getValue(indexAttr) + "\"");
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
}
