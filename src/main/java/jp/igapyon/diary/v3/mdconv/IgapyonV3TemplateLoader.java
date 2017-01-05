package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import freemarker.cache.TemplateLoader;

public class IgapyonV3TemplateLoader implements TemplateLoader {
	// set my custom template loader.

	protected Map<String, String> resourceMap = new HashMap<String, String>();

	/**
	 * notice static!!! danger
	 */
	protected static List<SyndEntry> synEntryList = null;

	public IgapyonV3TemplateLoader() {
		try {
			ensureLoadAtomXml();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ensureLoadAtomXml() throws IOException {
		// FIXME dot file. should be like rootdir.
		final File atomXmlFile = new File("./atom.xml");
		if (synEntryList == null) {
			synEntryList = new ArrayList<SyndEntry>();
			if (atomXmlFile.exists() == false) {
				return;
			}
			try {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(atomXmlFile)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					synEntryList.add(entry);
				}
			} catch (FeedException e) {
				throw new IOException(e);
			}
		}
	}

	public int findTargetAtomEntry(final String url) throws IOException {
		if (synEntryList == null) {
			return -1;
		}

		for (int index = 0; index < synEntryList.size(); index++) {
			final SyndEntry entry = synEntryList.get(index);
			if (url.equals(entry.getLink())) {
				return index;
			}
		}

		return -1;
	}

	/**
	 * 与えられたリソース名にロケール文字列が含まれている場合がこれを除去します。
	 * 
	 * @param resourceName
	 * @return
	 */
	public static String stripLocaleName(final String resourceName) {
		String actualResourceName = resourceName;

		// 現在の設定では無効化されているが、FreeMarker はデフォルトでロケール付きで以下のように値が渡されてくる。
		// ここからしばらくの処理は、このロケール文字列を除去するための記述に当たる。
		// test/data/hatena/ig161227.html.src_ja_JP.md

		// for case below: config.setLocalizedLookup(true);
		final Pattern patLocale = Pattern.compile("[_]..[_]..\\.");
		final Matcher matLocale = patLocale.matcher(resourceName);

		if (matLocale.find()) {
			// ロケール記述があれば、これを除去。
			actualResourceName = resourceName.substring(0, matLocale.start())
					+ resourceName.substring(matLocale.end() - 1);
		}

		return actualResourceName;
	}

	public static String getFirstH2String(final File file) throws IOException {
		final List<String> lines = FileUtils.readLines(file, "UTF-8");
		String firstH2Line = null;
		for (String line : lines) {
			if (firstH2Line == null) {
				// 最初の ## からテキストを取得。
				if (line.startsWith("## ")) {
					firstH2Line = line.substring(3);
				}
			}
		}
		return firstH2Line;
	}

	public static String getYearListMdString() {
		return "[2017](https://igapyon.github.io/diary/2017/index.html)\n"
				+ "/ [2016](https://igapyon.github.io/diary/2016/index.html)\n"
				+ "/ [2015](https://igapyon.github.io/diary/2015/index.html)\n"
				+ "/ [2014](https://igapyon.github.io/diary/2014/index.html)\n"
				+ "/ [2013](https://igapyon.github.io/diary/2013/index.html)\n"
				+ "/ [2012](https://igapyon.github.io/diary/2012/index.html)\n"
				+ "/ [2011](https://igapyon.github.io/diary/2011/index.html)\n"
				+ "/ [2010](https://igapyon.github.io/diary/2010/index.html)\n"
				+ "/ [2009](https://igapyon.github.io/diary/2009/index.html)\n"
				+ "/ [2008](https://igapyon.github.io/diary/2008/index.html)\n"
				+ "/ [2007](https://igapyon.github.io/diary/2007/index.html)\n"
				+ "/ [2006](https://igapyon.github.io/diary/2006/index.html)\n"
				+ "/ [2005](https://igapyon.github.io/diary/2005/index.html)\n"
				+ "/ [2004](https://igapyon.github.io/diary/2004/index.html)\n"
				+ "/ [2003](https://igapyon.github.io/diary/2003/index.html)\n"
				+ "/ [2002](https://igapyon.github.io/diary/2002/index.html)\n"
				+ "/ [2001](https://igapyon.github.io/diary/2001/index.html)\n"
				+ "/ [2000](https://igapyon.github.io/diary/2000/index.html)\n"
				+ "/ [1998](https://igapyon.github.io/diary/1998/index.html)\n"
				+ "/ [1997](https://igapyon.github.io/diary/1997/index.html)\n"
				+ "/ [1996](https://igapyon.github.io/diary/1996/index.html)\n"
				+ "/ [ALL](https://igapyon.github.io/diary/idxall.html)\n";
	}

	@Override
	public Object findTemplateSource(final String resourceName) throws IOException {
		if ("diaryYearList".equals(resourceName)) {
			return "diaryYearList";
		}

		ensureLoadAtomXml();

		final File actualFile = new File(stripLocaleName(resourceName));
		String load = FileUtils.readFileToString(actualFile, "UTF-8");

		if (actualFile.getName().startsWith("ig")) {
			String year1 = "20";
			String year2 = actualFile.getName().substring(2, 4);
			if (year2.startsWith("9")) {
				year1 = "19";
			}

			String month = actualFile.getName().substring(4, 6);
			String day = actualFile.getName().substring(6, 8);

			final String firstH2Line = getFirstH2String(actualFile);

			final String targetURL = "https://igapyon.github.io/diary/" + year1 + year2 + "/ig" + year2 + month + day
					+ ".html";
			final int entryIndex = findTargetAtomEntry(targetURL);

			String header = "[top](https://igapyon.github.io/diary/) \n";
			header += " / [index](https://igapyon.github.io/diary/" + year1 + year2 + "/index.html) \n";

			if (entryIndex < 0 || entryIndex > synEntryList.size() - 2) {
				header += " / prev \n";
			} else {
				header += " / [prev](" + synEntryList.get(entryIndex + 1).getLink() + ") \n";
			}

			if (entryIndex <= 0) {
				header += " / next \n";
			} else {
				header += " / [next](" + synEntryList.get(entryIndex - 1).getLink() + ") \n";
			}

			header += " / [target](" + targetURL + ") \n";
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
			footer += "[いがぴょんについて](https://igapyon.github.io/diary/memo/memoigapyon.html) / [日記ジェネレータ](https://github.com/igapyon/igapyonv3)\n";

			load += footer;
		} else {
			final String firstH2Line = getFirstH2String(actualFile);

			String header = "[top](https://igapyon.github.io/diary/) \n";
			header += "\n";

			// ヘッダ追加
			header += (firstH2Line + "\n");
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
			footer += "[いがぴょんについて](https://igapyon.github.io/diary/memo/memoigapyon.html) / [日記ジェネレータ](https://github.com/igapyon/igapyonv3)\n";

			load += footer;
		}

		resourceMap.put(resourceName, load);
		return resourceName;
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) throws IOException {
		if ("diaryYearList".equals((String) templateSource)) {
			return new StringReader(getYearListMdString());
		}

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
