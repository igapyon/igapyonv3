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

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import jp.igapyon.util.IgapyonFileUtil;

/**
 * FreeMarker 用のユーティリティクラス。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3FreeMarkerUtil {

	public static void main(String[] args) throws IOException {
		final Map<String, Object> templateData = new HashMap<String, Object>();
		templateData.put("user", "Taro Yamada");

		// for Ant build.xml
		templateData.put("encoding", "${encoding}");

		{
			// DummyVOMvnProject obj = new DummyVOMvnProject();
			// for Maven pom.xml
			// ${project.build.directory}
			// templateData.put("project", obj);
		}

		IgapyonV3FreeMarkerUtil.process(new File("."), new File("test/data/hatena/ig161227.html.src.md"), templateData);
	}

	public static String process(File rootdir, File file, final Map<String, Object> templateData) throws IOException {
		// do canonical
		rootdir = rootdir.getCanonicalFile();
		file = file.getCanonicalFile();

		final String relativePath = IgapyonFileUtil.getRelativePath(rootdir, file);

		// newest version at this point.
		final Configuration config = new Configuration(Configuration.VERSION_2_3_25);
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// DISABLE auto escape
		config.setAutoEscapingPolicy(Configuration.DISABLE_AUTO_ESCAPING_POLICY);

		// only for camel case not snake one.
		config.setNamingConvention(Configuration.CAMEL_CASE_NAMING_CONVENTION);

		// ??? set API disable
		config.setAPIBuiltinEnabled(true);

		// only for newer one.
		config.setClassicCompatible(false);

		config.setLazyAutoImports(false);

		// diary system not need localize lookup
		config.setLocalizedLookup(false);

		config.setLogTemplateExceptions(false);
		config.setRecognizeStandardFileExtensions(false);
		config.setWhitespaceStripping(false);

		// set my custom template loader.
		config.setTemplateLoader(new IgapyonV3TemplateLoader());

		// register custom tag.
		config.setSharedVariable("rssfeed", new RSSFeedDirectiveModel());
		config.setSharedVariable("localrss", new LocalRssDirectiveModel());
		config.setSharedVariable("linkdiary", new LinkDiaryDirectiveModel());
		config.setSharedVariable("linksearch", new LinkSearchDirectiveModel());
		config.setSharedVariable("linkamazon", new LinkAmazonDirectiveModel());

		final Template templateBase = config.getTemplate(relativePath);
		try {
			final StringWriter writer = new StringWriter();
			templateBase.process(templateData, writer);
			return writer.toString();
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}
}
