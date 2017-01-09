package jp.igapyon.diary.v3.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SimpleDirUtilTest {
	@Test
	public void testGetRelativePath() throws Exception {
		assertEquals("", SimpleDirUtil.getRelativePath(new File("."), new File(".")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/"), new File("/")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test/a")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test/"), new File("/test/a")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test/"), new File("/test/a/")));

		try {
			assertEquals("a", SimpleDirUtil.getRelativePath(new File("/testtest/"), new File("/test/a/")));
			fail("never");
		} catch (IOException e) {
			// OK
		}
	}
}
