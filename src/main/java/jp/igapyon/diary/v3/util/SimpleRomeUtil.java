/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

/**
 * 
 * 
 * @author Toshiki Iga
 */
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
