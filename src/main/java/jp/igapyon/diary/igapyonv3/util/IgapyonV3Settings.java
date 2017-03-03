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

package jp.igapyon.diary.igapyonv3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

/**
 * いがぴょんの日記v3 システムのための基本設定クラス。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3Settings {
	/**
	 * 日記のルートディレクトリ。
	 */
	private File rootdir = new File(".");

	/**
	 * md2html の出力ディレクトリ。
	 */
	private File outputhtmldir = new File("./target/html");

	/**
	 * URL of diary base.
	 * 
	 * URL should NOT be ended with '/'
	 */
	private String baseurl = "https://igapyon.github.io/mydiary";

	/**
	 * URL of diary source base.
	 * 
	 * URL should NOT be ended with '/'
	 */
	private String sourcebaseurl = "https://github.com/igapyon/mydiary/blob/gh-pages";

	/**
	 * TODAY! for newly diary.
	 */
	private Date today = null;

	/**
	 * verbose mode for debugging.
	 */
	private boolean verbose = true;

	/**
	 * debug mode for debugging.
	 */
	private boolean debug = false;

	/**
	 * Generate today's diary or not.
	 */
	private boolean generateTodayDiary = true;

	/**
	 * Convert markdown to html
	 */
	private boolean convertMarkdown2Html = true;

	/**
	 * Duplicate fake html.md or not.
	 * 
	 * for gh-pages
	 */
	private boolean duplicateFakeHtmlMd = false;

	/**
	 * Generate keyword srcmd if needed.
	 */
	private boolean generateKeywordIfNeeded = true;

	private List<String[]> doubleKeywordList = new ArrayList<String[]>();

	/**
	 * この日記の作成者。
	 */
	private String author = "Taro Yamada";

	/**
	 * 日記の記述言語。
	 */
	private String language = "ja_JP";

	/**
	 * 日記のタイトル。
	 */
	private String siteTitle = "Watashi's diary";

	/**
	 * Default double keywords.
	 */
	public static final String[][] DEFAULT_DOUBLE_KEYWORDS = { //
			// igapyon's keyword

			// 処理の過程で、keyword 以下のキーワードがしょっぱなに登録されます。
			// FIXME TODO これは、keyword ディレクトリからの自動取り込み反映に実装変更の予定。

			//

			// blanco Framework
			{ "blanco Framework", "https://ja.osdn.net/projects/blancofw/wiki/blancofw" },
			{ "Blanco2g", "https://ja.osdn.net/projects/blancofw/wiki/Blanco2g" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" },
			{ "blancoDb", "http://www.igapyon.jp/blanco/blancodb.html" },
			{ "blancoDbDotNet", "http://www.igapyon.jp/blanco/blancodbdotnet.html" },
			{ "blancoResourceBundle", "http://www.igapyon.jp/blanco/blancoresourcebundle.html" },
			{ "blancoMailCore", "http://www.igapyon.jp/blanco/blancomailcore.html" },
			{ "Chrome", "https://www.google.co.jp/chrome/browser/" },
			{ "Object Pascal", "https://ja.wikipedia.org/wiki/Object_Pascal" },

			// it
			{ "IoT", "https://ja.wikipedia.org/wiki/%E3%83%A2%E3%83%8E%E3%81%AE%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%BC%E3%83%8D%E3%83%83%E3%83%88" },
			{ "AirPrint", "https://support.apple.com/ja-jp/HT201311" }, { "Cordova", "https://cordova.apache.org/" },

			// embarcadero
			{ "RAD Studio", "https://www.embarcadero.com/jp/products/rad-studio" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "InterBase", "https://ja.wikipedia.org/wiki/InterBase" },
			{ "FireUI", "https://www.embarcadero.com/jp/products/rad-studio/fireui" },
			{ "VCL", "https://ja.wikipedia.org/wiki/Visual_Component_Library" },

			// names
			{ "ネコバス", "http://nlab.itmedia.co.jp/nl/articles/1607/15/news147.html" },//
	};

	public IgapyonV3Settings() {
		today = new Date();
	}

	public Date getToday() {
		return today;
	}

	public List<String[]> getDoubleKeywordList() throws IOException {
		if (doubleKeywordList.size() == 0) {
			// keyword ディレクトリをロード。
			// キーワードのリストを読み込み。
			final Map<String, Object> checkMap = new HashMap<String, Object>();
			try {
				final File fileAtom = new File(getRootdir().getCanonicalPath() + "/keyword", "atom.xml");
				if (fileAtom.exists()) {
					final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
					for (Object lookup : synFeed.getEntries()) {
						final SyndEntry entry = (SyndEntry) lookup;
						doubleKeywordList.add(new String[] { entry.getTitle(), entry.getLink() });
						checkMap.put(entry.getTitle().toLowerCase(), entry);
					}
				}
			} catch (FeedException e) {
				throw new IOException(e);
			}

			for (String[] lookup : DEFAULT_DOUBLE_KEYWORDS) {
				doubleKeywordList.add(lookup);
				if (checkMap.get(lookup[0].toLowerCase()) != null) {
					throw new IOException("Duplicate keyword: [" + lookup[0] + "]");
				}
				checkMap.put(lookup[0].toLowerCase(), lookup[1]);
			}

			try {
				final File fileV2Link = new File(getRootdir(), "igapyon-v2-link.xml");
				if (fileV2Link.exists()) {
					System.err.println("Additional keyword linkfile found: " + fileV2Link.getCanonicalPath());
					final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileV2Link)));
					for (Object lookup : synFeed.getEntries()) {
						final SyndEntry entry = (SyndEntry) lookup;
						doubleKeywordList.add(new String[] { entry.getTitle(), entry.getLink() });

						if (checkMap.get(entry.getTitle().toLowerCase()) != null) {
							throw new IOException("Duplicate keyword: [" + entry.getTitle() + "]");
						}

						checkMap.put(entry.getTitle().toLowerCase(), entry);
					}
				}
			} catch (FeedException e) {
				throw new IOException(e);
			}
		}
		return doubleKeywordList;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public File getRootdir() {
		return rootdir;
	}

	public void setRootdir(File rootdir) {
		this.rootdir = rootdir;
	}

	public String getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}

	public String getSourcebaseurl() {
		return sourcebaseurl;
	}

	public void setSourcebaseurl(String sourcebaseurl) {
		this.sourcebaseurl = sourcebaseurl;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isGenerateTodayDiary() {
		return generateTodayDiary;
	}

	public void setGenerateTodayDiary(boolean generateTodayDiary) {
		this.generateTodayDiary = generateTodayDiary;
	}

	public boolean isConvertMarkdown2Html() {
		return convertMarkdown2Html;
	}

	public void setConvertMarkdown2Html(boolean convertMarkdown2Html) {
		this.convertMarkdown2Html = convertMarkdown2Html;
	}

	public boolean isDuplicateFakeHtmlMd() {
		return duplicateFakeHtmlMd;
	}

	public void setDuplicateFakeHtmlMd(boolean duplicateFakeHtmlMd) {
		this.duplicateFakeHtmlMd = duplicateFakeHtmlMd;
	}

	public boolean isGenerateKeywordIfNeeded() {
		return generateKeywordIfNeeded;
	}

	public void setGenerateKeywordIfNeeded(boolean generateKeywordSrcMdIfNeeded) {
		this.generateKeywordIfNeeded = generateKeywordSrcMdIfNeeded;
	}

	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	public File getOutputhtmldir() {
		return outputhtmldir;
	}

	public void setOutputhtmldir(File outputhtmldir) {
		this.outputhtmldir = outputhtmldir;
	}
}
