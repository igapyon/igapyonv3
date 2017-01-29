package jp.igapyon.diary.v3;

import java.io.File;

import org.junit.Test;

public class DefaultProcessorTest {
	@Test
	public void test() throws Exception {
		new DefaultProcessor().process(new File("./test/data"), false);
	}
}
