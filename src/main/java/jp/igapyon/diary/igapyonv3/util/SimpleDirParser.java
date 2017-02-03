package jp.igapyon.diary.igapyonv3.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.diary.util.IgFileComparatorByName;

public abstract class SimpleDirParser {
	protected boolean isRecursive = false;

	public abstract boolean isProcessTarget(final File file);

	public List<File> listFiles(final File rootDir, final boolean isRecursive) {
		this.isRecursive = isRecursive;

		final List<File> result = new ArrayList<File>();

		processListDir(rootDir, result);

		// sort it.
		Collections.sort(result, new IgFileComparatorByName());

		return result;
	}

	protected void processListDir(final File targetDir, final List<File> result) {
		final File[] files = targetDir.listFiles();
		if (files == null) {
			return;
		}

		try {
			for (File file : files) {
				if (isProcessTarget(file) == false) {
					continue;
				}

				if (file.isDirectory()) {
					// add to list.
					result.add(file.getCanonicalFile());

					if (isRecursive) {
						processListDir(file, result);
					}
				} else if (file.isFile()) {
					// add to list.
					result.add(file.getCanonicalFile());
				}
			}
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
