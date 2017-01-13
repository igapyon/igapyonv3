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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * Map へのリンク用のディレクティブモデル
 * 
 * @author Toshiki Iga
 */
public class LinkMapDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public LinkMapDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		if (params.get("lat") == null) {
			throw new TemplateModelException("lat param is required.");
		}
		if (params.get("lon") == null) {
			throw new TemplateModelException("lon param is required.");
		}

		final String latString = params.get("lat").toString();
		final String lonString = params.get("lon").toString();

		String titleString = "地図で表示";
		if (params.get("title") != null) {
			titleString = params.get("title").toString();
		}

		String qString = "https://openstreetmap.jp/map#zoom=17&lat=" + latString + "&lon=" + lonString
				+ "&layers=00BFF";

		writer.write("[" + titleString + "](" + qString + ")");

		writer.flush();
	}
}