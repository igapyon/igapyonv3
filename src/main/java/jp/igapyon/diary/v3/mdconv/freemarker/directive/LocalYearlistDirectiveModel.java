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
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleDirParser;
import jp.igapyon.diary.v3.util.SimpleDirUtil;

/**
 * ローカルの年リスト用のディレクティブモデル
 * 
 * &lt;@localyearlist /&gt;
 * 
 * @author Toshiki Iga
 */
public class LocalYearlistDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public LocalYearlistDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		// get current directory
		final String sourceName = env.getMainTemplate().getSourceName();
		final File sourceDir = new File(settings.getRootdir(), sourceName).getCanonicalFile().getParentFile();

		final List<File> files = getLocalYearList(settings.getRootdir());

		boolean isFirst = true;
		for (int index = files.size() - 1; index >= 0; index--) {
			if (isFirst) {
				isFirst = false;
			} else {
				writer.write("/ ");
			}

			final File file = files.get(index);

			String url = settings.getBaseurl() + "/" + file.getName() + "/index.html";
			url = SimpleDirUtil.getRelativeUrlIfPossible(url, sourceDir, settings);
			writer.write("[" + file.getName() + "](" + url + ")\n");
		}

		String url = settings.getBaseurl() + "/idxall.html";
		url = SimpleDirUtil.getRelativeUrlIfPossible(url, sourceDir, settings);
		writer.write("/ [ALL](" + url + ")\n");

		writer.flush();
	}

	public static List<File> getLocalYearList(final File rootdir) {
		final SimpleDirParser parser = new SimpleDirParser() {
			final Pattern pat = Pattern.compile("^[0-9][0-9][0-9][0-9]$");

			@Override
			public boolean isProcessTarget(final File file) {
				if (file.isDirectory() == false) {
					return false;
				}
				final Matcher mat = pat.matcher(file.getName());
				if (mat.find()) {
					// 年の形式のみ対象。
					return true;
				}

				return false;
			}
		};

		return parser.listFiles(rootdir, false);
	}
}