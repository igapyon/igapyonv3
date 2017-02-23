package jp.igapyon.diary.igapyonv3.inittemplate;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.igapyonv3.IgDiaryProcessor;

public class IgInittemplateDiaryDirTest {

	@Test
	public void test() throws Exception {
		new IgInittemplateDiaryDir(new File("./target/diarydirinittemplatetest")).process();

		new IgDiaryProcessor(new File("./target/diarydirinittemplatetest")).process();
	}
}
