package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
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
			actualResourceName = resourceName.substring(0, matLocale.start())
					+ resourceName.substring(matLocale.end() - 1);
		}

		final File actualFile = new File(actualResourceName);
		String load = FileUtils.readFileToString(actualFile, "UTF-8");

		if (actualFile.getName().startsWith("ig")) {
			String year1 = "20";
			String year2 = actualFile.getName().substring(2, 4);
			if (year2.startsWith("9")) {
				year1 = "19";
			}

			String month = actualFile.getName().substring(4, 6);
			String day = actualFile.getName().substring(6, 8);

			final List<String> lines = FileUtils.readLines(actualFile, "UTF-8");
			String firstH2Line = null;
			for (String line : lines) {
				if (firstH2Line == null) {
					// 最初の ## からテキストを取得。
					if (line.startsWith("## ")) {
						firstH2Line = line.substring(3);
					}
				}
			}

			String header = "[top](https://igapyon.github.io/diary/) \n";
			header += " / [index](https://igapyon.github.io/diary/" + year1 + year2 + "/index.html) \n";
			header += " / prev \n";
			header += " / next \n";
			header += " / [target](https://igapyon.github.io/diary/" + year1 + year2 + "/ig" + year2 + month + day
					+ ".html) \n";
			header += " / [source](https://github.com/igapyon/diary/blob/gh-pages/" + year1 + year2 + "/ig" + year2
					+ month + day + ".html.src.md) \n";
			header += "\n";

			// ヘッダ追加
			header += (year1 + year2 + "-" + month + "-" + day + " diary: " + firstH2Line + "\n");
			header += "=====================================================================================================\n";
			header += "[![いがぴょん画像(小)](https://igapyon.github.io/diary/images/iga200306s.jpg \"いがぴょん\")](https://igapyon.github.io/diary/memo/memoigapyon.html) 日記形式でつづる [いがぴょん](https://igapyon.github.io/diary/memo/memoigapyon.html)コラム ウェブページです。\n";
			header += "\n";

			load = header + load;

			// フッタ追加
			String footer = "";
			if (load.endsWith("\n") || load.endsWith("\r")) {
				// do nothing.
			} else {
				footer += "\n";
			}
			footer += "\n";
			footer += "----------------------------------------------------------------------------------------------------\n";
			footer += "\n";
			footer += "## この日記について\n";
			footer += "[いがぴょんについて](https://igapyon.github.io/diary/memo/memoigapyon.html) / [インデックスに戻る](https://igapyon.github.io/diary/idxall.html)\n";

			load += footer;
		}

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
