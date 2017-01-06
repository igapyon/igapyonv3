package jp.igapyon.diary.v3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;

public class SimpleRomeUtil {
	public static String atomxml2String(final File atomXmlFile) throws IOException {
		String indexmdText = "";
		if (atomXmlFile.exists() == false) {
			return "";
		}

		try {
			final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(atomXmlFile)));

			for (Object lookup : synFeed.getEntries()) {
				final SyndEntry entry = (SyndEntry) lookup;
				indexmdText += "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "](" + entry.getLink() + ")\n";
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	public static String atomxml2String(final URL atomURL, final int maxcount) throws IOException {
		String indexmdText = "";
		try {
			final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(atomURL));

			// FIXME File 版と挙動が異なります。いつか直します。

			indexmdText += "#### [" + StringEscapeUtils.escapeXml11(synFeed.getTitle()) + "](" + synFeed.getLink()
					+ ")\n\n";

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			int count = 0;
			for (Object lookup : synFeed.getEntries()) {
				if (count++ >= maxcount) {
					break;
				}

				final SyndEntry entry = (SyndEntry) lookup;
				indexmdText += "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "](" + entry.getLink() + ") "
						+ sdf.format(entry.getUpdatedDate()) + "\n";
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	public static void itemList2AtomXml(final List<DiaryItemInfo> diaryItemInfoList, final File targetAtomFile,
			final String title) throws IOException {
		final SyndFeed feed = new SyndFeedImpl();
		feed.setTitle(title);
		// FIXME should be variable.
		feed.setAuthor("Toshiki Iga");
		feed.setEncoding("UTF-8");
		feed.setGenerator("https://github.com/igapyon/igapyonv3");
		feed.setLanguage("ja_JP");
		feed.setFeedType("atom_1.0");

		// sort desc order
		Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

		for (DiaryItemInfo diaryItemInfo : diaryItemInfoList) {
			final SyndEntry entry = new SyndEntryImpl();
			entry.setTitle(diaryItemInfo.getTitle());
			entry.setUri(diaryItemInfo.getUri());
			entry.setLink(diaryItemInfo.getUri());
			entry.setAuthor("Toshiki Iga");
			feed.getEntries().add(entry);
		}

		try {
			new SyndFeedOutput().output(feed, targetAtomFile);
		} catch (FeedException e) {
			throw new IOException(e);
		}

	}
}
