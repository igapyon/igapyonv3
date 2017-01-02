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

package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.MdTextUtil;

/**
 * .src.md から .md を生成するためのクラス。
 */
public class DiarySrcMd2MdConverter {
	private IgapyonV3Settings settings = null;

	public DiarySrcMd2MdConverter(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void processDir(final File dir) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file);
			} else if (file.isFile()) {
				if (file.getName().endsWith(".src.md")) {
					processFile(file);
				}
			}
		}
	}

	void processFile(final File file) throws IOException {
		final List<String> lines = FileUtils.readLines(file, "UTF-8");

		final boolean isDiary = file.getName().startsWith("ig");

		String firstH2Line = null;
		String year1 = "20";
		String year2 = file.getName().substring(2, 4);
		if (year2.startsWith("9")) {
			year1 = "19";
		}

		String month = file.getName().substring(4, 6);
		String day = file.getName().substring(6, 8);
		for (String line : lines) {
			if (firstH2Line == null) {
				// 最初の ## からテキストを取得。
				if (line.startsWith("## ")) {
					firstH2Line = line.substring(3);
				}
			}
		}

		for (int index = 0; index < lines.size(); index++) {
			String line = lines.get(index);
			line = MdTextUtil.convertDoubleKeyword2MdLink(line, settings);

			// タブは２スペースに変換。
			line = StringUtils.replaceAll(line, "\t", "  ");

			// 直リンク形式を md リンク形式に変換します。
			line = MdTextUtil.convertSimpleUrl2MdLink(line);

			lines.set(index, line);
		}

		// TODO support template system.

		if (isDiary) {
			// ヘッダ追加
			lines.add(0, year1 + year2 + "-" + month + "-" + day + " diary: " + firstH2Line);
			lines.add(1,
					"=====================================================================================================");
			lines.add(2,
					"[![いがぴょん画像(小)](https://igapyon.github.io/diary/images/iga200306s.jpg \"いがぴょん\")](https://igapyon.github.io/diary/memo/memoigapyon.html) 日記形式でつづる [いがぴょん](https://igapyon.github.io/diary/memo/memoigapyon.html)コラム ウェブページです。");
			lines.add(3, "");

			// 本体

			// フッタ追加
			lines.add("");
			lines.add("");
			lines.add(
					"----------------------------------------------------------------------------------------------------");
			lines.add("");
			lines.add("## この日記について");
			lines.add(
					"[いがぴょんについて](https://igapyon.github.io/diary/memo/memoigapyon.html) / [インデックスに戻る](https://igapyon.github.io/diary/idxall.html)");
		}

		String newName = file.getName().substring(0, file.getName().length() - (".src.md".length())) + ".md";
		FileUtils.writeLines(new File(file.getParentFile(), newName), lines);
	}
}
