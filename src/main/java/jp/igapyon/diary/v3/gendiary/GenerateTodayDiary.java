package jp.igapyon.diary.v3.gendiary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class GenerateTodayDiary {

	public static void main(String[] args) throws IOException {
		new GenerateTodayDiary().process();
	}

	public void process() throws IOException {
		File dir = new File(".");
		dir = dir.getCanonicalFile();
		System.out.println(dir.getPath());

		if (dir.getName().equals("diary")) {
			processDir(dir);
		} else {
			System.out.println("期待とは違うディレクトリ:" + dir.getName());
			return;
		}
	}

	void processDir(final File dir) throws IOException {
		final Date today = new Date();

		final String yyyy = new SimpleDateFormat("yyyy").format(today);
		final File yearDir = new File(dir, yyyy);
		if (yearDir.exists() == false) {
			if (yearDir.mkdirs() == false) {
				System.out.println("ディレクトリを作成できません:" + yearDir.getCanonicalPath());
				return;
			}
		}

		final String yymmdd = new SimpleDateFormat("yyMMdd").format(today);
		final File file = new File(yearDir, "ig" + yymmdd + ".html.src.md");
		if (file.exists()) {
			return;
		}

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

		FileUtils.writeLines(file, lines);
		System.out.println("New diary md file is created.");
	}
}
