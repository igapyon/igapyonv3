package jp.igapyon.diary.v3.indexing.title;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class DiaryAtomByTitleGenerator {
	private IgapyonV3Settings settings = null;

	public DiaryAtomByTitleGenerator(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void process() throws IOException {
		// キーワードのリストを読み込み。
		final List<SyndEntry> keywordEntryList = new ArrayList<SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir().getCanonicalPath() + "/keyword", "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					keywordEntryList.add(entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		// すべての日記のタイトルを読み込み。
		final List<SyndEntry> diaryEntryList = new ArrayList<SyndEntry>();
		try {
			final File fileAtom = new File(settings.getRootdir(), "atom.xml");
			if (fileAtom.exists()) {
				final SyndFeed synFeed = new SyndFeedInput().build(new XmlReader(new FileInputStream(fileAtom)));
				for (Object lookup : synFeed.getEntries()) {
					final SyndEntry entry = (SyndEntry) lookup;
					diaryEntryList.add(entry);
				}
			}
		} catch (FeedException e) {
			throw new IOException(e);
		}

		// 各キーワードごとに atomキーワード物理名.xml を keyword ディレクトリに生成
		for (SyndEntry entry : diaryEntryList) {
			System.out.println(entry.getTitle());
		}

		// SimpleRomeUtil.itemList2AtomXml(diaryItemInfoList, new
		// File(settings.getRootdir(), "keyword" + "/atom.xml"),
		// "Igapyon Diary v3 keyword", settings);
	}

	public static void main(final String[] args) throws IOException {
		IgapyonV3Settings settings = new IgapyonV3Settings();
		settings.setRootdir(new File("../diary"));
		new DiaryAtomByTitleGenerator(settings).process();
	}
}
