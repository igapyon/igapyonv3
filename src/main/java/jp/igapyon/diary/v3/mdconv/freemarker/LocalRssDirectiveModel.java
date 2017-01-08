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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleRomeUtil;

/**
 * RSS Feed 用のディレクティブモデル
 * 
 * <@localrss filename="atom.xml" />
 * 
 * @author Toshiki Iga
 */
public class LocalRssDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	/**
	 * キャッシュ用オブジェクト。
	 */
	protected final Map<String, String> cacheAtomStringMap = new HashMap<String, String>();

	public LocalRssDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		final String sourceName = env.getMainTemplate().getSourceName();
		final File sourceDir = new File(settings.getRootdir(), sourceName).getCanonicalFile().getParentFile();

		String filename = "atom.xml";

		if (params.get("filename") != null) {
			// SimpleScalar#toString()
			filename = params.get("filename").toString();
		}

		int maxcount = 10;
		if (params.get("maxcount") != null) {
			final String maxcountString = params.get("maxcount").toString();
			if (NumberUtils.isParsable(maxcountString)) {
				maxcount = NumberUtils.toInt(maxcountString);
			}
		}

		{
			if (cacheAtomStringMap.get(filename) == null) {
			}
			// writer.write(cacheAtomStringMap.get(filename));
		}

		{
			final File atomFile = new File(sourceDir, filename).getCanonicalFile();
			if (cacheAtomStringMap.get(atomFile.getAbsolutePath()) == null) {
				// FIXME maxcount impl
				cacheAtomStringMap.put(atomFile.getAbsolutePath(), SimpleRomeUtil.atomxml2String(atomFile));
			}
			writer.write(cacheAtomStringMap.get(atomFile.getAbsolutePath()));
		}

		writer.flush();
	}

}