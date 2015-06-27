package jp.igapyon.diary.v3.markdown_proto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.PegDownProcessor;

public class ProtoMd2Html2 {
	public static void main(final String[] args) throws IOException {

		final StringWriter strWriter = new StringWriter();
		final BufferedWriter writer = new BufferedWriter(strWriter);

		final PegDownProcessor processor = new PegDownProcessor();
		String aaa = processor
				.markdownToHtml(IgapyonV3Util.readTextFile(new File(
						"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));

		System.out.println(aaa);
	}
}
