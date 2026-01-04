/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.diary.igapyonv3.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;
import jp.igapyon.diary.igapyonv3.util.SimpleDirParser;
import jp.igapyon.diary.igapyonv3.util.SimpleDirUtil;

public class SimpleDirUtilTest {
	@Test
	public void testGetRelativePath() throws Exception {
		assertEquals("", SimpleDirUtil.getRelativePath(new File("."), new File(".")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/"), new File("/")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/test/"), new File("/test")));
		assertEquals("", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test/")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test/a")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test/"), new File("/test/a")));
		assertEquals("a", SimpleDirUtil.getRelativePath(new File("/test/"), new File("/test/a/")));
		assertEquals("a/b/c", SimpleDirUtil.getRelativePath(new File("/test"), new File("/test/a/b/c")));

		try {
			assertEquals("a", SimpleDirUtil.getRelativePath(new File("/testtest/"), new File("/test/a/")));
			fail("never");
		} catch (IOException e) {
			// OK
		}
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testUrl2File() throws Exception {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("/private/tmp/aaa/"));
		settings.setBaseurl("https://igapyon.github.io/diary");

		assertEquals("/private/tmp/aaa",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary", settings).getCanonicalPath());
		assertEquals("/private/tmp/aaa/", SimpleDirUtil.url2File("https://igapyon.github.io/diary/", settings)
				.getCanonicalFile().getAbsolutePath());
		assertEquals("/private/tmp/aaa/aaa",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/aaa", settings).getCanonicalPath());
		assertEquals("/private/tmp/aaa/bbb",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/bbb", settings).getCanonicalPath());
		assertEquals("/private/tmp/aaa/bbb",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/bbb/", settings).getCanonicalPath());
		assertEquals("/private/tmp/aaa/bbb.txt",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/bbb.txt", settings).getCanonicalPath());
	}

	@Test
	public void testFile2Url() throws Exception {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("/tmp/aaa/"));
		settings.setBaseurl("https://igapyon.github.io/diary");

		assertEquals("https://igapyon.github.io/diary", SimpleDirUtil.file2Url(new File("/tmp/aaa"), settings));
		assertEquals("https://igapyon.github.io/diary", SimpleDirUtil.file2Url(new File("/tmp/aaa/"), settings));
		assertEquals("https://igapyon.github.io/diary/bbb", SimpleDirUtil.file2Url(new File("/tmp/aaa/bbb"), settings));
		assertEquals("https://igapyon.github.io/diary/bbb",
				SimpleDirUtil.file2Url(new File("/tmp/aaa/bbb/"), settings));
		assertEquals("https://igapyon.github.io/diary/bbb.txt",
				SimpleDirUtil.file2Url(new File("/tmp/aaa/bbb.txt"), settings));

	}

	@Test
	public void testGetRelativeUrlIfPossible() throws Exception {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("/tmp/aaa/"));
		settings.setBaseurl("https://igapyon.github.io/diary");

		assertEquals("https://igapyon.github.io/diary", SimpleDirUtil
				.getRelativeUrlIfPossible("https://igapyon.github.io/diary", new File("/tmp/aaa/"), settings));
		assertEquals("aaa", SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa",
				new File("/tmp/aaa/"), settings));
		assertEquals("aaa/bbb", SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa/bbb",
				new File("/tmp/aaa/"), settings));

		assertEquals("https://igapyon.github.io/diary/aaa", SimpleDirUtil
				.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa", new File("/tmp/aaa/aaa/"), settings));
		assertEquals("bbb", SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa/bbb",
				new File("/tmp/aaa/aaa"), settings));
	}

	@Test
	public void test002() throws Exception {
		SimpleDirParser parser = new SimpleDirParser() {
			public boolean isProcessTarget(final File file) {
				if (file.getName().startsWith(".")) {
					return false;
				}
				if (file.isDirectory() && file.getName().equals("target")) {
					return false;
				}

				if (file.getName().endsWith(".md") == false) {
					return false;
				}
				return true;
			}
		};
		List<File> files = parser.listFiles(new File("."), true);
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
		}
	}

	@Test
	public void testPathList001() throws Exception {

		final List<String> aaa = SimpleDirUtil.toPathList(new File("/a/b/c/d/e/f/g"));
		for (String lookup : aaa) {
			// System.out.println("dir"+lookup);
		}
	}

	@Test
	public void testMovingPath001() throws Exception {
		assertEquals("", SimpleDirUtil.getMovingPath("/", "/"));
		assertEquals("test", SimpleDirUtil.getMovingPath("/", "/test"));
		assertEquals("test/test", SimpleDirUtil.getMovingPath("/", "/test/test"));
		assertEquals("..", SimpleDirUtil.getMovingPath("/test", "/"));
		assertEquals("../test2", SimpleDirUtil.getMovingPath("/test1", "/test2"));
		assertEquals("../testC", SimpleDirUtil.getMovingPath("/testA/testB", "/testA/testC"));
		assertEquals("../../testC/testD", SimpleDirUtil.getMovingPath("/testA/testB", "/testC/testD"));
	}
}
