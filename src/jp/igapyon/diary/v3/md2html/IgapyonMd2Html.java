/**************************************************************************
 * Copyright (c) 2015, Toshiki Iga, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************** */
/**************************************************************************
 * Copyright 2015 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *********************************************************************** */
package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonLinkRenderer;
import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonPegDownProcessor;
import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonPegDownTagConf;
import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonPegDownUtil;
import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.RootNode;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * @author Toshiki Iga
 */
public class IgapyonMd2Html {
	protected IgapyonMd2HtmlSettings settings = new IgapyonMd2HtmlSettings();

	public static void main(final String[] args) throws IOException {
		new IgapyonMd2HtmlCli().process(args);
	}

	public void processFile(final File sourceMd, final File targetHtml)
			throws IOException {
		// TODO 最初に Markdown ファイルを解析。ジャンボエリアを確定。description のところまで行を進める。
		// TODO description のところの前後で Markdown ファイルを分割。
		// TODO 別れたファイルを、おのおの html 化。前半はジャンボ処理。META タグの description
		// には画像を含めない処理が必要???

		final String inputMdString = IgapyonV3Util.readTextFile(sourceMd);
		final char[] inputMdChars = inputMdString.toCharArray();

		final IgapyonPegDownProcessor processor = new IgapyonPegDownProcessor(
				settings.getPegdownProcessorExtensions());
		final RootNode rootNode = processor.parseMarkdown(inputMdChars);

		final HeaderNode firstHeader = IgapyonPegDownUtil
				.getFistHeader(rootNode);
		final HeaderNode secondHeader = IgapyonPegDownUtil
				.getSecondHeader(rootNode);

		if (firstHeader != null) {
			settings.setHtmlTitle(IgapyonPegDownUtil
					.getElementChildText(firstHeader));
		}
		if (firstHeader != null && secondHeader != null) {
			String desc = IgapyonPegDownUtil.getElementChildText(rootNode,
					firstHeader.getEndIndex(), secondHeader.getStartIndex());
			settings.setHtmlDescription(desc);
		}

		String mdStringHead = inputMdString;
		String mdStringBody = "";
		if (firstHeader != null && secondHeader != null) {
			mdStringHead = inputMdString.substring(0,
					secondHeader.getStartIndex());
			mdStringBody = inputMdString
					.substring(secondHeader.getStartIndex());
		}

		final StringWriter outputHtmlWriter = new StringWriter();
		// TODO first h1 to be title, after text to be description
		// TODO properties should be VO.
		// TODO Description link with Markdown.

		{
			final IgapyonPegDownTagConf tagConf = IgapyonPegDownTagConf
					.getDefault();

			// set h1 to null for Jumbotron.
			tagConf.setAttrClassValue("h1", null);
			IgapyonV3Util.writePreHtml(settings, tagConf, outputHtmlWriter,
					mdStringHead, "Toshiki Iga");
		}

		{
			final IgapyonPegDownTagConf tagConf = IgapyonPegDownTagConf
					.getDefault();
			final String bodyMarkdown = IgapyonV3Util.simpleMd2Html(settings,
					tagConf, mdStringBody, new IgapyonLinkRenderer());
			outputHtmlWriter.write(bodyMarkdown);
		}

		IgapyonV3Util.writePostHtml(outputHtmlWriter);

		outputHtmlWriter.close();

		if (targetHtml.getParentFile().exists() == false) {
			targetHtml.getParentFile().mkdirs();
		}

		if (IgapyonV3Util.checkWriteNecessary("md2html",
				outputHtmlWriter.toString(), targetHtml) == false) {
			// no need to write
			return;
		}

		IgapyonV3Util.writeHtmlFile(outputHtmlWriter.toString(), targetHtml);
	}

	public void processDir(final File sourceMdDir, final File targetHtmlDir,
			final boolean recursivedir) throws IOException {
		// TODO check to be another method.
		if (sourceMdDir.exists() == false) {
			System.err.println("md2html: source dir not exists: "
					+ sourceMdDir.getAbsolutePath());
			return;
		}
		if (sourceMdDir.isDirectory() == false) {
			System.err.println("md2html: source dir is not dir: "
					+ sourceMdDir.getAbsolutePath());
			return;
		}

		if (targetHtmlDir.exists() == false) {
			System.err.println("md2html: target dir not exists: "
					+ targetHtmlDir.getAbsolutePath());
			return;
		}
		if (targetHtmlDir.isDirectory() == false) {
			System.err.println("md2html: target dir is not dir: "
					+ targetHtmlDir.getAbsolutePath());
			return;
		}

		new IgapyonDirProcessor() {
			@Override
			public void parseFile(final File baseDir, final File file)
					throws IOException {
				final String subFile = getSubdir(baseDir, file);
				processFile(
						file,
						new File(targetHtmlDir + "/"
								+ replaceExt(subFile, ".html")));
			}
		}.parseDir(sourceMdDir, ".md", recursivedir);
	}

	public void processDir(final String sourceMdDirString,
			final String targetHtmlDirString, final boolean recursivedir)
			throws IOException {
		final File sourceMdDir = new File(sourceMdDirString);
		final File targetHtmlDir = new File(targetHtmlDirString);

		processDir(sourceMdDir, targetHtmlDir, recursivedir);
	}
}