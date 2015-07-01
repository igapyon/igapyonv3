package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class Md2Html2 {
	public void myProcess(final File inputMd, final File outputHtml)
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
		final String bodyMarkdown = processor.markdownToHtml(
				IgapyonV3Util.readTextFile(inputMd), new MyLinkRenderer());
		writer.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(writer);

		writer.close();

		if (outputHtml.getParentFile().exists() == false) {
			outputHtml.getParentFile().mkdirs();
		}

		IgapyonV3Util.writeHtmlFile(writer.toString(), outputHtml);
		// System.out.println(writer.toString());
	}

	public void process() throws IOException {
		final String targetDir = "./test/data/output";
		new AbstractParseDir() {
			@Override
			public void parseFile(File baseDir, File file) throws IOException {
				final String subFile = getSubdir(baseDir, file);
				myProcess(
						file,
						new File(targetDir + "/" + replaceExt(subFile, ".html")));
			}
		}.parseDir(new File("./test/data/src"), ".md");
	}

	public static void main(final String[] args) throws IOException {
		new Md2Html2().process();
	}
}
