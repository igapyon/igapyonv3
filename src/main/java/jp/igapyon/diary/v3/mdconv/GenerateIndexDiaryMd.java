package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import jp.igapyon.diary.v3.item.DiaryItemInfo;

/**
 * .md ファイルからコンテンツ一覧を作成します。
 */
public class GenerateIndexDiaryMd {
	private List<DiaryItemInfo> diaryItemInfoList = new ArrayList<DiaryItemInfo>();

	public List<DiaryItemInfo> processDir(final File dir, String path) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return diaryItemInfoList;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file, path + "/" + file.getName());
			} else if (file.isFile()) {
				if (file.getName().startsWith("ig") && file.getName().endsWith(".md")
						&& false == file.getName().endsWith(".src.md")) {
					processFile(file, path);
				}
			}
		}

		return diaryItemInfoList;
	}

	void processFile(final File file, final String path) throws IOException {
		final List<String> lines = FileUtils.readLines(file, "UTF-8");

		final String url = "https://igapyon.github.io/diary" + path + "/"
				+ file.getName().substring(0, file.getName().length() - 3);

		final DiaryItemInfo diaryItemInfo = new DiaryItemInfo();
		diaryItemInfo.setUri(url);
		diaryItemInfo.setTitle(lines.get(0));

		diaryItemInfoList.add(diaryItemInfo);
	}
}
