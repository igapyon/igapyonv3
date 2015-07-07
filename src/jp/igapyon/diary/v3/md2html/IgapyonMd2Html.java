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
import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * @author Toshiki Iga
 */
public class IgapyonMd2Html {
	public static final String VERSION = "010";

	/**
	 * 
	 * 
	 * FIXME check with trim() or not???
	 * 
	 * @param mdLines
	 * @return
	 */
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

	public void processFile(final File sourceMd, final File targetHtml)
			throws IOException {
		// TODO 最初に Markdown ファイルを解析。ジャンボエリアを確定。description のところまで行を進める。
		// TODO description のところの前後で Markdown ファイルを分割。
		// TODO 別れたファイルを、おのおの html 化。前半はジャンボ処理。META タグの description
		// には画像を含めない処理が必要???

		final String inputMdString = IgapyonV3Util.readTextFile(sourceMd);
		final List<String> mdLines = IgapyonV3Util.stringToList(inputMdString);

		final int separateIndex = getSeparateIndex(mdLines);
		final List<String> mdLinesHead = mdLines.subList(0, separateIndex);
		final List<String> mdLinesBody = mdLines.subList(separateIndex,
				mdLines.size());
		final String mdStringHead = IgapyonV3Util.listToString(mdLinesHead);
		final String mdStringBody = IgapyonV3Util.listToString(mdLinesBody);

		final StringWriter outputHtmlWriter = new StringWriter();
		// TODO first h1 to be title, after text to be description
		// TODO properties should be VO.
		// TODO Description link with Markdown.
		IgapyonV3Util.writePreHtml(outputHtmlWriter, mdStringHead, "Title",
				"Descriptoin", "Toshiki Iga");

		final String bodyMarkdown = IgapyonV3Util.simpleMd2Html(mdStringBody,
				new IgapyonLinkRenderer());
		outputHtmlWriter.write(bodyMarkdown);

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

	public static void main(final String[] args) throws IOException {
		// TODO args to be input, output dir.

		final String source = "./test/data/src";
		final String target = "./test/data/output";
		final boolean recursivedir = true;

		System.out.println("md2html: ver:" + IgapyonMd2Html.VERSION
				+ ", source:[" + source + "], target:[" + target
				+ "], recursivedir=" + recursivedir);

		new IgapyonMd2Html().processDir(source, target, recursivedir);
	}
}