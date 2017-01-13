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
	public void testUrl2File() throws Exception {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("/tmp/aaa/"));
		settings.setBaseurl("https://igapyon.github.io/diary");

		assertEquals("/tmp/aaa",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary", settings).getCanonicalPath());
		assertEquals("/tmp/aaa/",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/", settings).getCanonicalPath());
		assertEquals("/tmp/aaa/aaa",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/aaa", settings).getCanonicalPath());
		assertEquals("/tmp/aaa/bbb",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/bbb", settings).getCanonicalPath());
		assertEquals("/tmp/aaa/bbb",
				SimpleDirUtil.url2File("https://igapyon.github.io/diary/bbb/", settings).getCanonicalPath());
		assertEquals("/tmp/aaa/bbb.txt",
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

		assertEquals("https://igapyon.github.io/diary",
				SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary", settings));
		assertEquals("aaa", SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa", settings));
		assertEquals("aaa/bbb",
				SimpleDirUtil.getRelativeUrlIfPossible("https://igapyon.github.io/diary/aaa/bbb", settings));
	}

}
