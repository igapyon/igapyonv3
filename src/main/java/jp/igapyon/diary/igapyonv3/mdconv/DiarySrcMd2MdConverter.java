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

package jp.igapyon.diary.igapyonv3.mdconv;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.igapyonv3.mdconv.freemarker.IgapyonV3FreeMarkerUtil;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.MdTextUtil;
import jp.igapyon.diary.igapyonv3.util.SimpleDirUtil;
import jp.igapyon.diary.util.IgFileComparatorByName;
import jp.igapyon.diary.util.IgFileUtil;

/**
 * ソースのマークダウンファイル `.src.md` から ターゲットのマークダウンファイル `.md` を生成するためのクラスです。
 * 
 * 多くの場合、`.src.md` から ターゲットの `.md` そしておまけで `.html.md` を生成します。
 * 
 * @author Toshiki Iga
 */
public class DiarySrcMd2MdConverter {
	private IgapyonV3Settings settings = null;

	/**
	 * キャッシュ用オブジェクト。
	 */
	protected final Map<String, String> cacheAtomStringMap = new HashMap<String, String>();

	public DiarySrcMd2MdConverter(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void processDir(final File dir) throws IOException {
		final List<File> fileList = new ArrayList<File>();
		{
			final File[] files = dir.listFiles();
			if (files == null) {
				return;
			}
			for (File file : files) {
				fileList.add(file);
			}
		}

		Collections.sort(fileList, new IgFileComparatorByName());

		for (File file : fileList) {
			if (file.isDirectory()) {
				// 根っこレベルの target および srcのみ除外する必要があります。
				final String dirName = SimpleDirUtil.getRelativePath(settings.getRootdir(), file);
				if ("target".equals(dirName) || "src".equals(dirName)) {
					// target や src は処理してはなりません。
					continue;
				}
				processDir(file);
			} else if (file.isFile()) {
				if (file.getName().endsWith(".src.md")) {
					processFile(file);
				}
			}
		}
	}

	public void processFile(final File file) throws IOException {
		if (settings.isDebug()) {
			System.err.println("srcmd2md: " + SimpleDirUtil.getRelativePath(settings.getRootdir(), file));
		}

		final Map<String, Object> templateData = new HashMap<String, Object>();

		// テンプレート適用処理を実施します。
		final String convertedString = IgapyonV3FreeMarkerUtil.process(file, templateData, settings);

		// 行データとして改めて読み込みます。
		final List<String> lines = new ArrayList<String>();
		{
			final BufferedReader reader = new BufferedReader(new StringReader(convertedString));
			for (;;) {
				final String line = reader.readLine();
				if (line == null) {
					break;
				}
				lines.add(line);
			}
		}

		for (int index = 0; index < lines.size(); index++) {
			String line = lines.get(index);
			line = MdTextUtil.convertDoubleKeyword2MdLink(line, file.getParentFile(), settings);

			// タブは２スペースに変換。
			line = StringUtils.replaceAll(line, "\t", "  ");

			// 直リンク形式を md リンク形式に変換します。
			// FreeMarker の都合、＜リンク＞の形式は利用せず、直リンク形式を採用しています。
			// line = MdTextUtil.convertSimpleUrl2MdLink(line);

			lines.set(index, line);
		}

		// TODO .src.md から .md を取得するための共通関数がほしいです。
		{
			// generate from .src.md to .md
			final String newName = file.getName().substring(0, file.getName().length() - (".src.md".length())) + ".md";
			final File fileWrite = new File(file.getParentFile(), newName);
			if (IgFileUtil.checkWriteNecessary("srcmd2md", lines, fileWrite)) {
				FileUtils.writeLines(fileWrite, lines);
			}
		}

		if (settings.isDuplicateFakeHtmlMd()) {
			// generate fake html.md file for gh-pages
			final String newName = file.getName().substring(0, file.getName().length() - (".src.md".length()))
					+ ".html.md";
			final File fileWrite = new File(file.getParentFile(), newName);
			if (IgFileUtil.checkWriteNecessary("srcmd2md", lines, fileWrite)) {
				FileUtils.writeLines(fileWrite, lines);
			}
		}
	}
}
