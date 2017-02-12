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

package jp.igapyon.diary.igapyonv3.init;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import jp.igapyon.diary.igapyonv3.IgDiaryConstants;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.SimpleDirUtil;

public class IgInitDiaryDir {
	public void process(final File rootdir) throws IOException {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(rootdir);

		process(settings);
	}

	public void process(final IgapyonV3Settings settings) throws IOException {
		{
			final File newdir = new File(settings.getRootdir(), "keyword");
			if (newdir.exists() == false) {
				newdir.mkdirs();
			}
		}

		{
			final File lookupSrcMd = new File(settings.getRootdir(), "settings.src.md");
			if (SimpleDirUtil.existsTargetMdOrSrcMd(lookupSrcMd) == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupSrcMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupSrcMd, IgDiaryConstants.DEFAULT_SETTINGS_SRC_MD, "UTF-8");
			}
		}
		{
			final File lookupTargetMd = new File(settings.getRootdir(), "template-header.md");
			if (lookupTargetMd.exists() == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupTargetMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupTargetMd, IgDiaryConstants.TEMPLATE_HEADER, "UTF-8");
			}
		}
		{
			final File lookupTargetMd = new File(settings.getRootdir(), "template-footer.md");
			if (lookupTargetMd.exists() == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupTargetMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupTargetMd, IgDiaryConstants.TEMPLATE_FOOTER, "UTF-8");
			}
		}

		{
			final File lookupSrcMd = new File(settings.getRootdir(), "index.src.md");
			if (SimpleDirUtil.existsTargetMdOrSrcMd(lookupSrcMd) == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupSrcMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupSrcMd, IgDiaryConstants.DEFAULT_INDEX_SRC_MD, "UTF-8");
			}
		}
		{
			final File lookupSrcMd = new File(settings.getRootdir(), "README.src.md");
			if (SimpleDirUtil.existsTargetMdOrSrcMd(lookupSrcMd) == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupSrcMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupSrcMd, IgDiaryConstants.DEFAULT_INDEX_SRC_MD, "UTF-8");
			}
		}

		{
			final File lookupSrcMd = new File(settings.getRootdir().getAbsolutePath() + "/keyword", "index.src.md");
			if (SimpleDirUtil.existsTargetMdOrSrcMd(lookupSrcMd) == false) {
				System.err.println("IgInitDiaryDir: generate " + lookupSrcMd.getCanonicalPath());
				FileUtils.writeStringToFile(lookupSrcMd, IgDiaryConstants.DEFAULT_KEYWORD_INDEX_SRC_MD, "UTF-8");
			}
		}
	}
}
