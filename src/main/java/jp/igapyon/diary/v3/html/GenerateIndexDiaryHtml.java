package jp.igapyon.diary.v3.html;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jp.igapyon.diary.v3.item.DiaryItemInfo;
import jp.igapyon.diary.v3.item.DiaryItemInfoComparator;
import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

/**
 * 既存 HTML からタイトル一覧を取得します。
 * 
 * 既存 HTML のタイトルが、所定の日記形式テキストから開始されていることが大前提となります。
 * また、ディレクトリ構造が年付きの構造になっていることも重要です。（いがぴょんの日記v2 形式）
 * 
 * @author Toshiki Iga
 */
public class GenerateIndexDiaryHtml {
	private List<DiaryItemInfo> diaryItemInfoList = new ArrayList<DiaryItemInfo>();

	public List<DiaryItemInfo> process() throws IOException {
		File dir = new File(".");
		dir = dir.getCanonicalFile();
		System.out.println(dir.getPath());

		if (dir.getName().equals("diary")) {
			System.out.println("期待通りディレクトリ");
			processDir(dir, "");
		} else {
			System.out.println("期待とは違うディレクトリ:" + dir.getName());
		}

		Collections.sort(diaryItemInfoList, new DiaryItemInfoComparator());

		return diaryItemInfoList;
	}

	public void processDir(final File dir, String path) throws IOException {
		final File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (file.isDirectory()) {
				processDir(file, path + "/" + file.getName());
			} else if (file.isFile()) {
				if (file.getName().startsWith("ig") && file.getName().endsWith(".html")
						&& false == file.getName().endsWith(".src.html")) {
					System.out.println(file.getName());
					processFile(file, path);
				}
			}
		}
	}

	void processFile(final File file, final String path) throws IOException {
		String source = FileUtils.readFileToString(file, "UTF-8");
		try {
			source = SimpleTagSoupUtil.formatHtml(source);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String title = "N/A";
		try {
			final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			final Document document = documentBuilder.parse(new InputSource(new StringReader(source)));

			final XPath xpath = XPathFactory.newInstance().newXPath();

			title = (String) xpath.evaluate("/html/head/title/text()", document, XPathConstants.STRING);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
			System.out.println("html:" + source);
			throw new IllegalArgumentException("BREAK!");
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		{
			// タイトル先頭部の日付形式をハイフンに変換
			String titleDate = title.substring(0, 10);
			titleDate = StringUtils.replaceChars(titleDate, "/", "-");
			title = titleDate + title.substring(10);
		}

		final String url = "https://igapyon.github.io/diary" + path + "/" + file.getName();

		final DiaryItemInfo diaryItemInfo = new DiaryItemInfo();
		diaryItemInfo.setUri(url);

		diaryItemInfo.setTitle(title);

		diaryItemInfoList.add(diaryItemInfo);
	}
}
