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
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jp.igapyon.diary.v3.util.IgapyonV3Current;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * Twitter シェアへのリンク用のディレクティブモデル
 * 
 * <@linkshare word="テスト" url="https://igapyon.github.io/diary/" tags=
 * "igapyonv3" />
 * 
 * @author Toshiki Iga
 */
public class LinkShareDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public LinkShareDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		final StringModel smodel = (StringModel) env.getDataModel().get("current");
		final IgapyonV3Current current = (IgapyonV3Current) smodel.getWrappedObject();

		String urlString = current.getUrl();
		if (params.get("url") != null) {
			urlString = params.get("url").toString();
		}

		String wordString = current.getTitle();
		if (params.get("word") != null) {
			wordString = params.get("word").toString();
		}

		String titleString = "Share on Twitter";
		if (params.get("title") != null) {
			titleString = params.get("title").toString();
		}

		// キーワードをタグに展開します。
		String tagsString = "igapyon,diary,いがぴょん";
		for (final String key : current.getKeywordList()) {
			tagsString += ("," + key);
		}
		if (params.get("tags") != null) {
			tagsString = params.get("tags").toString();
		}

		// Twitter以外のシェア方法は現状ありません。
		// String engineString = "twitter";
		// if (params.get("engine") != null) {
		// engineString = params.get("engine").toString();
		// }

		final URLCodec codec = new URLCodec();
		try {
			String qString = "https://twitter.com/intent/tweet?hashtags=" + codec.encode(tagsString) + "&text="
					+ codec.encode(wordString) + "&url=" + codec.encode(urlString);

			writer.write("[" + titleString + "](" + qString + ")");
		} catch (EncoderException e) {
			throw new IOException(e);
		}

		writer.flush();
	}
}