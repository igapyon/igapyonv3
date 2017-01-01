package jp.igapyon.diary.v3.gendiary;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class TodayDiaryGeneratorTest {
	@Test
	public void test() throws Exception {
		final File testDir = new File("./target/test");
		if (testDir.exists() == false)
			testDir.mkdirs();

		final IgapyonV3Settings settings = new IgapyonV3Settings();
		new TodayDiaryGenerator(settings).processDir(testDir);
	}
}
