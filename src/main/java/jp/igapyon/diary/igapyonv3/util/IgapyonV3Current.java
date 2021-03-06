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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IgapyonV3Current {
	private String title = "N/A";

	private String url = "N/A";

	private String filename = "NA.na";

	/**
	 * メンバーを追加しただけ。いつか日記の最終更新日とリンクします。
	 */
	private Date lastModified;

	private List<String> keywordList = new ArrayList<String>();

	public List<String> getKeywordList() {
		return keywordList;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public void setKeywordList(List<String> keywordList) {
		this.keywordList = keywordList;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public boolean isDiary() {
		final Pattern pat = Pattern.compile("ig[0-9][0-9][0-9][0-9][0-9][0-9]\\.");
		final Matcher mat = pat.matcher(filename);
		if (mat.find()) {
			return true;
		}
		return false;
	}

	public String getDiaryTitle() {
		if (filename.length() < 9) {
			return "ERROR:N/A";
		}

		String year1 = "20";
		String year2 = filename.substring(2, 4);
		if (year2.startsWith("9")) {
			year1 = "19";
		}

		String month = filename.substring(4, 6);
		String day = filename.substring(6, 8);

		return (year1 + year2 + "-" + month + "-" + day + " diary: " + getTitle());
	}
}
