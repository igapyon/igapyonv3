package jp.igapyon.diary.v3.markdown_proto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;

public class ProtoMd2Html {
	public static void main(final String[] args) throws IOException {

		final StringWriter strWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(strWriter);

		final HtmlDocumentBuilder builder = new HtmlDocumentBuilder(writer);
		// without head and body.
		builder.setEmitAsDocument(false);

		final MarkupParser parser = new MarkupParser(new MarkdownLanguage(),
				builder);
		parser.parse(IgapyonV3Util.readTextFile(new File(
				"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));

		writer.close();
		System.out.println(strWriter.toString());
	}
}
