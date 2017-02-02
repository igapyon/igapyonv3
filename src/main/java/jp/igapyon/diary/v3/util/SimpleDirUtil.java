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

package jp.igapyon.diary.v3.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ディレクトリ処理のためのユーティリティを蓄えます。
 * 
 * @author Toshiki Iga
 */
public class SimpleDirUtil {
	/**
	 * 与えられたファイルの、基準ディレクトリからの相対パスを文字列で取得します。
	 * 
	 * 先頭は / や ￥ を含まないものとします。
	 * 
	 * @param baseDir
	 *            dir of base.
	 * @param file
	 *            file of input.
	 * @return relative path.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static String getRelativePath(final File baseDir, final File file) throws IOException {
		final String baseDirStr = baseDir.getCanonicalPath();
		final String targetFileStr = file.getCanonicalPath();
		if (targetFileStr.startsWith(baseDirStr) == false) {
			throw new IOException("SimpleDirUtil#getRelativePath: file[" + targetFileStr + "] must under of baseDir["
					+ baseDirStr + "].");
		}

		String targetRelFileStr = targetFileStr.substring(baseDirStr.length());
		if (targetRelFileStr.startsWith("/") || targetRelFileStr.startsWith("\\")) {
			// 先頭が / や ￥ で開始される場合はこれを取り去ります。
			targetRelFileStr = targetRelFileStr.substring(1);
		}

		return targetRelFileStr;
	}

	public static List<String> toPathList(File file) throws IOException {
		file = file.getCanonicalFile();
		final List<String> result = new ArrayList<String>();
		for (;;) {
			if (file.getName().equals("/") || file.getName().equals("\\") || file.getName().equals(File.pathSeparator)
					|| file.getName().equals("")) {
				break;
			}
			result.add(0, file.getName());
			file = file.getParentFile();
		}
		return result;
	}

	public static String getMovingPath(String fromPathString, String toPathString) throws IOException {
		if (fromPathString.startsWith("/") == false) {
			fromPathString = ("/" + fromPathString);
		}
		if (toPathString.startsWith("/") == false) {
			toPathString = ("/" + toPathString);
		}
		final File fromPath = new File(fromPathString).getCanonicalFile();
		final File toPath = new File(toPathString).getCanonicalFile();

		return getMovingPath(fromPath, toPath);
	}

	public static String getMovingPath(File fromPath, File toPath) throws IOException {
		final List<String> fromList = toPathList(fromPath.getCanonicalFile());
		final List<String> toList = toPathList(toPath.getCanonicalFile());

		return getMovingPath(fromList, toList);
	}

	public static String getMovingPath(final List<String> fromPathList, final List<String> toPathList)
			throws IOException {
		int level = 0;
		for (;; level++) {
			if (level >= toPathList.size()) {
				// reach to end of tolist
				String result = "";
				for (int index = level; index < fromPathList.size(); index++) {
					if (result.length() != 0) {
						result += "/";
					}
					result += "..";
				}
				return result;
			}

			if (level >= fromPathList.size()) {
				String result = "";
				for (int index = level; index < toPathList.size(); index++) {
					if (result.length() != 0) {
						result += "/";
					}
					result += toPathList.get(index);
				}
				return result;
			}

			final String left = fromPathList.get(level);
			final String right = toPathList.get(level);
			if (left.equals(right) == false) {
				// different
				String result = "";
				for (int index = level; index < fromPathList.size(); index++) {
					if (result.length() != 0) {
						result += "/";
					}
					result += "..";
				}
				for (int index = level; index < toPathList.size(); index++) {
					result += "/";
					result += toPathList.get(index);
				}
				return result;
			}
		}
	}

	public static String getRelativeUrlIfPossible(final String url, final File currentDir,
			final IgapyonV3Settings settings) throws IOException {
		if (url.startsWith(settings.getBaseurl()) == false) {
			return url;
		}

		final File targetFile = url2File(url, settings);

		final String movingPath = getMovingPath(currentDir, targetFile);
		if (movingPath.length() == 0) {
			return url;
		}
		return movingPath;
	}

	/**
	 * URL を File に変換します。
	 * 
	 * @param url
	 *            input URL.
	 * @param settings
	 *            diary settings.
	 * @return file style.
	 * @throws IOException
	 *             io exception occurs.
	 * @deprecated 使いドコロがあったはずと思って作成したものの、実際に適用しようとしたらフィットする箇所がない。とりあえず
	 *             deprecated マークとする
	 */
	public static File url2File(String url, final IgapyonV3Settings settings) throws IOException {
		if (url.startsWith(settings.getBaseurl()) == false) {
			throw new IOException(
					"SimpleDirUtil#url2File: url[" + url + "] must under of baseurl[" + settings.getBaseurl() + "].");
		}
		url = url.substring(settings.getBaseurl().length());
		return new File(settings.getRootdir(), url);
	}

	/**
	 * ファイルを url に変換します。
	 * 
	 * このクラスの中から呼び出しされています。
	 * 
	 * @param file
	 *            input file.
	 * @param settings
	 *            diary settings.
	 * @return URL string.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static String file2Url(final File file, final IgapyonV3Settings settings) throws IOException {
		final String relativePath = getRelativePath(settings.getRootdir(), file);
		if (relativePath.length() == 0) {
			return settings.getBaseurl();
		}
		if (relativePath.startsWith("/")) {
			return settings.getBaseurl() + relativePath;
		} else {
			return settings.getBaseurl() + "/" + relativePath;
		}
	}
}
