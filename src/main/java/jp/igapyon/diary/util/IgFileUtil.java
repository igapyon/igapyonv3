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

package jp.igapyon.diary.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class IgFileUtil {
	/**
	 * Check if it should be written or not.
	 * 
	 * @param titleString
	 *            like 'md2html'.
	 * @param outputData
	 *            data of string to be written.
	 * @param targetFile
	 *            file of target html.
	 * @return true:write
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static boolean checkWriteNecessary(final String titleString, final String outputData, final File targetFile)
			throws IOException {
		if (targetFile.exists() == false) {
			System.out.println(titleString + ": add: " + targetFile.getCanonicalPath());
			return true;
		} else {
			final String origOutputHtmlString = FileUtils.readFileToString(targetFile, "UTF-8");
			if (outputData.equals(origOutputHtmlString)) {
				System.out.println(titleString + ": non: " + targetFile.getCanonicalPath());
				return false;
			} else {
				System.out.println(titleString + ": upd: " + targetFile.getCanonicalPath());
				return true;
			}
		}
	}
}
