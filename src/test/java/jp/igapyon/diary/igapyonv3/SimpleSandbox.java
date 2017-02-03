package jp.igapyon.diary.igapyonv3;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import jp.igapyon.diary.util.IgStringUtil;

public class SimpleSandbox {
	@Test
	public void test() throws Exception {
		String result = StringUtils.abbreviate("123456789012345.html", 10);
		System.out.println(result);

		result = StringUtils.abbreviateMiddle("[http://d.hatena.ne.jp/igapyon/20161222", "...", 20);
		System.out.println(result);

		System.out.println(IgStringUtil.abbreviateMiddle("[http://d.hatena.ne.jp/igapyon/20161222"));
	}

}
