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

package jp.igapyon.diary.v3.mdconv.freemarker.directive;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * 検索エンジンへのリンク用のディレクティブモデル
 * 
 * <@linksearch title="いがぴょん検索" word="いがぴょん" />
 * 
 * <@linksearch title="いがぴょん検索サイト内" word="いがぴょん" site=
 * "https://igapyon.github.io/diary/" />
 * 
 * <@linksearch title="いがぴょんTwitter" word="伊賀敏樹" engine="twitter" />
 * 
 * @author Toshiki Iga
 */
public class LinkSearchDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public LinkSearchDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		if (params.get("word") == null) {
			throw new TemplateModelException("word param is required.");
		}

		// SimpleScalar#toString()
		String titleString = null;
		if (params.get("title") != null) {
			titleString = params.get("title").toString();
		}
		final String wordString = params.get("word").toString();

		String siteString = null;
		if (params.get("site") != null) {
			siteString = params.get("site").toString();
		}

		String engineString = "google";
		if (params.get("engine") != null) {
			engineString = params.get("engine").toString();
		}

		if (titleString == null) {
			// タイトル文字指定がない場合は、規定の文言を生成します。
			titleString = "Search '" + wordString + "' in " + engineString;
		}

		final URLCodec codec = new URLCodec();
		try {
			String qString = "https://www.google.co.jp/#pws=0&q="
					+ (siteString == null ? "" : "site:" + codec.encode(siteString) + "+") + codec.encode(wordString);
			if ("twitter".equals(engineString)) {
				// twitter はサイト内検索はサポートしません。
				// TODO site指定の際にエラー処理が必要か検討。
				qString = "https://twitter.com/search?q="
						+ codec.encode(wordString.indexOf(' ') < 0 ? "#" + wordString : wordString);
			}

			writer.write("[" + titleString + "](" + qString + ")");
		} catch (EncoderException e) {
			throw new IOException(e);
		}

		writer.flush();
	}
}