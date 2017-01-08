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

package jp.igapyon.diary.v3.indexing.title;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.io.FileUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleRomeUtil;

/**
 * 日記タイトルからインデックス作成???
 * 
 * 現状、まだ diary 側からのビルドに対応しておらず、このクラスの main からの起動が必要。
 * 
 * @author Toshiki Iga
 */
public class DiaryAtomByTitleKeyGenerator {
	private IgapyonV3Settings settings = null;

	public DiaryAtomByTitleKeyGenerator(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void generateRssByIndex() throws IOException {
		// キーワードのリストを読み込み。
		final Map<String, SyndEntry> keywordEntryMap = new HashMap<String, SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir().getCanonicalPath() + "/keyword", "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					keywordEntryMap.put(entry.getTitle().toLowerCase(), entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		// すべての日記のタイトルを読み込み。
		final List<SyndEntry> diaryEntryList = new ArrayList<SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir(), "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					diaryEntryList.add(entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		final Map<String, List<SyndEntry>> diaryByKeywordMap = new HashMap<String, List<SyndEntry>>();

		// 各タイトルから[]ワードを抽出。
		// これとヒットするキーワードがあれば、atomキーワード物理名.xml を keyword ディレクトリに生成
		for (SyndEntry entry : diaryEntryList) {
			final Pattern pat = Pattern.compile("\\[.*?\\]");
			final Matcher mat = pat.matcher(entry.getTitle());

			for (; mat.find();) {
				// まず、タイトルの [] を読み込み。これは、本文のダブルカッコと同じものと考えて良い。
				String word = mat.group();
				word = word.substring(1, word.length() - 1);
				if (keywordEntryMap.get(word.toLowerCase()) == null) {
					// 日記タイトルの新規のキーワード。さしあたり無視。
				} else {
					// すでに存在するキーワード
					if (diaryByKeywordMap.get(word.toLowerCase()) == null) {
						diaryByKeywordMap.put(word.toLowerCase(), new ArrayList<SyndEntry>());
					}
					diaryByKeywordMap.get(word.toLowerCase()).add(entry);
				}
			}
		}

		for (String key : diaryByKeywordMap.keySet()) {
			// + "atomTitleKey-" TODO???

			final List<SyndEntry> entryList = diaryByKeywordMap.get(key);

			final File dirAtom = new File(settings.getRootdir().getCanonicalPath() + "/keyword/atom");
			if (dirAtom.exists() == false) {
				dirAtom.mkdirs();
			}

			final File atomFile = new File(dirAtom, "maven.xml");

			System.out.println("AAA:" + atomFile.getAbsolutePath());
			SimpleRomeUtil.entryList2AtomXml(entryList, atomFile, "key", settings);
		}
	}

	/**
	 * 日記タイトルに新規の [キーワード] が発見されたら、それに対応するキーワードファイルを作成します。
	 * 
	 * @throws IOException
	 */
	public void generateNewKeyword() throws IOException {
		// キーワードのリストを読み込み。
		final Map<String, SyndEntry> keywordEntryMap = new HashMap<String, SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir().getCanonicalPath() + "/keyword", "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					keywordEntryMap.put(entry.getTitle().toLowerCase(), entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		// すべての日記のタイトルを読み込み。
		final List<SyndEntry> diaryEntryList = new ArrayList<SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir(), "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					diaryEntryList.add(entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		// + "atomTitleKey-" TODO???

		// 各タイトルから[]ワードを抽出。
		// これとヒットするキーワードがあれば、atomキーワード物理名.xml を keyword ディレクトリに生成
		for (SyndEntry entry : diaryEntryList) {
			final Pattern pat = Pattern.compile("\\[.*?\\]");
			final Matcher mat = pat.matcher(entry.getTitle());

			for (; mat.find();) {
				// まず、タイトルの [] を読み込み。これは、本文のダブルカッコと同じものと考えて良い。
				String word = mat.group();
				word = word.substring(1, word.length() - 1);
				if (keywordEntryMap.get(word.toLowerCase()) == null) {
					System.out.println("  日記タイトルの新規のキーワードによるファイル新規作成:" + word);

					try {
						final File keywordFile = new File(settings.getRootdir().getCanonicalPath() + "/keyword/"
								+ new URLCodec().encode(word.toLowerCase()) + ".html.src.md");
						if (keywordFile.exists()) {
							continue;
						}

						final List<String> lines = new ArrayList<String>();
						lines.add("[index](https://igapyon.github.io/diary/keyword/index.html)");
						lines.add("");
						lines.add("## " + word + "");
						lines.add("");
						lines.add("[[" + word + "]] は、、、、です。");
						lines.add("");
						lines.add("### URL");
						lines.add("");
						lines.add("* TBD URL");
						lines.add("");
						lines.add("### 特徴");
						lines.add("");
						lines.add("* TBD URL");
						lines.add("");
						lines.add("### まとめ情報");
						lines.add("");
						lines.add("* TBD URL");
						lines.add("* <@linksearch title=\"Search on Igapyon Diary\" word=\"" + word
								+ "\" site=\"https://igapyon.github.io/diary/\" />");
						lines.add("* <@linksearch title=\"Search in Google\" word=\"" + word + "\" />");
						lines.add("* <@linksearch title=\"Search in Twitter\" word=\"" + word
								+ "\" engine=\"twitter\" />");
						lines.add("");

						FileUtils.writeLines(keywordFile, lines);
					} catch (EncoderException e) {
						throw new IOException(e);
					}
				} else {
					// すでに存在するキーワード
				}
			}
		}
	}

	public static void main(final String[] args) throws IOException {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("../diary"));
		// new DiaryAtomByTitleKeyGenerator(settings).generateNewKeyword();

		new DiaryAtomByTitleKeyGenerator(settings).generateRssByIndex();

	}
}
