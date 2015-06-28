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
				Extensions.FENCED_CODE_BLOCKS /* , PegDownPlugins */);
		final String bodyMarkdown = processor
				.markdownToHtml(IgapyonV3Util.readTextFile(new File(
						"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));
		writer.write(bodyMarkdown);

		IgapyonV3Util.writePostHtml(writer);

		writer.close();
		System.out.println(writer.toString());
	}
}
