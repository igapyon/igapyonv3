package jp.igapyon.diary.v3.gendiary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;

/**
 * 今日付けの日記 igyyMMdd.html.src.md ファイルが存在しなければこれを新規作成します。
 * 
 * @author Toshiki Iga
 */
public class TodayDiaryGenerator {
	private IgapyonV3Settings settings = null;

	public TodayDiaryGenerator(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	/**
	 * 本日の日記ファイルを取得します。
	 * 
	 * @param yearDir
	 * @return
	 */
	public File getTodayDiaryFile(final File yearDir) {
		// ファイル名は igyyMMdd.html.src.md 形式。
		final String yymmdd = new SimpleDateFormat("yyMMdd").format(settings.getToday());
		return new File(yearDir, ("ig" + yymmdd + ".html.src.md"));
	}

	/**
	 * 日記システムの今日の日記のためのルートディレクトリを取得します。
	 * 
	 * ディレクトリが存在しない場合は新規作成します。
	 * 
	 * @param rootdir
	 * @return
	 * @throws IOException
	 */
	public File getYearDir(final File rootdir) throws IOException {
		final String yyyy = new SimpleDateFormat("yyyy").format(settings.getToday());
		final File yearDir = new File(rootdir, yyyy);
		if (yearDir.exists() == false) {
			if (yearDir.mkdirs() == false) {
				throw new IOException("Fail to create 'year' dir [" + yearDir.getCanonicalPath() + "]. End process.");
			}
			System.err.println("New Year dir was created: " + yearDir.getAbsolutePath());
		}

		return yearDir;
	}

	/**
	 * 主たるエントリーポイント。
	 * 
	 * @param rootdir
	 * @throws IOException
	 */
	public void processDir(final File rootdir) throws IOException {
		final File yearDir = getYearDir(rootdir);

		// ファイル名は igyyMMdd.html.src.md 形式。
		final File file = getTodayDiaryFile(yearDir);
		if (file.exists()) {
			// すでに本日の日記ファイルは存在します。処理中断します。
			System.err.println("Today's diary is alread exist.: " + file.getAbsolutePath());
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
		System.err.println("Today's diary md file was created: " + file.getAbsolutePath());
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

		if (dir.getName().equals("igapyonv3")) {
			final IgapyonV3Settings settings = new IgapyonV3Settings();
			new TodayDiaryGenerator(settings).processDir(dir);
		} else {
			System.out.println("期待とは違うディレクトリ:" + dir.getName());
			return;
		}
	}
}
