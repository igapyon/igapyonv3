package jp.igapyon.util;

import java.io.File;
import java.io.IOException;

public class IgapyonFileUtil {
	public static String getRelativePath(File rootdir, File file) throws IOException {
		// do canonical
		rootdir = rootdir.getCanonicalFile();
		file = file.getCanonicalFile();
		if (file.getPath().indexOf(rootdir.getPath()) < 0) {
			throw new IOException("ディレクトリ相関関係エラー:rootdir=" + rootdir.getPath() + ",file=" + file.getPath());
		}

		String relativePath = file.getPath().substring(rootdir.getPath().length());
		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}

		return relativePath;
	}
}
