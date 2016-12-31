package jp.igapyon.diary.v3.gendiary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 今日付けの日記 igyyMMdd.html.src.md ファイルが存在しなければこれを新規作成します。
 * 
 * @author Toshiki Iga
 */
public class GenerateTodayDiary {
	/**
	 * 主たるエントリーポイント。
	 * 
	 * @param dir
	 * @throws IOException
	 */
	public void processDir(final File dir) throws IOException {
		final Date today = new Date();

		final String yyyy = new SimpleDateFormat("yyyy").format(today);
		final File yearDir = new File(dir, yyyy);
		if (yearDir.exists() == false) {
			if (yearDir.mkdirs() == false) {
				System.err.println("ディレクトリを作成できません:" + yearDir.getCanonicalPath());
				return;
			}
		}

		// ファイル名は igyyMMdd.html.src.md 形式。
		final String yymmdd = new SimpleDateFormat("yyMMdd").format(today);
		final File file = new File(yearDir, "ig" + yymmdd + ".html.src.md");
		if (file.exists()) {
			// すでに本日の日記ファイルは存在します。処理中断します。
			return;
		}

		// 日記ファイルの新規作成に移ります。

		final List<String> lines = new ArrayList<String>();
		lines.add("## ここに日記のタイトル");
		lines.add("");
		lines.add("ここに何か日記の内容。");
		lines.add("");
		lines.add("* 箇条書き1");
		lines.add("* 箇条書き2");
		lines.add("  * 箇条書き2-1");
		lines.add("");
		lines.add("```java:Hello.java");
		lines.add("System.out.println(\"Hello world\");");
		lines.add("```");

		// 日記ファイルを新規作成します。
		FileUtils.writeLines(file, lines);
		System.out.println("diary md file created: " + file.getAbsolutePath());
	}

	/**
	 * テスト用のエントリポイント。
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File dir = new File(".");
		dir = dir.getCanonicalFile();

		if (dir.getName().equals("diary")) {
			new GenerateTodayDiary().processDir(dir);
		} else {
			System.out.println("期待とは違うディレクトリ:" + dir.getName());
			return;
		}
	}
}
