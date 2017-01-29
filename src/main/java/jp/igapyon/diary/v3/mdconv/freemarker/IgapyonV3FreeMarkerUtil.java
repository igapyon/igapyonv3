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

package jp.igapyon.diary.v3.mdconv.freemarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.IncludeDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.KeywordlistDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LastModifiedDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkAmazonDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkDiaryDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkMapDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkNextDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkPrevDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkSearchDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkShareDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkSourceDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkTargetDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkTopDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LocalRssDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LocalYearlistDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.NavlistDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.RSSFeedDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.method.SetAuthorMethodModel;
import jp.igapyon.diary.v3.mdconv.freemarker.method.SetBaseurlMethodModel;
import jp.igapyon.diary.v3.mdconv.freemarker.method.SetSourcebaseurlMethodModel;
import jp.igapyon.diary.v3.mdconv.freemarker.method.SetVerboseMethodModel;
import jp.igapyon.diary.v3.mdconv.freemarker.method.ShowSettingsMethodModel;
import jp.igapyon.diary.v3.util.IgapyonV3Current;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleDirUtil;

/**
 * FreeMarker 用のユーティリティクラス。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3FreeMarkerUtil {
	/**
	 * FreeMarker を使ったテンプレート変換処理を実行します。
	 * 
	 * @param rootdir
	 * @param file
	 * @param templateData
	 * @return
	 * @throws IOException
	 */
	public static String process(File file, final Map<String, Object> templateData, final IgapyonV3Settings settings)
			throws IOException {

		// do canonical
		final File rootdir = settings.getRootdir().getCanonicalFile();
		file = file.getCanonicalFile();

		final String relativePath = SimpleDirUtil.getRelativePath(rootdir, file);

		final Configuration config = getConfiguration(settings, true);

		// Pre-define value
		templateData.put("settings", settings);

		IgapyonV3Current current = null;

		final Template templateBase = config.getTemplate(relativePath);

		try {
			// 一旦、事前準備運動として空読み込みを実施します。
			templateData.put("current", new IgapyonV3Current());

			final StringWriter writer = new StringWriter();
			templateBase.process(templateData, writer);

			// ここで得られた 展開後の md ファイルを入力として、current オブジェクトへのプリセットを実施します。

			current = buildCurrentObjectByPreParse(writer.toString(), templateBase.getName(), settings);
		} catch (TemplateException e) {
			throw new IOException(e);
		}

		// 空読みで得られた知見を current に反映したい。
		templateData.put("current", current);

		try {
			final StringWriter writer = new StringWriter();
			templateBase.process(templateData, writer);
			return writer.toString();
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}

	public static IgapyonV3Current buildCurrentObjectByPreParse(final String sourceString, final String sourceName,
			final IgapyonV3Settings settings) throws IOException {
		final IgapyonV3Current current = new IgapyonV3Current();
		current.setFilename(new File(sourceName).getName());

		final BufferedReader reader = new BufferedReader(new StringReader(sourceString));
		for (;;) {
			final String line = reader.readLine();
			if (line == null) {
				break;
			}
			// 最初の ## からテキストを取得。これは igapyonv3 の最大の制約です。
			if (line.startsWith("## ")) {
				current.setTitle(line.substring(3));
				// System.err.println("title: [" + current.getTitle() + "]");
				break;
			}
		}

		{
			String url = SimpleDirUtil.file2Url(new File(settings.getRootdir(), sourceName), settings);
			{
				// TODO共通関数化せよ。
				// igapyonv3 特有のファイル名変化に当たります。
				if (url.endsWith(".html.src.md")) {
					url = url.substring(0, url.length() - ".src.md".length());
				}
				// README.md は index.html に読み替えます。
				if (url.endsWith("/README.src.md")) {
					url = url.substring(0, url.length() - "/README.src.md".length()) + "/index.html";
				}
			}
			current.setUrl(url);
		}

		// こいつはキーワード抽出。
		{
			{
				// タイトルに [] があればこれを記憶。
				final Pattern pat = Pattern.compile("\\[.*?\\]");
				final Matcher mat = pat.matcher(current.getTitle());

				for (; mat.find();) {
					// まず、タイトルの [] を読み込み。これは、本文のダブルカッコと同じものと考えて良い。
					String word = mat.group();
					word = word.substring(1, word.length() - 1);
					current.getKeywordList().add(word);
				}
			}

			{
				// ボディに [[]] があればこれを記憶。
				final Pattern pat = Pattern.compile("\\[\\[.*?\\]\\]");
				final Matcher mat = pat.matcher(sourceString);

				for (; mat.find();) {
					String word = mat.group();
					word = word.substring(2, word.length() - 2);
					current.getKeywordList().add(word);
				}
			}
			{
				// 重複キーワードは除去
				final Map<String, String> mapDupCheck = new HashMap<String, String>();
				for (int index = 0; index < current.getKeywordList().size(); index++) {
					final String lookup = current.getKeywordList().get(index);
					if (mapDupCheck.get(lookup.toLowerCase()) != null) {
						// dup key
						current.getKeywordList().remove(index);
						index--;
						continue;
					}
					mapDupCheck.put(lookup.toLowerCase(), lookup);
				}
			}
		}

		// TODO これ以外に、キーワードのリスト抽出とか、いろいろやりたいのだが。。。

		return current;
	}

	/**
	 * IgayponV3 で利用するデフォルトのコンフィグレーションを取得します。
	 * 
	 * デフォルトからは、いくつか重要な変更が行われています。詳しくはソースコードを参照してください。
	 * 
	 * @return
	 */
	public static Configuration getConfiguration(final IgapyonV3Settings settings, final boolean isExpandHeaderFooter) {
		// newest version at this point.
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// DISABLE auto escape
		config.setAutoEscapingPolicy(Configuration.DISABLE_AUTO_ESCAPING_POLICY);

		// only for camel case not snake one.
		config.setNamingConvention(Configuration.CAMEL_CASE_NAMING_CONVENTION);

		// ??? set API disable
		config.setAPIBuiltinEnabled(true);

		// only for newer one.
		config.setClassicCompatible(false);

		config.setLazyAutoImports(false);

		// diary system not need localize lookup
		config.setLocalizedLookup(false);

		config.setLogTemplateExceptions(false);
		config.setRecognizeStandardFileExtensions(false);
		config.setWhitespaceStripping(false);

		// set my CUSTOM template loader.
		config.setTemplateLoader(new IgapyonV3TemplateLoader(settings, isExpandHeaderFooter));

		/////////////////////////
		// register custom tag.

		// methos
		config.setSharedVariable("setVerbose", new SetVerboseMethodModel(settings));
		config.setSharedVariable("setAuthor", new SetAuthorMethodModel(settings));
		config.setSharedVariable("setBaseurl", new SetBaseurlMethodModel(settings));
		config.setSharedVariable("setSourcebaseurl", new SetSourcebaseurlMethodModel(settings));
		config.setSharedVariable("showSettings", new ShowSettingsMethodModel(settings));

		// search
		config.setSharedVariable("linksearch", new LinkSearchDirectiveModel(settings));

		// sns
		config.setSharedVariable("linkshare", new LinkShareDirectiveModel(settings));

		// navi
		config.setSharedVariable("linktop", new LinkTopDirectiveModel(settings));
		config.setSharedVariable("linktarget", new LinkTargetDirectiveModel(settings));
		config.setSharedVariable("linksource", new LinkSourceDirectiveModel(settings));
		config.setSharedVariable("linkprev", new LinkPrevDirectiveModel(settings));
		config.setSharedVariable("linknext", new LinkNextDirectiveModel(settings));
		config.setSharedVariable("navlist", new NavlistDirectiveModel(settings));

		// others
		config.setSharedVariable("linkdiary", new LinkDiaryDirectiveModel(settings));
		config.setSharedVariable("linkamazon", new LinkAmazonDirectiveModel(settings));
		config.setSharedVariable("linkmap", new LinkMapDirectiveModel(settings));
		config.setSharedVariable("lastmodified", new LastModifiedDirectiveModel(settings));

		// lists
		config.setSharedVariable("rssfeed", new RSSFeedDirectiveModel(settings));
		config.setSharedVariable("localrss", new LocalRssDirectiveModel(settings));
		config.setSharedVariable("localyearlist", new LocalYearlistDirectiveModel(settings));
		config.setSharedVariable("keywordlist", new KeywordlistDirectiveModel(settings));

		// file
		config.setSharedVariable("include", new IncludeDirectiveModel(settings));

		return config;
	}
}
