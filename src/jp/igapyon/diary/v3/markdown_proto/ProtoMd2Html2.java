package jp.igapyon.diary.v3.markdown_proto;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class ProtoMd2Html2 {
	public static void main(final String[] args) throws IOException {
		final StringWriter writer = new StringWriter();
		IgapyonV3Util.writePreHtml(writer, "Title", "Descriptoin",
				"Toshiki Iga");

		final PegDownProcessor processor = new PegDownProcessor(
				Extensions.AUTOLINKS | Extensions.STRIKETHROUGH
						| Extensions.FENCED_CODE_BLOCKS | Extensions.TABLES
						| Extensions.WIKILINKS /*
												 * , PegDownPlugins
												 */);
		final String bodyMarkdown = processor.markdownToHtml(IgapyonV3Util
				.readTextFile(new File("./test/data/src/test001.md")),
				new MyLinkRenderer());
		writer.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(writer);

		writer.close();

		IgapyonV3Util.writeHtmlFile(writer.toString(), new File(
				"./test/data/output/test001.html"));
		System.out.println(writer.toString());
	}
}
