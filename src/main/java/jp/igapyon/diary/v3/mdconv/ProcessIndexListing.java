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

package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * 指定されたファイルの一覧部分を更新する仕組みです。
 * 
 * TODO 他のクラスに併合されるべき処理と考えます。
 */
public class ProcessIndexListing {
	private IgapyonV3Settings settings = null;

	public ProcessIndexListing(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void process(File fileTarget, final List<DiaryItemInfo> diaryItemInfoList) throws IOException {
		fileTarget = fileTarget.getCanonicalFile();
		if (fileTarget.getName().endsWith(".src.md") == false) {
			return;
		}
		final String newName = fileTarget.getName().substring(0, fileTarget.getName().length() - 7) + ".md";

		String wrk = "";
		for (DiaryItemInfo itemInfo : diaryItemInfoList) {
			wrk = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrk;
		}

		String wrkRecent = "";
		for (DiaryItemInfo itemInfo : diaryItemInfoList) {
			if (itemInfo.getTitle().startsWith("2016-") || itemInfo.getTitle().startsWith("2017-")) {
				wrkRecent = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrkRecent;
			}
		}

		String target = FileUtils.readFileToString(fileTarget, "UTF-8");
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist}", wrk);
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist.recent}", wrkRecent);
		FileUtils.writeStringToFile(new File(fileTarget.getParentFile() + "/" + newName), target, "UTF-8");
	}
}
