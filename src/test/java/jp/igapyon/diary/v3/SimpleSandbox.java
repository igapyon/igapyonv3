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
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.diary.v3.html2md.IgapyonV2Html2MdParser;
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
}
