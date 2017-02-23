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

package jp.igapyon.diary.igapyonv3.md2html;

import java.io.File;
import java.io.IOException;

/**
 * md ファイルから html ファイルを生成します。
 * 
 * ※初期の github gh-pages 対応では、これは利用しません。
 * 
 * @author Toshiki Iga
 */
public abstract class IgapyonDirProcessor {
	protected String fileExt = ".md";

	public void parseDir(final File lookup, final String fileExt, final boolean recursivedir) throws IOException {
		this.fileExt = fileExt;
		parseDir(lookup, lookup, recursivedir);
	}

	public static String getSubdir(final File baseDir, final File file) throws IOException {
		final String baseDirStr = baseDir.getCanonicalPath();
		final String targetFileStr = file.getCanonicalPath();
		if (targetFileStr.startsWith(baseDirStr) == false) {
			throw new IllegalArgumentException("getSubdir: file[" + file.getCanonicalPath()
					+ "] must starts with baseDir[" + baseDir.getCanonicalPath() + "].");
		}
		String targetRelFileStr = targetFileStr.substring(baseDirStr.length());
		if (targetRelFileStr.startsWith("/") || targetRelFileStr.startsWith("\\")) {
			targetRelFileStr = targetRelFileStr.substring(1);
		}
		return targetRelFileStr;
	}

	public static String replaceExt(final String origName, final String newExt) {
		if (origName.contains(".") == false) {
			return origName + newExt;
		}
		final String withoutExt = origName.substring(0, origName.lastIndexOf("."));
		return withoutExt + newExt;
	}

	protected void parseDir(final File baseDir, final File lookup, final boolean recursivedir) throws IOException {
		final File[] files = lookup.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				if (recursivedir) {
					parseDir(baseDir, file, recursivedir);
				}
			}
			if (file.isFile()) {
				if (file.getName().toLowerCase().endsWith(".html.md")) {
					// special for igapyonv3
					continue;
				}
				if (file.getName().toLowerCase().endsWith(fileExt)) {
					final String baseDirStr = baseDir.getCanonicalPath();
					final String targetFileStr = file.getCanonicalPath();
					String targetRelFileStr = targetFileStr.substring(baseDirStr.length());
					if (targetRelFileStr.startsWith("/") || targetRelFileStr.startsWith("\\")) {
						targetRelFileStr = targetRelFileStr.substring(1);
					}

					parseFile(baseDir, file);
				}
			}
		}
	}

	public abstract void parseFile(final File baseDir, final File file) throws IOException;
}
