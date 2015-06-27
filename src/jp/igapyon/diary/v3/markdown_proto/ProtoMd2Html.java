package jp.igapyon.diary.v3.markdown_proto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import org.eclipse.mylyn.wikitext.core.parser.MarkupParser;
import org.eclipse.mylyn.wikitext.core.parser.builder.HtmlDocumentBuilder;
import org.eclipse.mylyn.wikitext.markdown.core.MarkdownLanguage;

public class ProtoMd2Html {
	public static String readTextFile(final File file) throws IOException {
		final StringWriter writer = new StringWriter();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		for (;;) {
			final String line = reader.readLine();
			if (line == null) {
				break;
			}
			writer.write(line);
			writer.write("\n");
		}
		reader.close();
		writer.close();
		return writer.toString();
	}

	public static void main(final String[] args) throws IOException {
		final StringWriter strWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(strWriter);
		final MarkupParser parser = new MarkupParser(new MarkdownLanguage(),
				new HtmlDocumentBuilder(writer));
		parser.parse(readTextFile(new File(
				"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));

		writer.close();
		System.out.println(strWriter.toString());
	}
}
