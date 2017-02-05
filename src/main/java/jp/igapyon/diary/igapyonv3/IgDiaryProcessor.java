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

package jp.igapyon.diary.igapyonv3;

import java.io.File;
import java.io.IOException;

import jp.igapyon.diary.igapyonv3.gendiary.TodayDiaryGenerator;
import jp.igapyon.diary.igapyonv3.indexing.DiaryIndexAtomGenerator;
import jp.igapyon.diary.igapyonv3.indexing.keyword.KeywordAtomByTitleGenerator;
import jp.igapyon.diary.igapyonv3.indexing.keyword.KeywordMdTextGenerator;
import jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html;
import jp.igapyon.diary.igapyonv3.mdconv.DiarySrcMd2MdConverter;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

/**
 * igapyonv3 diary processor entry point.
 * 
 * @author Toshiki Iga
 */
public class IgDiaryProcessor {
	/**
	 * run igapyonv3 with specified rootdir.
	 * 
	 * @param rootdir
	 *            root dir of diary.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public void process(final File rootdir) throws IOException {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(rootdir);

		process(settings);
	}

	/**
	 * run igapyonv3 with specified settings.
	 * 
	 * @param settings
	 *            igapyonv3 settings.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public void process(final IgapyonV3Settings settings) throws IOException {
		{
			// settings.src.md first.
			final File fileSettings = new File(settings.getRootdir(), "settings.src.md");
			if (fileSettings.exists()) {
				System.err.println("igapyonv3 setting file found. :" + fileSettings.getCanonicalPath());
				new DiarySrcMd2MdConverter(settings).processFile(fileSettings);
			} else {
				System.err.println("igapyonv3 setting file not found. :" + fileSettings.getCanonicalPath());
			}
		}

		{
			// FIXME
			final String[][] ADDING_DOUBLE_KEYWORDS = new String[][] { //
					{ "艦これ", "http://www.dmm.com/netgame/feature/kancolle.html" }, //
			};

			for (String[] lookup : ADDING_DOUBLE_KEYWORDS) {
				settings.getDoubleKeywordList().add(lookup);
			}
		}

		if (settings.isGenerateTodayDiary()) {
			// 今日の日記について、存在しなければ作成します。
			System.err.println("Generate today's diary file if not exists.");
			new TodayDiaryGenerator(settings).processDir();
		}

		{
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
			System.err.println("Update keyword atom.xml.");
			new KeywordAtomByTitleGenerator(settings).process();

			System.err.println("Generate keyword md if exists.");
			new KeywordMdTextGenerator(settings).generateNewKeyword();

			// .src.md ファイルから .md ファイルを生成します。
			System.err.println("Convert .src.md to .html.md file.");
			new DiarySrcMd2MdConverter(settings).processDir(settings.getRootdir());
		}

		if (settings.isConvertMarkdown2Html()) {
			final File targetDir = new File(settings.getRootdir().getCanonicalPath() + "/target", "md2html");
			if (targetDir.exists() == false) {
				targetDir.mkdirs();
			}
			new IgapyonMd2Html().processDir(settings.getRootdir(), targetDir, true);
		}
	}

	/**
	 * Entry point for java command line exec.
	 * 
	 * @param args
	 *            java main args.
	 */
	public static void main(final String[] args) {
		try {
			new IgDiaryProcessor().process(new File("."));
		} catch (IOException e) {
			System.err.println("ERROR: " + e.toString());
			e.printStackTrace();
		}
	}
}
