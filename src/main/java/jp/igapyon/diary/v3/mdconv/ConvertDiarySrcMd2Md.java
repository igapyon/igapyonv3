package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * .src.md から .md を生成するためのクラス。
 */
public class ConvertDiarySrcMd2Md {
	public void process() throws IOException {
		File dir = new File(".");
		dir = dir.getCanonicalFile();
		System.out.println(dir.getPath());

		if (dir.getName().equals("diary")) {
			System.out.println("期待通りディレクトリ");
			processDir(dir);
		} else {
			System.out.println("期待とは違うディレクトリ:" + dir.getName());
			return;
		}
	}

	void processDir(final File dir) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file);
			} else if (file.isFile()) {
				if (file.getName().startsWith("ig") && file.getName().endsWith(".src.md")) {
					System.out.println(file.getName());
					processFile(file);
				}
			}
		}
	}

	void processFile(final File file) throws IOException {
		final List<String> lines = FileUtils.readLines(file, "UTF-8");

		String firstH2Line = null;
		String year1 = "20";
		String year2 = file.getName().substring(2, 4);
		if (year2.startsWith("9")) {
			year1 = "19";
		}

		String month = file.getName().substring(4, 6);
		String day = file.getName().substring(6, 8);
		for (String line : lines) {
			if (firstH2Line == null) {
				if (line.startsWith("## ")) {
					firstH2Line = line.substring(3);
				}
			}
			// System.out.println(" " + line);
		}

		// ヘッダ追加
		lines.add(0, year1 + year2 + "-" + month + "-" + day + " diary: " + firstH2Line);
		lines.add(1,
				"=====================================================================================================");
		lines.add(2,
				"[![いがぴょん画像(小)](https://igapyon.github.io/diary/images/iga200306s.jpg \"いがぴょん\")](https://igapyon.github.io/diary/memo/memoigapyon.html) 日記形式でつづる [いがぴょん](https://igapyon.github.io/diary/memo/memoigapyon.html)コラム ウェブページです。");
		lines.add(3, "");

		// 本体

		// フッタ追加
		lines.add("");
		lines.add("");
		lines.add(
				"----------------------------------------------------------------------------------------------------");
		lines.add("");
		lines.add("## この日記について");
		lines.add(
				"[いがぴょんについて](http://www.igapyon.jp/igapyon/diary/memo/memoigapyon.html) / [インデックスに戻る](https://igapyon.github.io/diary/idxall.html)");

		String newName = file.getName().substring(0, file.getName().length() - (".src.md".length())) + ".md";
		FileUtils.writeLines(new File(file.getParentFile(), newName), lines);
		// System.out.println("NEWMANE [" + newName + "]");
	}
}
