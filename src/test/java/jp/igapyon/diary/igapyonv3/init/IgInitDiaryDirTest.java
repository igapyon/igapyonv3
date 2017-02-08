package jp.igapyon.diary.igapyonv3.init;

import java.io.File;

import org.junit.Test;

import jp.igapyon.diary.igapyonv3.IgDiaryProcessor;

public class IgInitDiaryDirTest {

	@Test
	public void test() throws Exception {
		new IgInitDiaryDir().process(new File("./target/diarydirtest"));

		new IgDiaryProcessor().process(new File("./target/diarydirtest"));
	}
}
