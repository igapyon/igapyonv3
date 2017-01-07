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

package jp.igapyon.diary.v3.migration.hatena2md;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author Toshiki Iga
 */
public class HatenaTextUtil {
	/**
	 * はてなリンク形式を md リンク形式に変換します。
	 * 
	 * @param source
	 * @return
	 */
	public static String convertHatenaLink2MdLink(final String source) {
		// はてなリンクパターン。小さいマッチのために「?」を利用しています。
		final Pattern pat = Pattern.compile("\\[.*?\\:title=.*?\\]");
		final Matcher mat = pat.matcher(source);

		if (mat.find() == false) {
			// 一致しませんでした。置換する箇所はありませんでした。処理を終えて、このまま返却します。
			return source;
		}

		// 一致した文字列。
		final String linkString = mat.group();

		// title の前後で分断します。
		final Pattern linkPat = Pattern.compile("\\:title=");
		final Matcher linkMat = linkPat.matcher(linkString);
		if (linkMat.find() == false) {
			throw new IllegalArgumentException("Unexpected state.");
		}

		// URLパートは前半部分です。カッコは除去。
		String linkURL = linkString.substring(1, linkMat.start());
		// タイトルは後半部分です。カッコは除去。
		final String linkTitle = linkString.substring(linkMat.end(), linkString.length() - 1);

		// URL
		{
			if (linkURL.indexOf("http://d.hatena.ne.jp/igapyon/") >= 0) {
				linkURL = "https://igapyon.github.io/diary/" + linkURL.substring(30, 34) + "/ig"
						+ linkURL.substring(32, 38) + ".html";
			}
		}

		// md 形式のリンクへとおきかえます。
		final String mdLink = "[" + linkTitle + "](" + linkURL + ")";

		// 該当箇所以降の置換は再帰処理とします。
		return source.substring(0, mat.start()) + mdLink + convertHatenaLink2MdLink(source.substring(mat.end()));
	}
}
