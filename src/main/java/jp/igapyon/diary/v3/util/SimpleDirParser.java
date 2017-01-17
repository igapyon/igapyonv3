package jp.igapyon.diary.v3.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SimpleDirParser {
	protected boolean isRecursive = false;

	public abstract boolean isProcessTarget(final File file);

	public List<File> listFiles(final File rootDir, final boolean isRecursive) {
		this.isRecursive = isRecursive;

		final List<File> result = new ArrayList<File>();

		processListDir(rootDir, result);

		// sort it.
		Collections.sort(result, new FileComparator());

		return result;
	}

	protected void processListDir(final File targetDir, final List<File> result) {
		final File[] files = targetDir.listFiles();
		if (files == null) {
			return;
		}

		for (File file : files) {
			if (isProcessTarget(file) == false) {
				continue;
			}

			if (file.isDirectory()) {
				if (isRecursive) {
					processListDir(file, result);
				}
			} else if (file.isFile()) {
				try {
					result.add(file.getCanonicalFile());
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
	}
}
