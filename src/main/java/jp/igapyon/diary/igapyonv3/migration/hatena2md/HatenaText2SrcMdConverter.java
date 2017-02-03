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

package jp.igapyon.diary.igapyonv3.migration.hatena2md;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

/**
 * 分割された「はてなテキスト」からMDファイル (.html.src.md) を生成します。
 * 
 * はてな記法の一部しかサポートできていません。
 * 
 * @author Toshiki Iga
 */
public class HatenaText2SrcMdConverter {
	private IgapyonV3Settings settings = null;

	public HatenaText2SrcMdConverter(final IgapyonV3Settings settings) {
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
				if (file.getName().startsWith("ig") && file.getName().endsWith(".src.hatenadiary")) {
					processFile(file);
				}
			}
		}
	}

	void processFile(final File file) throws IOException {
		final String origString = FileUtils.readFileToString(file, "UTF-8");
		final List<String> lines = FileUtils.readLines(file, "UTF-8");

		// 本体
		// タイトル行は別の仕組みで生成されるので、とりあえずとってしまう。
		lines.remove(0);

		// 最初の空行を除去。
		for (int index = 0; index < lines.size(); index++) {
			String line = lines.get(index);
			if (line.trim().length() == 0) {
				lines.remove(index);
				index--;
			} else {
				break;
			}
		}

		///////////////////////////////
		// 処理基本情報

		// リスト表現
		boolean isProcessInListing = false;

		for (int index = 0; index < lines.size(); index++) {
			{
				String line = lines.get(index);
				if (line.startsWith("*p1*") || line.startsWith("*p2*") || line.startsWith("*p3*")
						|| line.startsWith("*p4*") || line.startsWith("*p5*")) {
					// はてなタイトルを md のh2 に変換。
					String newLine = line;
					newLine = StringUtils.replace(newLine, "*p1*", "## ");
					newLine = StringUtils.replace(newLine, "*p2*", "## ");
					newLine = StringUtils.replace(newLine, "*p3*", "## ");
					newLine = StringUtils.replace(newLine, "*p4*", "## ");
					newLine = StringUtils.replace(newLine, "*p5*", "## ");

					if (index > 0) {
						lines.set(index++, "");
						lines.add(index, newLine);
					} else {
						lines.set(index, newLine);
					}
					lines.add(index + 1, "");
				}
			}

			{
				// その他のはてなタイトル行表現を md 形式に変換。
				String line = lines.get(index);
				if (line.startsWith("**") || line.startsWith("***") || line.startsWith("****")
						|| line.startsWith("*****") || line.startsWith("******")) {
					String newLine = line;
					newLine = StringUtils.replace(newLine, "******", "####### ");
					newLine = StringUtils.replace(newLine, "*****", "###### ");
					newLine = StringUtils.replace(newLine, "****", "##### ");
					newLine = StringUtils.replace(newLine, "***", "#### ");
					newLine = StringUtils.replace(newLine, "**", "### ");

					if (index > 0) {
						lines.set(index++, "");
						lines.add(index, newLine);
					} else {
						lines.set(index, newLine);
					}
					lines.add(index + 1, "");
				}
			}

			{
				// はてなコメント記法
				String line = lines.get(index);
				if (line.trim().equals("||<")) {
					lines.set(index, "```");
					lines.add(++index, "");
					// FIXME それ以降に何があろうが、とりあえず無視しています。
					continue;
				}
				if (line.trim().equals(">||")) {
					lines.add(index++, "");
					lines.set(index, "```");
					// FIXME それ以降に何があろうが、とりあえず無視しています。
					continue;
				}

				{
					// はてなリンクパターン。小さいマッチのために「?」を利用しています。
					final Pattern pat = Pattern.compile(">\\|.*?\\|");
					final Matcher mat = pat.matcher(line);

					if (mat.find()) {
						final String markup = mat.group();
						String language = markup.substring(2, markup.length() - 1);
						if (language.equals("Java")) {
							language = "java";
						}
						line = mat.replaceFirst("```" + language);

						lines.add(index++, "");
						lines.set(index, line);
						// FIXME それ以降に何があろうが、とりあえず無視しています。
						continue;
					}
				}
			}

			{
				// リスト構造。
				String line = lines.get(index);
				if (line.trim().startsWith("---")) {
					if (isProcessInListing == false) {
						isProcessInListing = true;
						lines.add(index++, "");
					}
					line = StringUtils.replaceFirst(line, "\\---", "    * ");
					lines.set(index, line);
				} else if (line.trim().startsWith("--")) {
					if (isProcessInListing == false) {
						isProcessInListing = true;
						lines.add(index++, "");
					}
					line = StringUtils.replaceFirst(line, "\\--", "  * ");
					lines.set(index, line);
				} else if (line.trim().startsWith("-")) {
					if (isProcessInListing == false) {
						isProcessInListing = true;
						lines.add(index++, "");
					}
					line = StringUtils.replaceFirst(line, "\\-", "* ");
					lines.set(index, line);
				} else {
					if (isProcessInListing) {
						isProcessInListing = false;
						lines.add(index++, "");
					}
				}
			}

			{
				// はてなリンク形式を md リンク形式に変換します。
				// FIXME ただし、この実装は、ここではなく、次のmdソース変換に移動する可能性が高い。
				String line = lines.get(index);
				line = HatenaTextUtil.convertHatenaLink2MdLink(line);
				lines.set(index, line);
			}
		}

		String newName = file.getName().substring(0, file.getName().length() - (".src.hatenadiary".length()))
				+ ".html.src.md";
		FileUtils.writeLines(new File(file.getParentFile(), newName), lines);

		final String destString = FileUtils.readFileToString(file, "UTF-8");
		if (origString.equals(destString) == false) {
			System.err.println("Hatena to md file: file updated: " + file.getAbsolutePath());
		}
	}
}