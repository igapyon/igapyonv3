package jp.igapyon.diary.v3.proto;

import info.bliki.wiki.model.WikiModel;

import java.io.IOException;
import java.io.StringWriter;

public class ProtoWiki2Html {
	public static void main(final String[] args) {
		final StringWriter writer = new StringWriter();
		try {
			WikiModel.toHtml("もんげーシンプルな [[Hello World]] wiki タグなう.", writer);
			writer.flush();
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		System.out.println(writer.toString());
	}
}
