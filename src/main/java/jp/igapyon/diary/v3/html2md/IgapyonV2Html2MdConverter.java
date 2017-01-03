package jp.igapyon.diary.v3.html2md;

import java.io.File;
import java.io.IOException;

public class IgapyonV2Html2MdConverter {
	public void processDir(final File dir) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file);
			} else if (file.isFile()) {
				if (file.getName().endsWith(".html")) {
					processFile(file);
				}
			}
		}
	}

	void processFile(final File file) throws IOException {
		System.out.println(file.getName());
		IgapyonV2Html2MdUtil.convertV2Html2Md(file);
	}
}
