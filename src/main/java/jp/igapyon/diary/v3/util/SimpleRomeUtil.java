package jp.igapyon.diary.v3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringEscapeUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

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
				indexmdText = "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "](" + entry.getLink() + ")\n"
						+ indexmdText;
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	public static String atomxml2String(final URL atomURL) throws IOException {
		String indexmdText = "";
		try {
			final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(atomURL));

			indexmdText += "#### [" + StringEscapeUtils.escapeXml11(synFeed.getTitle()) + "](" + synFeed.getLink()
					+ ")\n\n";

			final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			for (Object lookup : synFeed.getEntries()) {
				final SyndEntry entry = (SyndEntry) lookup;
				indexmdText += "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "](" + entry.getLink() + ") "
						+ sdf.format(entry.getUpdatedDate()) + "\n";
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}
}
