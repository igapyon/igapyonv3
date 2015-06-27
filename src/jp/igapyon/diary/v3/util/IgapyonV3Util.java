package jp.igapyon.diary.v3.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class IgapyonV3Util {
	public static String readTextFile(final File file) throws IOException {
		final StringWriter writer = new StringWriter();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		final char[] buf = new char[2048];
		for (;;) {
			final int iReadLen = reader.read(buf);
			if (iReadLen < 0) {
				break;
			}
			writer.write(buf, 0, iReadLen);
		}
		reader.close();
		writer.close();
		return writer.toString();
	}
}
