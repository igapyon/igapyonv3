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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * ローカル日記へのリンク用のディレクティブモデル
 * 
 * <@linkdiary date="2017-01-02" />
 * 
 * @author Toshiki Iga
 */
public class LinkDiaryDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	/**
	 * キャッシュ用オブジェクト。
	 */
	protected Map<String, SyndEntry> cacheAtomMap = null;

	public LinkDiaryDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		if (cacheAtomMap == null) {
			cacheAtomMap = new HashMap<String, SyndEntry>();

			try {
				// ルート直下の atom.xml を利用します。
				final SyndFeed synFeed = new SyndFeedInput()
						.build(new XmlReader(new FileInputStream(new File(settings.getRootdir(), "atom.xml"))));

				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					cacheAtomMap.put(entry.getTitle().substring(0, 10), entry);
				}
			} catch (FeedException e) {
				throw new IOException(e);
			}
		}

		if (params.get("date") == null) {
			throw new TemplateModelException("date param is required.");
		}

		// SimpleScalar#toString()
		final String dateString = params.get("date").toString();

		{
			final SyndEntry entry = cacheAtomMap.get(dateString);
			if (entry == null) {
				writer.write("ERROR: no such [" + dateString + "] diary.");
			} else {
				writer.write("[" + entry.getTitle() + "](" + entry.getLink() + ")");
			}
		}

		writer.flush();
	}
}