package jp.igapyon.diary.igapyonv3.directive;

import java.io.File;

import org.junit.jupiter.api.Test;

import jp.igapyon.diary.igapyonv3.mdconv.DiarySrcMd2MdConverter;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

public class DirectiveTest {
	@Test
	public void test() throws Exception {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("./test/data"));
		new DiarySrcMd2MdConverter(settings).processDir(settings.getRootdir());
	}
}
