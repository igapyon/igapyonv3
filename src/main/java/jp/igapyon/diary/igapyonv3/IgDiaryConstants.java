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

/**
 * Constants of Igapyonv3
 * 
 * @author Toshiki Iga
 */
public class IgDiaryConstants {
	public static final String DEFAULT_SETTINGS_SRC_MD = "## Settings for igapyonv3 env\n" //
			+ "\n" //
			+ "This file is settings for [[igapyonv3]].\n" //
			+ "\n" //
			+ "### Setting\n" //
			+ "\n" //
			+ "${setVerbose(\"false\")}\n" //
			+ "${setGeneratetodaydiary(\"false\")}\n" //
			+ "${setDuplicatefakehtmlmd(\"true\")}\n" //
			+ "${setConvertmarkdown2html(\"true\")}\n" //
			+ "${setAuthor(\"Test Author's name.\")}\n" //
			+ "${setBaseurl(\"https://igapyon.github.io/diary\")}\n" //
			+ "${setSourcebaseurl(\"https://github.com/igapyon/diary/blob/gh-pages\")}\n" //
			+ "\n" //
			+ "### Result\n" //
			+ "\n" //
			+ "${showSettings()}\n"; //

	/**
	 * default template header.
	 */
	public static final String TEMPLATE_HEADER = "<@navlist /> \n" //
			+ "\n" //
			+ "<#if current.isDiary()>${current.getDiaryTitle()}<#else>${current.title}</#if>\n" //
			+ "=====================================================================================================\n" //
			+ "[![いがぴょん画像(小)](${settings.baseurl}/images/iga200306s.jpg \"いがぴょん\")](${settings.baseurl}/memo/memoigapyon.html) 日記形式でつづる [いがぴょん](${settings.baseurl}/memo/memoigapyon.html)コラム ウェブページです。\n" //
			+ "";

	/**
	 * default template footer.
	 */
	public static final String TEMPLATE_FOOTER = "<#if current.isDiary()>\n" //
			+ "<@keywordlist /></#if>\n" //
			+ "----------------------------------------------------------------------------------------------------\n" //
			+ "\n" //
			+ "## この日記について\n" //
			+ "\n" //
			+ "<@linkshare /> / <@linktop /> / [いがぴょんについて](${settings.baseurl}/memo/memoigapyon.html) / [Diary Generator](https://github.com/igapyon/igapyonv3)\n" //
			+ "\n";
}
