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

package jp.igapyon.diary.igapyonv3.mdconv.freemarker.directive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.SimpleDirUtil;

/**
 * コンテンツトップへのリンク用のディレクティブモデル
 * 
 * @author Toshiki Iga
 */
public class LinkTopDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public LinkTopDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		// get current directory
		final String sourceName = env.getMainTemplate().getSourceName();

		writer.write(getOutputString(sourceName));

		writer.flush();
	}

	/**
	 * get formatted string by tags.
	 * 
	 * @param sourceName
	 *            source name.
	 * @return formatted string.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public String getOutputString(final String sourceName) throws IOException {
		// get current directory
		final File sourceDir = new File(settings.getRootdir(), sourceName).getCanonicalFile().getParentFile();
		return ("[top]("
				+ SimpleDirUtil.getRelativeUrlIfPossible(settings.getBaseurl() + "/index.html", sourceDir, settings)
				+ ")");
	}
}