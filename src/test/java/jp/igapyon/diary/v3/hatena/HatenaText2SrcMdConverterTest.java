package jp.igapyon.diary.v3.hatena;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.v3.mdconv.DiarySrcMd2MdConverter;

public class HatenaText2SrcMdConverterTest {

	@Test
	public void test() throws Exception {
		final File rootdir = new File("./test/data/hatena/");

		new HatenaText2SrcMdConverter().processDir(rootdir);

		new DiarySrcMd2MdConverter().processDir(rootdir);
	}
}
