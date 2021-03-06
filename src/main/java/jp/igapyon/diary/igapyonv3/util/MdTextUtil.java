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
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.igapyon.diary.util.IgStringUtil;

/**
 * Markdown テキストのためのユーティリティです。
 * 
 * @author Toshiki Iga
 */
public class MdTextUtil {
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

	/**
	 * シンプルな URL を MD リンク形式に変換します。
	 * 
	 * @param source
	 *            input URL.
	 * @return markdown style string.
	 */
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

	public static String getMdLinkString(final String originalUrl) {
		// iPhone SE だと 38 が好適そう...
		final String urlShow = IgStringUtil.abbreviateMiddle(originalUrl);
		return "[" + urlShow + "](" + originalUrl + ")";
	}

	public static String convertDoubleKeyword2MdLink(final String source, final File currentdir,
			final IgapyonV3Settings settings) throws IOException {

		// [[key]]system
		final String DOUBLE_KEYWORD_PATTERN = "\\[\\[.*?\\]\\]";
		final Pattern patDoubleKeyword = Pattern.compile(DOUBLE_KEYWORD_PATTERN);
		final Matcher matDoubleKeyword = patDoubleKeyword.matcher(source);
		final boolean isDoubleKeywordFound = matDoubleKeyword.find();
		if (isDoubleKeywordFound == false) {
			return source;
		}

		String foundKeyword = matDoubleKeyword.group();
		foundKeyword = foundKeyword.substring(2, foundKeyword.length() - 2);

		for (String[] registeredPair : settings.getDoubleKeywordList()) {
			if (registeredPair[0].compareToIgnoreCase(foundKeyword) == 0) {
				// 最初のヒットのみ置換したうえで残り部分を再帰呼出し。
				return source.substring(0, matDoubleKeyword.start()) + "[" + registeredPair[0] + "]("
						+ SimpleDirUtil.getRelativeUrlIfPossible(registeredPair[1], currentdir, settings) + ")"
						+ convertDoubleKeyword2MdLink(source.substring(matDoubleKeyword.end()), currentdir, settings);
			}
		}

		System.err.println("Keyword [[" + foundKeyword + "]] was skipped.");

		return source;
	}
}
