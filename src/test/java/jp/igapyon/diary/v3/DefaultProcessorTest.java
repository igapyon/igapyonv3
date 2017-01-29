package jp.igapyon.diary.v3;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class DefaultProcessorTest {
	@Test
	public void test() throws Exception {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("./test/data"));
		new DefaultProcessor().process(settings);
	}
}
