package jp.igapyon.diary.v3.mdconv;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import jp.igapyon.diary.v3.item.DiaryItemInfo;

/**
 * 指定されたファイルの一覧部分を更新する仕組み。
 */
public class ProcessIndexListing {
	public void process(File fileTarget, final List<DiaryItemInfo> diaryItemInfoList) throws IOException {
		fileTarget = fileTarget.getCanonicalFile();
		if (fileTarget.getName().endsWith(".src.md") == false) {
			return;
		}
		final String newName = fileTarget.getName().substring(0, fileTarget.getName().length() - 7) + ".md";

		String wrk = "";
		for (DiaryItemInfo itemInfo : diaryItemInfoList) {
			wrk = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrk;
		}

		String wrkRecent = "";
		for (DiaryItemInfo itemInfo : diaryItemInfoList) {
			if (itemInfo.getTitle().startsWith("2016-") || itemInfo.getTitle().startsWith("2017-")) {
				wrkRecent = "* [" + itemInfo.getTitle() + "](" + itemInfo.getUri() + ")\n" + wrkRecent;
			}
		}

		String target = FileUtils.readFileToString(fileTarget, "UTF-8");
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist}", wrk);
		target = StringUtils.replace(target, "{igapyon.diary.ghpages.dialylist.recent}", wrkRecent);
		FileUtils.writeStringToFile(new File(fileTarget.getParentFile() + "/" + newName), target, "UTF-8");
	}
}
