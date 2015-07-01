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
		final StringWriter writer = new StringWriter();
		IgapyonV3Util.writePreHtml(writer, "Title", "Descriptoin",
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
		writer.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(writer);

		writer.close();

		if (targetHtml.getParentFile().exists() == false) {
			targetHtml.getParentFile().mkdirs();
		}

		if (targetHtml.exists() == false) {
			System.out.println("md2html: A: " + targetHtml.getCanonicalPath());
		} else {
			final String origOutputHtmlString = IgapyonV3Util
					.readTextFile(targetHtml);
			if (writer.toString().equals(origOutputHtmlString)) {
				System.out.println("md2html: N: "
						+ targetHtml.getCanonicalPath());
				// nothing changed. then return here!!!
				return;
			} else {
				System.out.println("md2html: U: "
						+ targetHtml.getCanonicalPath());
			}
		}

		IgapyonV3Util.writeHtmlFile(writer.toString(), targetHtml);
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
		new IgapyonMd2Html()
				.processDir("./test/data/src", "./test/data/output");
	}
}
