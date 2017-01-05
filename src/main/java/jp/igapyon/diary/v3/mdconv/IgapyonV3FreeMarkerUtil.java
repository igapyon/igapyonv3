package jp.igapyon.diary.v3.mdconv;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.NullOutputStream;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.util.IgapyonFileUtil;

public class IgapyonV3FreeMarkerUtil {

	public static void main(String[] args) throws IOException {
		final Map<String, Object> templateData = new HashMap<String, Object>();
		templateData.put("user", "Taro Yamada");

		// templateData.put("project.build.directory", "wrk");

		{
			DummyVOMvnProject obj = new DummyVOMvnProject();
			templateData.put("project", obj);
			// ${project.build.directory}
		}

		IgapyonV3FreeMarkerUtil.process(new File("."), new File("test/data/hatena/ig161227.html.src.md"), templateData);
	}

	public static void process(File rootdir, File file, final Map<String, Object> templateData) throws IOException {
		// do canonical
		rootdir = rootdir.getCanonicalFile();
		file = file.getCanonicalFile();

		final String relativePath = IgapyonFileUtil.getRelativePath(rootdir, file);

		// newest version at this point.
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// DISABLE auto escape
		config.setAutoEscapingPolicy(Configuration.DISABLE_AUTO_ESCAPING_POLICY);

		// only for camel case not snake one.
		config.setNamingConvention(Configuration.CAMEL_CASE_NAMING_CONVENTION);

		// ??? set API disable
		config.setAPIBuiltinEnabled(true);

		// only for newer one.
		config.setClassicCompatible(false);

		config.setLazyAutoImports(false);

		// diary system not need localize lookup
		config.setLocalizedLookup(false);

		config.setLogTemplateExceptions(false);
		config.setRecognizeStandardFileExtensions(false);
		config.setWhitespaceStripping(false);

		// set my custom template loader.
		config.setTemplateLoader(new IgapyonV3TemplateLoader());

		final Template templateBase = config.getTemplate(relativePath);
		try {
			OutputStreamWriter writer = new OutputStreamWriter(new BufferedOutputStream(new NullOutputStream()),
					"UTF-8");
			templateBase.process(templateData, writer);
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}
}
