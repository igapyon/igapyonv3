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

package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * 指定されたファイルの一覧部分を更新する仕組みです。
 * 
 * TODO 他のクラスに併合されるべき処理と考えます。
 */
public class ProcessIndexListing {
	private IgapyonV3Settings settings = null;

	public ProcessIndexListing(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void process(File fileTarget, final List<DiaryItemInfo> diaryItemInfoList) throws IOException {
		fileTarget = fileTarget.getCanonicalFile();
		if (fileTarget.getName().endsWith(".src.md") == false) {
			return;
		}
		final String newName = fileTarget.getName().substring(0, fileTarget.getName().length() - 7) + ".md";

		String wrk = "";
		for (DiaryItemInfo itemInfo : diaryItemInfoList) {
			wrk = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrk;
		}

		String wrkRecent = "";
		{
			int diaryListupCount = 15;

			final List<DiaryItemInfo> recentItemInfoList = new ArrayList<DiaryItemInfo>();

			for (int index = diaryItemInfoList.size() - 1; index >= 0; index--) {
				final DiaryItemInfo itemInfo = diaryItemInfoList.get(index);
				diaryListupCount--;
				recentItemInfoList.add(itemInfo);
				if (diaryListupCount <= 0) {
					break;
				}
			}

			// sort again.
			Collections.sort(recentItemInfoList, new DiaryItemInfoComparator());

			for (final DiaryItemInfo itemInfo : recentItemInfoList) {
				wrkRecent = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrkRecent;
			}
		}

		String publickeyText = "";
		try {
			final URL atomURL = new URL("http://www.publickey1.jp/atom.xml");
			final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(atomURL));

			publickeyText += "#### [" + StringEscapeUtils.escapeXml11(synFeed.getTitle()) + "](" + synFeed.getLink()
					+ ")\n\n";

			for (Object lookup : synFeed.getEntries()) {
				final SyndEntry entry = (SyndEntry) lookup;
				publickeyText += "* [" + StringEscapeUtils.escapeXml11(entry.getTitle()) + "](" + entry.getLink()
						+ ")\n";
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		}

		String target = FileUtils.readFileToString(fileTarget, "UTF-8");
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist}", wrk);
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist.recent}", wrkRecent);
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist.publickey}", publickeyText);
		FileUtils.writeStringToFile(new File(fileTarget.getParentFile() + "/" + newName), target, "UTF-8");
	}
}
