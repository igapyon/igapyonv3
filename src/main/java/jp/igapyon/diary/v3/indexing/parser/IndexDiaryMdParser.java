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

package jp.igapyon.diary.v3.indexing.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * .md ファイルからコンテンツ一覧を作成します。
 */
public class IndexDiaryMdParser {
	private List<DiaryItemInfo> diaryItemInfoList = new ArrayList<DiaryItemInfo>();

	private IgapyonV3Settings settings = null;

	private String prefixName = "ig";

	public IndexDiaryMdParser(final IgapyonV3Settings settings, final String prefixName) {
		this.settings = settings;
		this.prefixName = prefixName;
	}

	public List<DiaryItemInfo> processDir(final File dir, String path) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return diaryItemInfoList;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file, path + "/" + file.getName());
			} else if (file.isFile()) {
				if (file.getName().startsWith(prefixName) && file.getName().endsWith(".md")
						&& false == file.getName().endsWith(".src.md")) {
					processFile(file, path);
				}
			}
		}

		return diaryItemInfoList;
	}

	void processFile(final File file, final String path) throws IOException {
		final List<String> lines = FileUtils.readLines(file, "UTF-8");

		final String url = "https://igapyon.github.io/diary" + path + "/"
				+ file.getName().substring(0, file.getName().length() - 3);

		final DiaryItemInfo diaryItemInfo = new DiaryItemInfo();
		diaryItemInfo.setUri(url);

		for (int index = 0; index < lines.size(); index++) {
			final String line = lines.get(index);
			if (line.startsWith("===")) {
				// 直前のものが、このテキストのタイトルです。
				break;
			}
			diaryItemInfo.setTitle(line);

		}

		diaryItemInfoList.add(diaryItemInfo);
	}
}
