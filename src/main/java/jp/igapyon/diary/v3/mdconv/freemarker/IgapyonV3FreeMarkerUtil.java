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
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkSearchDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LinkShareDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LocalRssDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.LocalYearlistDirectiveModel;
import jp.igapyon.diary.v3.mdconv.freemarker.directive.RSSFeedDirectiveModel;
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

			current = buildCurrentObjectByPreParse(writer.toString());
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

	public static IgapyonV3Current buildCurrentObjectByPreParse(final String sourceString) throws IOException {
		final IgapyonV3Current current = new IgapyonV3Current();

		final BufferedReader reader = new BufferedReader(new StringReader(sourceString));
		for (;;) {
			final String line = reader.readLine();
			if (line == null) {
				break;
			}
			// 最初の ## からテキストを取得。これは igapyonv3 の最大の制約です。
			if (line.startsWith("## ")) {
				current.setTitle(line.substring(3));
				break;
			}
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

		// register custom tag.
		config.setSharedVariable("include", new IncludeDirectiveModel(settings));
		config.setSharedVariable("rssfeed", new RSSFeedDirectiveModel(settings));
		config.setSharedVariable("localrss", new LocalRssDirectiveModel(settings));
		config.setSharedVariable("linkdiary", new LinkDiaryDirectiveModel(settings));
		config.setSharedVariable("linksearch", new LinkSearchDirectiveModel(settings));
		config.setSharedVariable("linkshare", new LinkShareDirectiveModel(settings));
		config.setSharedVariable("linkmap", new LinkMapDirectiveModel(settings));
		config.setSharedVariable("linkamazon", new LinkAmazonDirectiveModel(settings));
		config.setSharedVariable("linknext", new LinkNextDirectiveModel(settings));
		config.setSharedVariable("localyearlist", new LocalYearlistDirectiveModel(settings));
		config.setSharedVariable("lastmodified", new LastModifiedDirectiveModel(settings));
		config.setSharedVariable("keywordlist", new KeywordlistDirectiveModel(settings));

		return config;
	}
}
