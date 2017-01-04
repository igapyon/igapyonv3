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
	public Object findTemplateSource(final String name) throws IOException {
		System.out.println("TemplateName:" + name);

		// test/data/hatena/ig161227.html.src_ja_JP.md

		final Pattern patLocale = Pattern.compile("[_]..[_]..\\.");
		final Matcher matLocale = patLocale.matcher(name);

		if (matLocale.find()) {
			final String locale = matLocale.group();
			System.out.println("locale:" + locale);
		}

		final File file = new File(name);

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
