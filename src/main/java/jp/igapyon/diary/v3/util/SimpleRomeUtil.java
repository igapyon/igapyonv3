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

/**
 * Atom / RSS 読み書きを実現する ROME ライブラリのためのユーティリティクラスです。
 * 
 * @author Toshiki Iga
 */
public class SimpleRomeUtil {
	/**
	 * 与えられた atom.xml ファイルを入力して Markdown テキストを取得します。
	 * 
	 * @param atomXmlFile
	 * @return
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static String atomxml2String(final File atomXmlFile, final File currentDir, final IgapyonV3Settings settings)
			throws IOException {
		String indexmdText = "";
		if (atomXmlFile.exists() == false) {
			return "";
		}

		try {
			final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(atomXmlFile)));

			for (Object lookup : synFeed.getEntries()) {
				final SyndEntry entry = (SyndEntry) lookup;
				indexmdText += "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "]("
						+ SimpleDirUtil.getRelativeUrlIfPossible(entry.getLink(), currentDir, settings) + ")\n";
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	/**
	 * 与えれらた URL から入手できる atom.xml ファイルを入力して、markdown テキストファイルを取得します。
	 * 
	 * @param atomURL
	 * @param maxcount
	 * @return
	 * @throws IOException
	 *             io exception occurs.
	 */
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
						+ (entry.getUpdatedDate() == null ? "" : sdf.format(entry.getUpdatedDate())) + "\n";
			}

			return indexmdText;
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	/**
	 * 与えられたアイテムリストを atom.xml ファイルに書き込みます。
	 * 
	 * ソート済みのものを与えてください。
	 * 
	 * @param diaryItemInfoList
	 * @param targetAtomFile
	 * @param title
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static void itemList2AtomXml(final List<DiaryItemInfo> diaryItemInfoList, final File targetAtomFile,
			final String title, final IgapyonV3Settings settings) throws IOException {
		final SyndFeed feed = new SyndFeedImpl();
		feed.setTitle(title);
		feed.setAuthor(settings.getAuthor());
		feed.setEncoding("UTF-8");
		feed.setGenerator("https://github.com/igapyon/igapyonv3");
		feed.setLanguage(settings.getLanguage());
		feed.setFeedType("atom_1.0");

		for (DiaryItemInfo diaryItemInfo : diaryItemInfoList) {
			final SyndEntry entry = new SyndEntryImpl();
			entry.setTitle(diaryItemInfo.getTitle());
			entry.setUri(diaryItemInfo.getUri());
			entry.setLink(diaryItemInfo.getUri());
			entry.setAuthor(settings.getAuthor());
			feed.getEntries().add(entry);
		}

		try {
			new SyndFeedOutput().output(feed, targetAtomFile);
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}

	public static void entryList2AtomXml(final List<SyndEntry> diaryItemInfoList, final File targetAtomFile,
			final String title, final IgapyonV3Settings settings) throws IOException {
		final SyndFeed feed = new SyndFeedImpl();
		feed.setTitle(title);
		feed.setAuthor(settings.getAuthor());
		feed.setEncoding("UTF-8");
		feed.setGenerator("https://github.com/igapyon/igapyonv3");
		feed.setLanguage(settings.getLanguage());
		feed.setFeedType("atom_1.0");

		// sort desc order
		// TODO Collections.sort(diaryItemInfoList, new
		// DiaryItemInfoComparator(true));

		for (SyndEntry diaryItemInfo : diaryItemInfoList) {
			feed.getEntries().add(diaryItemInfo);
		}

		try {
			new SyndFeedOutput().output(feed, targetAtomFile);
		} catch (FeedException e) {
			throw new IOException(e);
		}
	}
}
