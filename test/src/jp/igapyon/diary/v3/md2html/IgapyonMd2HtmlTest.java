package jp.igapyon.diary.v3.md2html;

import org.junit.Test;

public class IgapyonMd2HtmlTest {
	@Test
	public void testProcessDirStringString() throws Exception {
		new IgapyonMd2Html()
				.processDir("./test/data/src", "./test/data/output");
	}
}
