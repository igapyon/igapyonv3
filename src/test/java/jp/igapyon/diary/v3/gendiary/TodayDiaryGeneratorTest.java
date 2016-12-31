package jp.igapyon.diary.v3.gendiary;

import java.io.File;

import org.junit.Test;

public class TodayDiaryGeneratorTest {
	@Test
	public void test() throws Exception {
		final File testDir = new File("./target/test");
		if (testDir.exists() == false)
			testDir.mkdirs();

		new TodayDiaryGenerator().processDir(testDir);
	}
}
