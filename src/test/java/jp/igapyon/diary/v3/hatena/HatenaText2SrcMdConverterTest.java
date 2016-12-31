package jp.igapyon.diary.v3.hatena;

import java.io.File;

import org.junit.Test;

public class HatenaText2SrcMdConverterTest {

	@Test
	public void test() throws Exception {
		new HatenaText2SrcMdConverter().processDir(new File("./test/data/hatena/"));
	}
}
