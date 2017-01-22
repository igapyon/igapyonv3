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
import java.util.ArrayList;
import java.util.List;
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
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleDirUtil;

/**
 * 次リンク
 * 
 * @author Toshiki Iga
 */
public class LinkPrevDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	/**
	 * notice static!!! danger
	 */
	protected static List<SyndEntry> synEntryList = null;

	public LinkPrevDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		final String sourceName = env.getMainTemplate().getSourceName();

		String url = SimpleDirUtil.file2Url(new File(settings.getRootdir(), sourceName), settings);
		{
			// TODO共通関数化、あるいは変数からurlを取得せよ。
			if (url.endsWith(".src.md")) {
				url = url.substring(0, url.length() - ".src.md".length());
			}
		}

		ensureLoadAtomXml();

		final int entryIndex = findTargetAtomEntry(url);

		if (entryIndex < 0 || entryIndex > synEntryList.size() - 2) {
			writer.write("prev");
		} else {
			writer.write("[prev](" + synEntryList.get(entryIndex + 1).getLink() + ")");
		}

		writer.flush();
	}

	/**
	 * 処理の過程で必要になる各種 atom ファイルをロード済みかどうか念押し確認します。
	 * 
	 * @throws IOException
	 */
	public void ensureLoadAtomXml() throws IOException {
		if (synEntryList == null) {
			synEntryList = new ArrayList<SyndEntry>();
			// FIXME ルートではないほうがよいとおもう。
			final File atomXmlFile = new File(settings.getRootdir(), "atom.xml");
			if (atomXmlFile.exists() == false) {
				return;
			}
			try {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(atomXmlFile)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					synEntryList.add(entry);
				}
			} catch (FeedException e) {
				throw new IOException(e);
			}
		}
	}

	public int findTargetAtomEntry(final String url) throws IOException {
		if (synEntryList == null) {
			return -1;
		}

		for (int index = 0; index < synEntryList.size(); index++) {
			final SyndEntry entry = synEntryList.get(index);
			if (url.equals(entry.getLink())) {
				return index;
			}
		}

		return -1;
	}
}