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

package jp.igapyon.diary.v3;

import java.io.File;
import java.io.IOException;

import jp.igapyon.diary.v3.gendiary.TodayDiaryGenerator;
import jp.igapyon.diary.v3.indexing.DiaryIndexAtomGenerator;
import jp.igapyon.diary.v3.indexing.keyword.KeywordAtomByTitleGenerator;
import jp.igapyon.diary.v3.mdconv.DiarySrcMd2MdConverter;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class DefaultProcessor {
	/**
	 * 現時点の、このプロジェクトのエントリポイント。
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		final boolean isGenerateTodaysDiary = false;

		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("./test/data"));

		settings.setBaseurl("https://igapyon.github.io/diary");
		// settings.setBaseurl("http://www.igapyon.jp/igapyon/diary");

		{
			// FIXME
			final String[][] ADDING_DOUBLE_KEYWORDS = new String[][] { //
					{ "艦これ", "http://www.dmm.com/netgame/feature/kancolle.html" }, //
			};

			for (String[] lookup : ADDING_DOUBLE_KEYWORDS) {
				settings.getDoubleKeywordList().add(lookup);
			}
		}

		{
			if (isGenerateTodaysDiary) {
				// 今日の日記について、存在しなければ作成します。
				System.err.println("Generate today's diary file if not exists.");
				new TodayDiaryGenerator(settings).processDir();
			}

			{
				File dir = new File(settings.getRootdir(), "keyword");
				if (dir.exists() == false) {
					dir.mkdirs();
				}

				dir = new File(settings.getRootdir(), "memo");
				if (dir.exists() == false) {
					dir.mkdirs();
				}
			}

			// ルートディレクトリを含む各ディレクトリ用の index用のatomファイルを生成および更新します。
			System.err.println("Update .md atom.xml.");
			new DiaryIndexAtomGenerator(settings).process();

			// キーワードの atom を更新します。
			System.err.println("Update .keyword atom.xml.");
			new KeywordAtomByTitleGenerator(settings).process();

			// .html.src.md ファイルから .md ファイルを生成します。
			System.err.println("Convert .html.src.md to .html.md file.");
			new DiarySrcMd2MdConverter(settings).processDir(settings.getRootdir());
		}
	}
}
