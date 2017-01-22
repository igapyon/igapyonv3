package jp.igapyon.diary.v3;

import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.diary.v3.mdconv.freemarker.IgapyonV3TemplateLoader;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class SimpleSandbox {
	@Test
	public void test() throws Exception {
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setLogTemplateExceptions(false);

		// set my custom template loader.
		config.setTemplateLoader(new IgapyonV3TemplateLoader(new IgapyonV3Settings(), true));

		final Map<String, String> templateData = new HashMap<String, String>();
		templateData.put("user", "Taro Yamada");

		final Template templateBase = config.getTemplate("basic");
		templateBase.process(templateData, new OutputStreamWriter(System.out));
	}
}
