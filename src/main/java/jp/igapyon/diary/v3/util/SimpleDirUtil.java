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
	 * @param file
	 * @return
	 * @throws IOException
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
}
