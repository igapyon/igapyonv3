package jp.igapyon.diary.v3.hatena.migration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;
import jp.igapyon.util.IgapyonXmlUtil;

/**
 * hatenaのエクスポートXMLから、分割されたテキストファイルを生成します。
 * 
 * 基本的に、移行バッチとして一度のみ実行されることを想定します。何度も動作させる際に適切に動作するような機構は組み込まれていません。
 * 
 * @author Toshiki Iga
 */
public class HatenaXml2SeparatedTextConverter {
	private IgapyonV3Settings settings = null;

	public HatenaXml2SeparatedTextConverter(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	public void processFile(final File sourceXml, final File targetMdDir) throws IOException {
		String inputXmlString = FileUtils.readFileToString(sourceXml, "UTF-8");
		inputXmlString = inputXmlString.replace('\u001c', '−');
		inputXmlString = inputXmlString.replace('\u001a', '?');
		final Element rootElement = IgapyonXmlUtil.stringToElement(inputXmlString);

		final List<DiaryItemInfo> diaryItemList = parseRoot(rootElement);
		for (DiaryItemInfo item : diaryItemList) {
			System.out.println(item.getTitle() /* + ":" + item.getBody() */);
			final String yyyymmdd = item.getTitle().substring(0, 4) + item.getTitle().substring(5, 7)
					+ item.getTitle().substring(8, 10);
			final File yearDir = new File(targetMdDir, yyyymmdd.substring(0, 4));
			yearDir.mkdirs();
			final File targetFile = new File(yearDir, "ig" + yyyymmdd.substring(2) + ".src.hatenadiary");
			FileUtils.write(targetFile, item.getTitle() + "\n" + item.getBody(), "UTF-8");
		}
	}

	public static List<DiaryItemInfo> parseRoot(final Element rootElement) throws IOException {
		final List<DiaryItemInfo> result = new ArrayList<DiaryItemInfo>();
		final NodeList nodeList = rootElement.getElementsByTagName("day");
		for (int index = 0; index < nodeList.getLength(); index++) {
			final Element look = (Element) nodeList.item(index);
			result.add(parseDay(look));
		}
		return result;
	}

	public static DiaryItemInfo parseDay(final Element dayElement) throws IOException {
		final DiaryItemInfo item = new DiaryItemInfo();

		String dateString = dayElement.getAttribute("date");
		item.setTitle(dateString + " " + dayElement.getAttribute("title"));

		final NodeList nodeList = dayElement.getElementsByTagName("body");
		for (int index = 0; index < nodeList.getLength(); index++) {
			final Element look = (Element) nodeList.item(index);
			item.setBody((item.getBody() == null ? "" : item.getBody()) + look.getTextContent());
		}

		// Drop comments.

		return item;
	}

	/**
	 * エントリポイント。
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static final void main(final String[] args) throws IOException {
		final IgapyonV3Settings settings = new IgapyonV3Settings();
		new HatenaXml2SeparatedTextConverter(settings).processFile(new File("/tmp/igapyon.xml"), new File("."));
	}
}
