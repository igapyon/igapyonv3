package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.cache.TemplateLoader;

public class IgapyonV3TemplateLoader implements TemplateLoader {
	// set my custom template loader.

	private static final String MY_TEMPLATE_STRING = "My name is ${user} desu.";

	@Override
	public Object findTemplateSource(String resourceName) throws IOException {
		System.out.println("TemplateName:" + resourceName);

		// test/data/hatena/ig161227.html.src_ja_JP.md

		final Pattern patLocale = Pattern.compile("[_]..[_]..\\.");
		final Matcher matLocale = patLocale.matcher(resourceName);

		if (matLocale.find()) {
			final String locale = matLocale.group();
			System.out.println("locale:" + locale);
			resourceName = resourceName.substring(0, matLocale.start()) + resourceName.substring(matLocale.end() - 1);
			System.out.println("res:" + resourceName);
		}

		final File file = new File(resourceName);

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
		return System.currentTimeMillis();
	}
}
