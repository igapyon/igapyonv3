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
import java.util.List;

import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonLinkRenderer;
import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonPegDownProcessor;
import jp.igapyon.diary.v3.md2html.pegdownext.IgapyonPegDownTagConf;
import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TextNode;

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

	/**
	 * 
	 * 
	 * FIXME check with trim() or not???
	 * 
	 * @deprecated
	 * @param mdLines
	 * @return
	 */
	@Deprecated
	protected int getSeparateIndex(final List<String> mdLines) {
		int separateIndex = 0;
		// 切れ目サーチ
		if (mdLines.size() <= 2) {
			// ジャンボ部分なし
			return 0;
		}
		if (mdLines.get(0).startsWith("#")) {
			// h があった!
			if (mdLines.get(1).startsWith("#")) {
				// でも、次の行も h。ここは切れ目だ
				return 1;
			}
			// 1 まで進める
			separateIndex = 1;
		} else if (mdLines.get(1).startsWith("===")) {
			// これも h1
			if (mdLines.get(2).startsWith("#")) {
				// ここが切れ目
				return 2;
			}
			// 2 まで進める
			separateIndex = 2;
		}

		for (; separateIndex < mdLines.size(); separateIndex++) {
			if (mdLines.get(separateIndex).startsWith("#")) {
				// ここでヘッド部分と分離します。
				return separateIndex;
			}
			if (separateIndex + 1 < mdLines.size()) {
				if (mdLines.get(separateIndex + 1).startsWith("===")
						|| mdLines.get(separateIndex + 1).startsWith("---")) {
					// ここでヘッド部分と分離します。
					return separateIndex;
				}
			}
		}
		// ヘッドのみで、ボディがない
		return mdLines.size() - 1;
	}

	/**
	 * TODO Move to commons.
	 */
	public HeaderNode getFistHeader(final RootNode rootNode) {
		for (Node node : rootNode.getChildren()) {
			if (node instanceof HeaderNode) {
				return (HeaderNode) node;
			}
		}
		return null;
	}

	/**
	 * TODO Move to commons.
	 */
	public HeaderNode getSecondHeader(final RootNode rootNode) {
		boolean isFirstHeader = true;
		for (Node node : rootNode.getChildren()) {
			if (node instanceof HeaderNode) {
				if (isFirstHeader) {
					isFirstHeader = false;
				} else {
					return (HeaderNode) node;
				}
			}
		}
		return null;
	}

	/**
	 * todo move
	 * 
	 * @param node
	 * @return
	 */
	public String getElementText(final Node node) {
		if (node instanceof AnchorLinkNode) {
			final AnchorLinkNode lookNode = (AnchorLinkNode) node;
			return lookNode.getText();
		} else if (node instanceof ParaNode) {
			final ParaNode lookNode = (ParaNode) node;
			return getElementChildText(lookNode);
		} else if (node instanceof SuperNode) {
			final SuperNode lookNode = (SuperNode) node;
			return getElementChildText(lookNode);
		} else if (node instanceof TextNode) {
			final TextNode lookNode = (TextNode) node;
			return lookNode.getText();
		} else {
			System.out.println("TRACE: unknown node: " + node.toString());
			return "";
		}
	}

	public String getElementChildText(final Node rootNode) {
		return getElementChildText(rootNode, 0, Integer.MAX_VALUE);
	}

	public String getElementChildText(final Node rootNode,
			final int startIndex, final int endIndex) {
		StringBuilder builder = null;
		for (Node node : rootNode.getChildren()) {
			if (node.getStartIndex() < startIndex) {
				continue;
			}
			if (node.getEndIndex() > endIndex) {
				continue;
			}
			if (builder == null) {
				builder = new StringBuilder();
			}
			builder.append(getElementText(node));
		}
		if (builder == null) {
			return null;
		}
		return builder.toString();
	}

	public void processFile(final File sourceMd, final File targetHtml)
			throws IOException {
		// TODO 最初に Markdown ファイルを解析。ジャンボエリアを確定。description のところまで行を進める。
		// TODO description のところの前後で Markdown ファイルを分割。
		// TODO 別れたファイルを、おのおの html 化。前半はジャンボ処理。META タグの description
		// には画像を含めない処理が必要???

		final String inputMdString = IgapyonV3Util.readTextFile(sourceMd);
		final char[] inputMdChars = inputMdString.toCharArray();

		// aaaaaaaaaaaaaaaaaaaaaaa
		final IgapyonPegDownProcessor processor = new IgapyonPegDownProcessor(
				settings.getPegdownProcessorExtensions());
		final RootNode rootNode = processor.parseMarkdown(inputMdChars);

		final HeaderNode firstHeader = getFistHeader(rootNode);
		final HeaderNode secondHeader = getSecondHeader(rootNode);

		if (firstHeader != null) {
			settings.setHtmlTitle(getElementChildText(firstHeader));
		}
		if (firstHeader != null && secondHeader != null) {
			String desc = getElementChildText(rootNode,
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