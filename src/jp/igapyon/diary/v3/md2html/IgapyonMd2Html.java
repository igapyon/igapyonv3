package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonDirProcessor;
import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class IgapyonMd2Html {
	public void processFile(final File inputMd, final File outputHtml)
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

		final String inputMdString = IgapyonV3Util.readTextFile(inputMd);

		final String bodyMarkdown = processor.markdownToHtml(inputMdString,
				new MyLinkRenderer());
		writer.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(writer);

		writer.close();

		if (outputHtml.getParentFile().exists() == false) {
			outputHtml.getParentFile().mkdirs();
		}

		{
		}

		if (outputHtml.exists() == false) {
			System.out.println("md2html: A: " + outputHtml.getCanonicalPath());
		} else {
			final String origOutputHtmlString = IgapyonV3Util
					.readTextFile(outputHtml);
			if (writer.toString().equals(origOutputHtmlString)) {
				System.out.println("md2html: N: "
						+ outputHtml.getCanonicalPath());
				// return here!!!
				return;
			} else {
				System.out.println("md2html: U: "
						+ outputHtml.getCanonicalPath());
			}
		}

		IgapyonV3Util.writeHtmlFile(writer.toString(), outputHtml);
	}

	public void process() throws IOException {
		final String targetDir = "./test/data/output";
		new IgapyonDirProcessor() {
			@Override
			public void parseFile(File baseDir, File file) throws IOException {
				final String subFile = getSubdir(baseDir, file);
				processFile(
						file,
						new File(targetDir + "/" + replaceExt(subFile, ".html")));
			}
		}.parseDir(new File("./test/data/src"), ".md");
	}

	public static void main(final String[] args) throws IOException {
		new IgapyonMd2Html().process();
	}
}
