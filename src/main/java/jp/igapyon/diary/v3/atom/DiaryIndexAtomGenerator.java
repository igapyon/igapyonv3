package jp.igapyon.diary.v3.atom;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.diary.v3.html.IndexDiaryHtmlParser;
import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.v3.mdconv.IndexDiaryMdParser;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.diary.v3.util.SimpleRomeUtil;

public class DiaryIndexAtomGenerator {
	private IgapyonV3Settings settings = null;

	public DiaryIndexAtomGenerator(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void process(final File rootdir) throws IOException {
		// ルートディレクトリ用
		{
			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files.");
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings).processDir(rootdir, "");
			System.err.println("Listing html files.");
			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IndexDiaryHtmlParser(settings).processDir(rootdir,
					"");
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(true));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, "atom.xml"), "Igapyon Diary v3 all");

			{
				int diaryListupCount = 15;

				final List<DiaryItemInfo> recentItemInfoList = new ArrayList<DiaryItemInfo>();

				for (DiaryItemInfo itemInfo : diaryItemInfoList) {
					diaryListupCount--;
					recentItemInfoList.add(itemInfo);
					if (diaryListupCount <= 0) {
						break;
					}
				}

				SimpleRomeUtil.itemList2AtomXml(recentItemInfoList, new File(rootdir, "atomRecent.xml"),
						"Igapyon Diary v3 recent");
			}
		}

		// new ProcessIndexListing(settings).process(new
		// File("README.src.md"), diaryItemInfoList);
		// new ProcessIndexListing(settings).process(new
		// File("idxall.html.src.md"), diaryItemInfoList);

		final String[] YEARS = new String[] { "1996", "1997", "1998", "2000", "2001", "2002", "2003", "2004", "2005",
				"2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017" };

		for (String year : YEARS) {
			// 各年ディレクトリ用

			// ファイルからファイル一覧情報を作成します。
			System.err.println("Listing md files for :" + year);
			final List<DiaryItemInfo> diaryItemInfoList = new IndexDiaryMdParser(settings)
					.processDir(new File(rootdir, year), "/" + year);

			System.err.println("Listing html files for :" + year);
			final List<DiaryItemInfo> diaryItemInfoHtmlList = new IndexDiaryHtmlParser(settings)
					.processDir(new File(rootdir, year), "/" + year);
			diaryItemInfoList.addAll(diaryItemInfoHtmlList);

			// sort them
			Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator(false));

			SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new File(rootdir, year + "/atom.xml"),
					"Igapyon Diary v3 year " + year);
		}
	}
}
