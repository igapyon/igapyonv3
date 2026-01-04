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

package jp.igapyon.diary.igapyonv3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Node;

import jp.igapyon.diary.igapyonv3.md2html.flexmark.FlexmarkUtil;
import jp.igapyon.diary.igapyonv3.md2html.tagconf.IgapyonMdTagConf;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.SimpleDirUtil;
import jp.igapyon.diary.util.IgFileUtil;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * ※初期の github gh-pages 対応では、これは利用しません。
 * 
 * @author Toshiki Iga
 */
public class IgapyonMd2Html {
	protected IgapyonMd2HtmlSettings settings = new IgapyonMd2HtmlSettings();
	protected String baseurl;
	protected File outputRootDir;

	public IgapyonMd2Html() {
		// default constructor
	}

	public IgapyonMd2Html(final IgapyonV3Settings settings) {
		this.baseurl = settings.getBaseurl();
	}

	public static void main(final String[] args) throws IOException {
		new IgapyonMd2HtmlCli().process(args);
	}

	public void processFile(final File sourceMd, final File targetHtml) throws IOException {
		// TODO 最初に Markdown ファイルを解析。ジャンボエリアを確定。description のところまで行を進める。
		// TODO description のところの前後で Markdown ファイルを分割。
		// TODO 別れたファイルを、おのおの html 化。前半はジャンボ処理。META タグの description
		// には画像を含めない処理が必要???

		final String inputMdString = IgapyonV3Util.readTextFile(sourceMd);
		final Node document = FlexmarkUtil.getParser().parse(inputMdString);

		final Heading firstHeader = FlexmarkUtil.getFirstHeading(document);
		final Heading secondHeader = FlexmarkUtil.getSecondHeading(document);

		if (firstHeader != null) {
			settings.setHtmlTitle(FlexmarkUtil.collectText(firstHeader));
		}
		if (firstHeader != null && secondHeader != null) {
			final String descMd = inputMdString.substring(firstHeader.getEndOffset(), secondHeader.getStartOffset());
			settings.setHtmlDescription(FlexmarkUtil.collectTextFromMarkdown(descMd));
		}

		String mdStringHead = inputMdString;
		String mdStringBody = "";
		if (firstHeader != null && secondHeader != null) {
			mdStringHead = inputMdString.substring(0, secondHeader.getStartOffset());
			mdStringBody = inputMdString.substring(secondHeader.getStartOffset());
		}

		final StringWriter outputHtmlWriter = new StringWriter();
		if (baseurl != null && outputRootDir != null) {
			settings.setHtmlCanonical(buildCanonicalUrl(targetHtml));
		}
		// TODO first h1 to be title, after text to be description
		// TODO properties should be VO.
		// TODO Description link with Markdown.

		{
			final IgapyonMdTagConf tagConf = IgapyonMdTagConf.getDefault();

			// set h1 to null for Jumbotron.
			tagConf.setAttrClassValue("h1", "text-3xl font-bold tracking-tight");
			IgapyonV3Util.writePreHtml(settings, tagConf, outputHtmlWriter, mdStringHead, "Toshiki Iga");
		}

		{
			final IgapyonMdTagConf tagConf = IgapyonMdTagConf.getDefault();
			final String bodyMarkdown = IgapyonV3Util.simpleMd2Html(settings, tagConf, mdStringBody);
			outputHtmlWriter.write(bodyMarkdown);
		}

		IgapyonV3Util.writePostHtml(outputHtmlWriter);

		outputHtmlWriter.close();

		if (targetHtml.getParentFile().exists() == false) {
			targetHtml.getParentFile().mkdirs();
		}

		if (IgFileUtil.checkWriteNecessary("md2html", outputHtmlWriter.toString(), targetHtml) == false) {
			// no need to write
			return;
		}

		IgapyonV3Util.writeHtmlFile(outputHtmlWriter.toString(), targetHtml);
	}

	public void processDir(final File sourceMdDir, final File targetHtmlDir, final boolean recursivedir)
			throws IOException {
		outputRootDir = targetHtmlDir;
		// TODO check to be another method.
		if (sourceMdDir.exists() == false) {
			System.err.println("md2html: source dir not exists: " + sourceMdDir.getAbsolutePath());
			return;
		}
		if (sourceMdDir.isDirectory() == false) {
			System.err.println("md2html: source dir is not dir: " + sourceMdDir.getAbsolutePath());
			return;
		}

		if (targetHtmlDir.exists() == false) {
			System.err.println("md2html: target dir not exists: " + targetHtmlDir.getAbsolutePath());
			return;
		}
		if (targetHtmlDir.isDirectory() == false) {
			System.err.println("md2html: target dir is not dir: " + targetHtmlDir.getAbsolutePath());
			return;
		}

		new IgapyonDirProcessor() {
			@Override
			public void parseFile(final File baseDir, final File file) throws IOException {
				final String subFile = getSubdir(baseDir, file);
				processFile(file, new File(targetHtmlDir + "/" + replaceExt(subFile, ".html")));
			}
		}.parseDir(sourceMdDir, ".md", recursivedir);
	}

	public void processDir(final String sourceMdDirString, final String targetHtmlDirString, final boolean recursivedir)
			throws IOException {
		final File sourceMdDir = new File(sourceMdDirString);
		final File targetHtmlDir = new File(targetHtmlDirString);

		processDir(sourceMdDir, targetHtmlDir, recursivedir);
	}

	private String buildCanonicalUrl(final File targetHtml) throws IOException {
		final String relativePath = SimpleDirUtil.getRelativePath(outputRootDir, targetHtml);
		if (relativePath.length() == 0) {
			return baseurl;
		}
		return baseurl + "/" + relativePath.replace('\\', '/');
	}
}
