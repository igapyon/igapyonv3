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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.v3.mdconv.freemarker.IgapyonV3FreeMarkerUtil;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.MdTextUtil;
import jp.igapyon.diary.v3.util.SimpleDirUtil;

/**
 * ソースのマークダウンファイル `.src.md` から ターゲットのマークダウンファイル `.md` を生成するためのクラスです。
 * 
 * 多くの場合、`.html.src.md` から ターゲットの `.html.md` を生成します。
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
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
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

	void processFile(final File file) throws IOException {
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
			line = MdTextUtil.convertDoubleKeyword2MdLink(line, settings);

			// タブは２スペースに変換。
			line = StringUtils.replaceAll(line, "\t", "  ");

			// 直リンク形式を md リンク形式に変換します。
			// FreeMarker の都合、＜リンク＞の形式は利用せず、直リンク形式を採用しています。
			line = MdTextUtil.convertSimpleUrl2MdLink(line);

			lines.set(index, line);
		}

		String newName = file.getName().substring(0, file.getName().length() - (".src.md".length())) + ".md";
		FileUtils.writeLines(new File(file.getParentFile(), newName), lines);
	}
}
