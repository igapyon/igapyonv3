package jp.igapyon.diary.v3.hatena;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.v3.mdconv.DiarySrcMd2MdConverter;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class HatenaText2SrcMdConverterTest {

	@Test
	public void test() throws Exception {
		final File rootdir = new File("./test/data/hatena/");

		final IgapyonV3Settings settings = new IgapyonV3Settings();

		new HatenaText2SrcMdConverter(settings).processDir(rootdir);

		new DiarySrcMd2MdConverter(settings).processDir(rootdir);
	}
}
