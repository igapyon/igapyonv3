package jp.igapyon.diary.v3.md2html;

import java.io.File;
import java.io.IOException;

public abstract class AbstractParseDir {
	protected String fileExt = ".md";

	public void parseDir(final File lookup, final String fileExt)
			throws IOException {
		this.fileExt = fileExt;
		parseDir(lookup, lookup);
	}

	public static String getSubdir(final File baseDir, final File file)
			throws IOException {
		final String baseDirStr = baseDir.getCanonicalPath();
		final String targetFileStr = file.getCanonicalPath();
		if (targetFileStr.startsWith(baseDirStr) == false) {
			throw new IllegalArgumentException("getSubdir: file["
					+ file.getCanonicalPath() + "] must starts with baseDir["
					+ baseDir.getCanonicalPath() + "].");
		}
		String targetRelFileStr = targetFileStr.substring(baseDirStr.length());
		if (targetRelFileStr.startsWith("/")
				|| targetRelFileStr.startsWith("\\")) {
			targetRelFileStr = targetRelFileStr.substring(1);
		}
		return targetRelFileStr;
	}

	public static String replaceExt(final String origName, final String newExt) {
		if (origName.contains(".") == false) {
			return origName + newExt;
		}
		final String withoutExt = origName.substring(0, origName.indexOf("."));
		return withoutExt + newExt;
	}

	protected void parseDir(final File baseDir, final File lookup)
			throws IOException {
		final File[] files = lookup.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				parseDir(baseDir, file);
			}
			if (file.isFile()) {
				if (file.getName().toLowerCase().endsWith(fileExt)) {
					final String baseDirStr = baseDir.getCanonicalPath();
					final String targetFileStr = file.getCanonicalPath();
					String targetRelFileStr = targetFileStr
							.substring(baseDirStr.length());
					if (targetRelFileStr.startsWith("/")
							|| targetRelFileStr.startsWith("\\")) {
						targetRelFileStr = targetRelFileStr.substring(1);
					}

					parseFile(baseDir, file);
				}
			}
		}
	}

	public abstract void parseFile(final File baseDir, final File file)
			throws IOException;
}
