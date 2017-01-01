package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

/**
 * .src.md から .md を生成するためのクラス。
 */
public class DiarySrcMd2MdConverter {
	public void processDir(final File dir) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file);
			} else if (file.isFile()) {
				if (file.getName().startsWith("ig") && file.getName().endsWith(".src.md")) {
					processFile(file);
				}
			}
		}
	}

	public static final String[][] DOUBLE_KEYWORDS = { { "Axis2", "https://axis.apache.org/axis2/java/core/" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" } };

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

		for (int index = 0; index < lines.size(); index++) {
			String line = lines.get(index);
			// [[key]]system
			final String DOUBLE_KEYWORD_PATTERN = "\\[\\[.*?\\]\\]";
			final Pattern patDoubleKeyword = Pattern.compile(DOUBLE_KEYWORD_PATTERN);
			final Matcher matDoubleKeyword = patDoubleKeyword.matcher(line);
			final boolean isDoubleKeywordFound = matDoubleKeyword.find();
			if (isDoubleKeywordFound) {
				String foundKeyword = matDoubleKeyword.group();
				foundKeyword = foundKeyword.substring(2, foundKeyword.length() - 2);

				boolean isReplaced = false;
				for (String[] registeredPair : DOUBLE_KEYWORDS) {
					if (registeredPair[0].compareToIgnoreCase(foundKeyword) == 0) {
						// 最初のやつだけ置換。
						line = line.substring(0, matDoubleKeyword.start()) + "[" + registeredPair[0] + "]("
								+ registeredPair[1] + ")" + line.substring(matDoubleKeyword.end());
						lines.set(index, line);
						isReplaced = true;
					}
				}

				if (isReplaced == false) {
					System.out.println("[[" + foundKeyword + "]]");
				}
			}
		}

		// TODO support template system.

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
				"[いがぴょんについて](https://igapyon.github.io/diary/memo/memoigapyon.html) / [インデックスに戻る](https://igapyon.github.io/diary/idxall.html)");

		String newName = file.getName().substring(0, file.getName().length() - (".src.md".length())) + ".md";
		FileUtils.writeLines(new File(file.getParentFile(), newName), lines);
	}
}
