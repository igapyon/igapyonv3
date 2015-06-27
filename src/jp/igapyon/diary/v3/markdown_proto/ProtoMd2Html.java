package jp.igapyon.diary.v3.markdown_proto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;

public class ProtoMd2Html {
	public static void main(final String[] args) throws IOException {
		final StringWriter strWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(strWriter);
		final MarkupParser parser = new MarkupParser(new MarkdownLanguage(),
				new HtmlDocumentBuilder(writer));
		parser.parse("An h1 header\n============\nParagraphs are separated by a blank line.\n  * this one");

		writer.close();
		System.out.println(strWriter.toString());
	}
}
