package jp.igapyon.diary.v3.mdconv;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import freemarker.cache.TemplateLoader;

public class IgapyonV3TemplateLoader implements TemplateLoader {
	// set my custom template loader.
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
}
