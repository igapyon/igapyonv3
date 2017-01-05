package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import freemarker.cache.TemplateLoader;

public class IgapyonV3TemplateLoader implements TemplateLoader {
	// set my custom template loader.

	protected Map<String, String> resourceMap = new HashMap<String, String>();

	@Override
	public Object findTemplateSource(final String resourceName) throws IOException {
		String actualResourceName = resourceName;

		// test/data/hatena/ig161227.html.src_ja_JP.md

		// for case below: config.setLocalizedLookup(true);
		final Pattern patLocale = Pattern.compile("[_]..[_]..\\.");
		final Matcher matLocale = patLocale.matcher(resourceName);

		if (matLocale.find()) {
		//	final String locale = matLocale.group();
	//		System.out.println("locale:" + locale);
			actualResourceName = resourceName.substring(0, matLocale.start())
					+ resourceName.substring(matLocale.end() - 1);
//			System.out.println("res:" + actualResourceName);
		}

		final String load = FileUtils.readFileToString(new File(actualResourceName), "UTF-8");

		resourceMap.put(resourceName, load);
		return resourceName;
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) throws IOException {
		return new StringReader(resourceMap.get(templateSource));
	}

	@Override
	public void closeTemplateSource(final Object templateSource) throws IOException {
		resourceMap.remove(templateSource);
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return System.currentTimeMillis();
	}
}
