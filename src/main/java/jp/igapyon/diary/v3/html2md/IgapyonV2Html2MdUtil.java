package jp.igapyon.diary.v3.html2md;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

public class IgapyonV2Html2MdUtil {
	public static void convertV2Html2Md(final File origFile) throws IOException {
		final File file = origFile.getCanonicalFile();

		String source = FileUtils.readFileToString(file, "UTF-8");
		try {
			// Normalize
			source = SimpleTagSoupUtil.formatHtml(source);

			final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			final SAXParser parser = saxFactory.newSAXParser();

			final IgapyonV2Html2MdParser htmlparser = new IgapyonV2Html2MdParser();

			parser.parse(new InputSource(new StringReader(source)), htmlparser);

			final File newFile = new File(file.getParentFile(),
					file.getName().substring(0, file.getName().length() - "-orig.html".length()) + ".html.src.md");
			System.out.println("convert from " + file.getName() + " to " + newFile.getName());
			FileUtils.writeStringToFile(newFile, htmlparser.getMarkdownString().trim(), "UTF-8");

			if (true) {
				// コピー後に、ファイルは移動してしまいます。
				final File targetFile = new File(file.getParentFile(),
						new File(file.getName().substring(0, file.getName().length() - ".html".length()))
								+ "-orig.html");
				FileUtils.moveFile(file, targetFile);
			}
		} catch (SAXException e) {
			throw new IOException(e);
		} catch (ParserConfigurationException e) {
			throw new IOException(e);
		}
	}
}
