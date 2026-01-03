package jp.igapyon.diary.igapyonv3.init;

import java.io.File;

import org.junit.jupiter.api.Test;

import jp.igapyon.diary.igapyonv3.IgDiaryProcessor;

public class IgInitDiaryDirTest {

	@Test
	public void test() throws Exception {
		new IgInitDiaryDir(new File("./target/diarydirtest")).process();

		new IgDiaryProcessor(new File("./target/diarydirtest")).process();
	}
}
