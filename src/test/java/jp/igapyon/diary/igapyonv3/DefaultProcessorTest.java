package jp.igapyon.diary.igapyonv3;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

public class DefaultProcessorTest {
	@Test
	public void test() throws Exception {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("./test/data"));
		new IgDiaryProcessor(settings).process();
	}
}
