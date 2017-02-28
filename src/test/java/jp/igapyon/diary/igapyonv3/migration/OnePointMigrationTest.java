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

package jp.igapyon.diary.igapyonv3.migration;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class OnePointMigrationTest {
	/**
	 * 対象とするシンプルなURLリンクパターン。
	 */
	public static final String URL_LINK_PATTERN = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";

	/**
	 * MD などで利用されるリンクのパターン。それ以外にも、はてな形式もこれで回避できます。
	 * 
	 * TODO あとはダブルクオートやシングルクオートなども読み飛ばし対象にすべきか???
	 */
	public static final String SKIPPING_MARKED_LINK_PATTERN = "(\\[.*?\\]|\\(.*?\\))";

	@Test
	public void testDoOnePointMigration() throws Exception {
		final File targetDir = new File("../diary/2002");
		final File[] files = targetDir.listFiles();
		for (File file : files) {
			if (file.isFile() == false) {
				continue;
			}
			if (file.getName().endsWith(".src.md") == false) {
				continue;
			}

			final String content = FileUtils.readFileToString(file, "UTF-8");
			final String converted = convertSimpleUrl2MdLink(content);
			if (content.equals(converted) == false) {
				System.err.println("Convert link:" + file.getName());
				FileUtils.writeStringToFile(file, converted, "UTF-8");
			}
		}
	}

	public static String convertSimpleUrl2MdLink(final String source) {

		final Pattern patMdLink = Pattern.compile(SKIPPING_MARKED_LINK_PATTERN);
		final Matcher matMdLink = patMdLink.matcher(source);

		final Pattern patURL = Pattern.compile(URL_LINK_PATTERN);
		final Matcher matURL = patURL.matcher(source);

		final boolean isMdLinkFound = matMdLink.find();
		final boolean isURLFound = matURL.find();

		if (isMdLinkFound == false && isURLFound == false) {
			// いずれも存在せず。処理せず戻します。
			return source;
		}

		if (isMdLinkFound && isURLFound == false) {
			// MDリンクのみ。リンクの終了場所まで読み飛ばしたうえで再帰処理します。
			return source.substring(0, matMdLink.end()) + convertSimpleUrl2MdLink(source.substring(matMdLink.end()));
		}

		if (isMdLinkFound && isURLFound) {
			// 両方見つかりました。それでは、どちらが先に登場するのでしょうか。
			if (matMdLink.start() < matURL.start()) {
				// MDリンクが先に登場しました。リンクの終了場所まで読み飛ばしたうえで再帰処理します。
				return source.substring(0, matMdLink.end())
						+ convertSimpleUrl2MdLink(source.substring(matMdLink.end()));
			}
			// 生リンクの勝ちです。
		}

		// それでは生リンクの埋め込み処理を実施します。
		// iPhone SE だと 38 が好適そう...
		return source.substring(0, matURL.start()) + getMdLinkString(matURL.group())
				+ convertSimpleUrl2MdLink(source.substring(matURL.end()));
	}

	public static String getMdLinkString(final String link) {
		return "<@link value=\"" + link + "\" />";
	}
}
