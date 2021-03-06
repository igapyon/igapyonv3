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

package jp.igapyon.diary.igapyonv3.mdconv.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import freemarker.cache.TemplateLoader;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

/**
 * igapyonv3 向けの所定の挙動をおこなうテンプロードローダーです。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3TemplateLoader implements TemplateLoader {
	// set my custom template loader.

	/**
	 * 日記エンジン用設定。
	 */
	private IgapyonV3Settings settings = null;

	/**
	 * ヘッダーとフッターを展開するかどうか。
	 */
	private boolean isExpandHeaderFooter = false;

	protected Map<String, String> resourceMap = new HashMap<String, String>();

	public IgapyonV3TemplateLoader(final IgapyonV3Settings settings, boolean isExpandHeaderFooter) {
		this.settings = settings;
		this.isExpandHeaderFooter = isExpandHeaderFooter;
	}

	/**
	 * 与えられたリソース名にロケール文字列が含まれている場合がこれを除去します。
	 * 
	 * @param resourceName
	 *            name of resource.
	 * @return without locale name.
	 */
	public static String stripLocaleName(final String resourceName) {
		String actualResourceName = resourceName;

		// 現在の設定では無効化されているが、FreeMarker はデフォルトでロケール付きで以下のように値が渡されてくる。
		// ここからしばらくの処理は、このロケール文字列を除去するための記述に当たる。
		// test/data/hatena/ig161227.html.src_ja_JP.md

		// for case below: config.setLocalizedLookup(true);
		final Pattern patLocale = Pattern.compile("[_]..[_]..\\.");
		final Matcher matLocale = patLocale.matcher(resourceName);

		if (matLocale.find()) {
			// ロケール記述があれば、これを除去。
			actualResourceName = resourceName.substring(0, matLocale.start())
					+ resourceName.substring(matLocale.end() - 1);
		}

		return actualResourceName;
	}

	@Override
	public Object findTemplateSource(final String resourceName) throws IOException {
		final File actualFile = new File(settings.getRootdir(), stripLocaleName(resourceName));
		final String body = FileUtils.readFileToString(actualFile, "UTF-8");
		String load = body;
		load = load.trim() + "\n";

		if (isExpandHeaderFooter == false) {
			// 加工無し出力。
			resourceMap.put(resourceName, load);
			return resourceName;
		}

		load = getStandardHeaderString() + load;

		{
			// keyword の場合の特殊処理。
			final File sourceDir = new File(settings.getRootdir(), resourceName).getCanonicalFile().getParentFile();
			if (sourceDir.getName().equals("keyword")) {
				String fileName = resourceName;
				fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
				fileName = fileName.substring(0, fileName.indexOf("."));

				String footer = "";
				footer += "\n";
				footer += "### 日記\n";
				footer += "\n";
				footer += "<@localrss filename=\"atom/" + fileName + ".xml\" /><#-- 利用日記情報を読み込み -->\n";
				footer += "\n";
				load += footer;
			}
		}

		{
			final File fileTemplate = new File(settings.getRootdir(), "template-footer.md");
			if (fileTemplate.exists()) {
				final String template = FileUtils.readFileToString(fileTemplate, "UTF-8");
				load += template;
			} else {
				System.err.println("template-footer.md not found.:" + fileTemplate.getCanonicalPath());
			}
		}

		// 加工無し出力は、いまのところ実装なし。
		// resourceMap.put(resourceName, load);
		// return resourceName;
		// }

		resourceMap.put(resourceName, load);

		if (settings.isDebug()) {
			System.err.println("IgapyonV3TemplateLoader#findTemplateSource(): [" + load + "]");
		}

		return resourceName;
	}

	@Override
	public Reader getReader(final Object templateSource, final String encoding) throws IOException {
		if ("diaryYearList".equals((String) templateSource)) {
			throw new IOException("ERROR: diaryYearList is removed. use directive instead.");
		}

		return new StringReader(resourceMap.get(templateSource));
	}

	@Override
	public void closeTemplateSource(final Object templateSource) throws IOException {
		resourceMap.remove(templateSource);
	}

	@Override
	public long getLastModified(final Object templateSource) {
		return System.currentTimeMillis();
	}

	//////////////////////////////////////////

	/**
	 * 標準的なヘッダーを取得します。
	 * 
	 * @return header string
	 * @throws IOException
	 *             io exception occurs.
	 */
	protected String getStandardHeaderString() throws IOException {
		String header = "";

		{
			// TODO 一行目の展開ができていません。良い実装方法を考えましょう。
			final File fileTemplate = new File(settings.getRootdir(), "template-header.md");
			if (fileTemplate.exists()) {
				final String template = FileUtils.readFileToString(fileTemplate, "UTF-8");
				header += template;
				if (header.endsWith("\n") == false) {
					header += "\n";
				}
			} else {
				System.err.println("template-header.md not found.:" + fileTemplate.getCanonicalPath());
				header += "<@navlist />\n";
				header += "\n";
				header += "<#if current.isDiary()>${current.getDiaryTitle()}<#else>${current.title}</#if>\n";
				header += "===================================\n";
				header += "<#-- template-header.md not found. -->\n";
			}
		}

		header += "\n";

		return header;
	}
}
