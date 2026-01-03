package jp.igapyon.diary.igapyonv3.html2md;

import java.io.File;

import org.junit.jupiter.api.Test;

import jp.igapyon.diary.igapyonv3.migration.html2md.IgapyonV2Html2MdUtil;

public class IgapyonV2Html2MdParserTest {

	@Test
	public void test() throws Exception {
		IgapyonV2Html2MdUtil.convertV2Html2Md(new File("./test/data/v2html/ig100102-orig.html").getCanonicalFile());
	}
}
