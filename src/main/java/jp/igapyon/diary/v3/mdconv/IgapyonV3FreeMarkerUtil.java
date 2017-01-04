package jp.igapyon.diary.v3.mdconv;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class IgapyonV3FreeMarkerUtil {
	public static void main(String[] args) throws IOException {
		IgapyonV3FreeMarkerUtil.process();
	}

	public static void process() throws IOException {
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setLogTemplateExceptions(false);

		// set my custom template loader.
		config.setTemplateLoader(new IgapyonV3TemplateLoader());

		final Map<String, String> templateData = new HashMap<String, String>();
		templateData.put("user", "Taro Yamada");

		final Template templateBase = config.getTemplate("basic");
		try {
			templateBase.process(templateData, new OutputStreamWriter(System.out));
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}
}
