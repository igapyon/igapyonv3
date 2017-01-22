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
import java.util.Map;

import org.apache.commons.io.FileUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * ヘッダー。
 * 
 * @author Toshiki Iga
 */
public class HeaderDirectiveModel implements TemplateDirectiveModel {
	private IgapyonV3Settings settings = null;

	public HeaderDirectiveModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void execute(final Environment env, @SuppressWarnings("rawtypes") final Map params,
			final TemplateModel[] loopVars, final TemplateDirectiveBody body) throws TemplateException, IOException {
		final BufferedWriter writer = new BufferedWriter(env.getOut());

		if (true) {
			// type="diary"
			// 日記ヘッダーの展開モード

			final String sourceName = env.getMainTemplate().getSourceName();

			// 日記ノードの処理。
			String year1 = "20";
			String year2 = sourceName.substring(2, 4);
			if (year2.startsWith("9")) {
				year1 = "19";
			}

			String month = sourceName.substring(4, 6);
			String day = sourceName.substring(6, 8);

			// FIXME そもそもヘッダーも <@header />
			// FIXME とかで表現できるような気がしてきた。そして遅延展開すると変数が利用可能になる。

			writer.write("[top](${settings.baseurl}/) \n");
			// FIXME index も current.index のような値がほしい。
			writer.write(" / [index](${settings.baseurl}/" + year1 + year2 + "/index.html) \n");

			writer.write(" / <@linkprev /> \n");
			writer.write(" / <@linknext /> \n");

			writer.write(" / [target](${current.url}) \n");
			// FIXME ソースも current.sourceurl などほしい。名前は後でよく考えよう。
			writer.write(" / [source](https://github.com/igapyon/diary/blob/gh-pages/" + year1 + year2 + "/ig" + year2
					+ month + day + ".html.src.md) \n");
			writer.write("\n");

			// ヘッダ追加
			writer.write(year1 + year2 + "-" + month + "-" + day + " diary: ${current.title}\n");

			{
				// TODO 固定部分より上の展開ができていません。良い実装方法を考えましょう。
				final File fileTemplate = new File(settings.getRootdir(), "template-header.md");
				if (fileTemplate.exists()) {
					final String template = FileUtils.readFileToString(fileTemplate, "UTF-8");
					writer.write(template);
					if (template.endsWith("\n") == false) {
						writer.write("\n");
					}
				} else {
					System.err.println("template-header.md not found.:" + fileTemplate.getCanonicalPath());
					writer.write("===================================\n");
					writer.write("<#-- template-header.md not found. -->\n");
				}
			}

			writer.write("\n");
		}

		writer.flush();
	}

}