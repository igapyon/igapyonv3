package jp.igapyon.diary.v3.markdown_proto;

import java.io.File;
import java.io.IOException;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class ProtoMd2Html2 {
	public static void main(final String[] args) throws IOException {
		final PegDownProcessor processor = new PegDownProcessor(
				Extensions.FENCED_CODE_BLOCKS /* , PegDownPlugins */);
		String aaa = processor
				.markdownToHtml(IgapyonV3Util.readTextFile(new File(
						"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));

		System.out.println(aaa);
	}
}
