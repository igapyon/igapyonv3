package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * @author Toshiki Iga
 */
public class IgapyonMd2Html {
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
				new MyLinkRenderer());
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

	public void processDir(final File sourceMdDir, final File targetHtmlDir)
			throws IOException {
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
			public void parseFile(File baseDir, File file) throws IOException {
				final String subFile = getSubdir(baseDir, file);
				processFile(
						file,
						new File(targetHtmlDir + "/"
								+ replaceExt(subFile, ".html")));
			}
		}.parseDir(sourceMdDir, ".md");
	}

	public void processDir(final String sourceMdDirString,
			final String targetHtmlDirString) throws IOException {
		final File sourceMdDir = new File(sourceMdDirString);
		final File targetHtmlDir = new File(targetHtmlDirString);

		processDir(sourceMdDir, targetHtmlDir);
	}

	public static void main(final String[] args) throws IOException {
		// TODO args to be input, output dir.
		new IgapyonMd2Html()
				.processDir("./test/data/src", "./test/data/output");
	}
}