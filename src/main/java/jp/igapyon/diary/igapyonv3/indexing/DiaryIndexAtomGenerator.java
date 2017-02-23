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

package jp.igapyon.diary.igapyonv3.indexing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.igapyon.diary.igapyonv3.item.DiaryItemInfo;
import jp.igapyon.diary.igapyonv3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.igapyonv3.parser.IgapyonHtmlV2TitleParser;
import jp.igapyon.diary.igapyonv3.parser.IgapyonMdTitleParser;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.SimpleDirParser;
import jp.igapyon.diary.igapyonv3.util.SimpleRomeUtil;

/**
 * 日記で利用するインデックスを生成するクラスです。
 * 
 * @author Toshiki Iga
 */
public class DiaryIndexAtomGenerator {
	private IgapyonV3Settings settings = null;

	public DiaryIndexAtomGenerator(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void process() throws IOException {
		// ルートディレクトリ用
		{
			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files.");
			final List<DiaryItemInfo> diaryItemInfoList = new IgapyonMdTitleParser(settings, "ig")
					.processDir(settings.getRootdir(), "");

			// FIXME disabled html parser.
			// System.err.println("Listing html files.");
			// final List<DiaryItemInfo> diaryItemInfoHtmlList = new
			// IgapyonHtmlV2TitleParser(settings)
			// .processDir(settings.getRootdir(), "");
			// diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(settings.getRootdir(), "atom.xml"),
					settings.getSiteTitle() + " all", settings);

			{
				int diaryListupCount = 15;

				final List<DiaryItemInfo> recentItemInfoList = new ArrayList<DiaryItemInfo>();

				for (DiaryItemInfo itemInfo : diaryItemInfoList) {
					diaryListupCount--;
					recentItemInfoList.add(itemInfo);
					if (diaryListupCount <= 0) {
						break;
					}
				}

				// sort them
				Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

				SimpleRomeUtil.itemList2AtomXml(recentItemInfoList, new File(settings.getRootdir(), "atomRecent.xml"),
						settings.getSiteTitle() + " recent", settings);
			}
		}

		final SimpleDirParser parser = new SimpleDirParser() {
			final Pattern pat = Pattern.compile("^[0-9][0-9][0-9][0-9]$");

			@Override
			public boolean isProcessTarget(final File file) {
				if (file.isDirectory() == false) {
					return false;
				}
				final Matcher mat = pat.matcher(file.getName());
				if (mat.find()) {
					return true;
				}

				return false;
			}
		};
		final List<File> files = parser.listFiles(settings.getRootdir(), false);

		for (File file : files) {
			final String year = file.getName();
			// 各年ディレクトリ用

			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files for :" + year);
			final List<DiaryItemInfo> diaryItemInfoList = new IgapyonMdTitleParser(settings, "ig")
					.processDir(new File(settings.getRootdir(), year), "/" + year);

			// FIXME disabled html parser.
			// System.err.println("Listing html files for :" + year);
			// final List<DiaryItemInfo> diaryItemInfoHtmlList = new
			// IgapyonHtmlV2TitleParser(settings)
			// .processDir(new File(settings.getRootdir(), year), "/" + year);
			// diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(settings.getRootdir(), year + "/atom.xml"),
					settings.getSiteTitle() + " year " + year, settings);
		}

		{
			// memo dir
			final List<DiaryItemInfo> diaryItemInfoList = new IgapyonMdTitleParser(settings, "memo")
					.processDir(new File(settings.getRootdir(), "memo"), "/memo");

			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IgapyonHtmlV2TitleParser(settings)
					.processDir(new File(settings.getRootdir(), "memo"), "/memo");
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			final File fileAtom = new File(settings.getRootdir(), "memo" + "/atom.xml");
			if (fileAtom.exists() == false) {
				System.err.println("Atom file [" + fileAtom.getCanonicalPath() + "] not found.");
			} else {
				SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, fileAtom, settings.getSiteTitle() + " memo",
						settings);
			}
		}

		{
			// keyword dir
			final List<DiaryItemInfo> diaryItemInfoList = new IgapyonMdTitleParser(settings, "")
					.processDir(new File(settings.getRootdir(), "keyword"), "/keyword");

			// タイトルでソートします。
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			final File fileAtom = new File(settings.getRootdir(), "keyword" + "/atom.xml");
			if (fileAtom.exists() == false) {
				System.err.println("Atom file [" + fileAtom.getCanonicalPath() + "] not found.");
			} else {
				SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, fileAtom, settings.getSiteTitle() + " keyword",
						settings);
			}
		}
	}
}
