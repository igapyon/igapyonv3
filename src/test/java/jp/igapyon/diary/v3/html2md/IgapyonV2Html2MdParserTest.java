package jp.igapyon.diary.v3.html2md;

import java.io.File;

import org.junit.Test;

public class IgapyonV2Html2MdParserTest {

	@Test
	public void test() throws Exception {
		IgapyonV2Html2MdUtil.convertV2Html2Md(new File("./test/data/v2html/ig100102.html").getCanonicalFile());
	}
}
