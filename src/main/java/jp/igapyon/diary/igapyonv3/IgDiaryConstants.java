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
			+ "This file is settings for [[igapyonv3]].\n" // FIXME how to
															// specify igapyonv3
															// ??? default
															// keyword???
			+ "\n" //
			+ "### Setting\n" //
			+ "\n" //
			+ "${setVerbose(\"true\")}\n" //
			+ "${setDebug(\"false\")}\n" //
			+ "${setGeneratetodaydiary(\"true\")}\n" //
			+ "${setDuplicatefakehtmlmd(\"false\")}\n" // TODO change for
														// gh-pages
			+ "${setConvertmarkdown2html(\"true\")}\n" //
			+ "${setAuthor(\"Test Author's name.\")}\n" //
			+ "${setBaseurl(\"https://igapyon.github.io/diary\")}\n" // FIXME
																		// should
																		// be
																		// from
																		// outside
			+ "${setSourcebaseurl(\"https://github.com/igapyon/diary/blob/gh-pages\")}\n" // FIXME
																							// should
																							// be
																							// from
																							// outside
			+ "${setGeneratekeywordifneeded(\"true\")}\n" //
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
			+ "%SITETITLE%\n" //
			+ "";

	/**
	 * default template footer.
	 */
	public static final String TEMPLATE_FOOTER = "<#if current.isDiary()>\n" //
			+ "<@keywordlist /></#if>\n" //
			+ "----------------------------------------------------------------------------------------------------\n" //
			+ "\n" //
			+ "## About %SITETITLE%\n" //
			+ "\n" //
			+ "<@linkshare /> / <@linktop /> [Diary Generator](https://github.com/igapyon/igapyonv3)\n" //
			+ "\n";

	public static String DEFAULT_INDEX_SRC_MD = "<@localyearlist /> / [keyword](keyword/index.html) / [memo](memo/index.html) / [mirror1](http://www.igapyon.jp/igapyon/diary/) / [mirror2](http://igapyon.a.la9.jp/igapyon/diary/) / [mirror3](https://igapyon.github.io/diary/)\n" //
			+ "\n" //
			+ "## %SITETITLE%: top\n" //
			+ "\n" //
			+ "### Recent diary ([Feed](${settings.baseurl}/atomRecent.xml))\n" //
			+ "\n" //
			+ "<@localrss filename=\"atomRecent.xml\" /><#-- Load recent updates from local -->\n"; //

	public static String DEFAULT_KEYWORD_INDEX_SRC_MD = "<@localyearlist /> / [keyword](../keyword/index.html)\n" //
			+ "\n" //
			+ "## %SITETITLE%: keyword\n" //
			+ "\n" //
			+ "<@localrss filename=\"atom.xml\" />";
}
