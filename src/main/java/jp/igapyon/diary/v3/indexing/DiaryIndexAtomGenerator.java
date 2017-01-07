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

package jp.igapyon.diary.v3.indexing;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.v3.parser.IndexDiaryHtmlParser;
import jp.igapyon.diary.v3.parser.IndexDiaryMdParser;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleRomeUtil;

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

	public void process(final File rootdir) throws IOException {
		// ルートディレクトリ用
		{
			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files.");
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings, "ig").processDir(rootdir,
					"");
			System.err.println("Listing html files.");
			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IndexDiaryHtmlParser(settings).processDir(rootdir,
					"");
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, "atom.xml"), "Igapyon Diary v3 all");

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

				SimpleRomeUtil.itemList2AtomXml(recentItemInfoList, new File(rootdir, "atomRecent.xml"),
						"Igapyon Diary v3 recent");
			}
		}

		final String[] YEARS = new String[] { "1996", "1997", "1998", "2000", "2001", "2002", "2003", "2004", "2005",
				"2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017" };

		for (String year : YEARS) {
			// 各年ディレクトリ用

			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files for :" + year);
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings, "ig")
					.processDir(new File(rootdir, year), "/" + year);

			System.err.println("Listing html files for :" + year);
			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IndexDiaryHtmlParser(settings)
					.processDir(new File(rootdir, year), "/" + year);
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, year + "/atom.xml"),
					"Igapyon Diary v3 year " + year);
		}

		{
			// memo dir
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings, "memo")
					.processDir(new File(rootdir, "memo"), "/memo");

			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IndexDiaryHtmlParser(settings)
					.processDir(new File(rootdir, "memo"), "/memo");
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, "memo" + "/atom.xml"),
					"Igapyon Diary v3 memo");
		}

		{
			// keyword dir
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings, "")
					.processDir(new File(rootdir, "keyword"), "/keyword");

			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, "keyword" + "/atom.xml"),
					"Igapyon Diary v3 keyword");
		}
	}
}
