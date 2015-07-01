package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class IgapyonMd2Html {
	public void processFile(final File sourceMd, final File targetHtml)
			throws IOException {
		final StringWriter outputHtmlWriter = new StringWriter();
		// TODO first h1 to be title, after text to be description
		IgapyonV3Util.writePreHtml(outputHtmlWriter, "Title", "Descriptoin",
				"Toshiki Iga");

		final PegDownProcessor processor = new PegDownProcessor(
				Extensions.AUTOLINKS | Extensions.STRIKETHROUGH
						| Extensions.FENCED_CODE_BLOCKS | Extensions.TABLES
						| Extensions.WIKILINKS /*
												 * , PegDownPlugins
												 */);

		final String inputMdString = IgapyonV3Util.readTextFile(sourceMd);

		final String bodyMarkdown = processor.markdownToHtml(inputMdString,
				new MyLinkRenderer());
		outputHtmlWriter.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(outputHtmlWriter);

		outputHtmlWriter.close();

		if (targetHtml.getParentFile().exists() == false) {
			targetHtml.getParentFile().mkdirs();
		}

		if (checkWriteNecessary(outputHtmlWriter.toString(), targetHtml) == false) {
			// no need to write
			return;
		}

		IgapyonV3Util.writeHtmlFile(outputHtmlWriter.toString(), targetHtml);
	}

	protected boolean checkWriteNecessary(final String outputData,
			final File targetHtml) throws IOException {
		if (targetHtml.exists() == false) {
			System.out.println("md2html: A: " + targetHtml.getCanonicalPath());
			return true;
		} else {
			final String origOutputHtmlString = IgapyonV3Util
					.readTextFile(targetHtml);
			if (outputData.equals(origOutputHtmlString)) {
				System.out.println("md2html: N: "
						+ targetHtml.getCanonicalPath());
				return false;
			} else {
				System.out.println("md2html: U: "
						+ targetHtml.getCanonicalPath());
				return true;
			}
		}
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
