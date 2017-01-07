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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * いがぴょんの日記v3 システムのための基本設定クラス。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3Settings {
	/**
	 * 日記のルートディレクトリ。
	 */
	private File rootdir = new File(".");

	/**
	 * TODAY! for newly diary.
	 */
	private Date today = null;

	private List<String[]> doubleKeywordList = new ArrayList<String[]>();

	/**
	 * この日記の作成者。
	 */
	private String author = "Toshiki Iga";

	/**
	 * 日記の記述言語。
	 */
	private String language = "ja_JP";

	/**
	 * Default double keywords.
	 */
	public static final String[][] DEFAULT_DOUBLE_KEYWORDS = { //
			// igapyon's keyword
			// FIXME TODO これは、keyword ディレクトリからの自動取り込み反映に実装変更の予定。
			{ "Maven", "https://igapyon.github.io/diary/keyword/Maven.html" }, //
			{ "FreeMarker", "https://igapyon.github.io/diary/keyword/FreeMarker.html" }, //

			//

			// blanco Framework
			{ "blanco Framework", "https://ja.osdn.net/projects/blancofw/wiki/blancofw" },
			{ "Blanco2g", "https://ja.osdn.net/projects/blancofw/wiki/Blanco2g" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" }, { "Ubuntu", "https://www.ubuntu.com/" },
			{ "blancoDb", "http://www.igapyon.jp/blanco/blancodb.html" },
			{ "blancoDbDotNet", "http://www.igapyon.jp/blanco/blancodbdotnet.html" },
			{ "blancoResourceBundle", "http://www.igapyon.jp/blanco/blancoresourcebundle.html" },
			{ "blancoMailCore", "http://www.igapyon.jp/blanco/blancomailcore.html" },
			{ "Chrome", "https://www.google.co.jp/chrome/browser/" }, { "Selenium", "http://www.seleniumhq.org/" },
			{ "Groovy", "http://www.groovy-lang.org/" },
			{ "Java", "http://www.oracle.com/technetwork/java/index.html" },
			{ "Object Pascal", "https://ja.wikipedia.org/wiki/Object_Pascal" },
			{ "Force.com", "https://www.salesforce.com/products/platform/products/force/" },
			{ "LLVM", "http://llvm.org/" }, { "OpenDocument", "https://ja.wikipedia.org/wiki/OpenDocument" },

			// oss
			{ "Apache", "https://www.apache.org/" }, { "Axis2", "https://axis.apache.org/axis2/java/core/" },
			{ "JExcelApi", "http://jexcelapi.sourceforge.net/" }, { "OmegaT", "http://www.omegat.org/ja/omegat.html" },
			{ "Jersey", "https://jersey.java.net/" },

			// it
			{ "IoT", "https://ja.wikipedia.org/wiki/%E3%83%A2%E3%83%8E%E3%81%AE%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%BC%E3%83%8D%E3%83%83%E3%83%88" },
			{ "AirPrint", "https://support.apple.com/ja-jp/HT201311" }, { "Cordova", "https://cordova.apache.org/" },
			{ "VMware", "http://www.vmware.com/jp.html" }, { "iOS", "http://www.apple.com/jp/ios/ios-10/" },

			// embarcadero
			{ "RAD Studio", "https://www.embarcadero.com/jp/products/rad-studio" },
			{ "Delphi", "https://www.embarcadero.com/jp/products/delphi" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "InterBase", "https://ja.wikipedia.org/wiki/InterBase" },
			{ "FireUI", "https://www.embarcadero.com/jp/products/rad-studio/fireui" },
			{ "VCL", "https://ja.wikipedia.org/wiki/Visual_Component_Library" },
			{ "C++Builder", "https://www.embarcadero.com/jp/products/cbuilder" },

			// names
			{ "ネコバス", "http://nlab.itmedia.co.jp/nl/articles/1607/15/news147.html" },//
	};

	public IgapyonV3Settings() {
		today = new Date();

		for (String[] lookup : DEFAULT_DOUBLE_KEYWORDS) {
			doubleKeywordList.add(lookup);
		}
	}

	public Date getToday() {
		return today;
	}

	public List<String[]> getDoubleKeywordList() {
		return doubleKeywordList;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public File getRootdir() {
		return rootdir;
	}

	public void setRootdir(File rootdir) {
		this.rootdir = rootdir;
	}
}
