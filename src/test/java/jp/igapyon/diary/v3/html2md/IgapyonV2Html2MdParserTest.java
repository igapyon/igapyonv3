package jp.igapyon.diary.v3.html2md;

import java.io.File;
import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import jp.igapyon.diary.v3.util.SimpleTagSoupUtil;

public class IgapyonV2Html2MdParserTest {

	@Test
	public void test() throws Exception {
		final File file = new File("./test/data/v2html/ig100102.html").getCanonicalFile();
		String source = FileUtils.readFileToString(file, "UTF-8");
		try {
			// Normalize
			source = SimpleTagSoupUtil.formatHtml(source);

			final SAXParserFactory saxFactory = SAXParserFactory.newInstance();
			final SAXParser parser = saxFactory.newSAXParser();

			final IgapyonV2Html2MdParser htmlparser = new IgapyonV2Html2MdParser();

			parser.parse(new InputSource(new StringReader(source)), htmlparser);

			final File newFile = new File(file.getParentFile(), file.getName() + ".src.md");
			System.out.println("convert from " + file.getName() + " to " + newFile.getName());
			FileUtils.writeStringToFile(newFile, htmlparser.getMarkdownString().trim(), "UTF-8");
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
