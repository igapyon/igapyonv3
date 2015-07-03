package jp.igapyon.diary.v3.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import jp.igapyon.diary.v3.md2html.MyLinkRenderer;

import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;

public class IgapyonV3Util {
	/**
	 * Read string from text file.
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String readTextFile(final File file) throws IOException {
		final StringWriter writer = new StringWriter();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "UTF-8"));
		final char[] buf = new char[2048];
		for (;;) {
			final int iReadLen = reader.read(buf);
			if (iReadLen < 0) {
				break;
			}
			writer.write(buf, 0, iReadLen);
		}
		reader.close();
		writer.close();
		return writer.toString();
	}

	/**
	 * Write html file.
	 * 
	 * @param strHtml
	 * @param file
	 * @throws IOException
	 */
	public static void writeHtmlFile(final String strHtml, final File file)
			throws IOException {
		final BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		writer.write(strHtml);
		writer.close();
	}

	public static void writePreHtml(final Writer writer,
			final String mdStringHead, final String title,
			final String description, final String author) throws IOException {
		writer.write("<!DOCTYPE html>\n");
		writer.write("<html lang=\"ja\">\n");
		writer.write("<head>\n");
		writer.write("<meta charset=\"utf-8\">\n");
		writer.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
		writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
		writer.write("<meta name=\"description\" content=\"" + description
				+ "\">\n");
		writer.write("<meta name=\"author\" content=\"" + author + "\">\n");
		writer.write("<title>" + title + "</title>\n");
		writer.write("<!-- Compiled and minified CSS -->\n");
		writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n");

		// see: http://getbootstrap.com/examples/theme/
		writer.write("<!-- Optional theme -->\n");
		writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">\n");

		writer.write("</head>\n");
		writer.write("<body>\n");
		// Use container-fluid instead container.
		writer.write("<div class=\"container-fluid\">\n");

		writer.write("<div class=\"jumbotron\">\n");

		if (mdStringHead.length() > 0) {
			final String bodyMarkdown = simpleMd2Html(mdStringHead,
					new MyLinkRenderer());
			writer.write(bodyMarkdown);
		}

		writer.write("</div>\n");

		// TODO container should be outside.
		writer.write("<div class=\"container-fluid\">\n");
	}

	public static String simpleMd2Html(final String mdString,
			final LinkRenderer linkRenderer) {
		final PegDownProcessor processor = new PegDownProcessor(
				Extensions.AUTOLINKS | Extensions.STRIKETHROUGH
						| Extensions.FENCED_CODE_BLOCKS | Extensions.TABLES
						| Extensions.WIKILINKS /*
												 * , PegDownPlugins
												 */);

		return processor.markdownToHtml(mdString, new MyLinkRenderer());
	}

	public static void writePostHtml(final Writer writer) throws IOException {
		writer.write("\n</div><!-- container-fluid -->\n");

		writer.write("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n");
		writer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>\n");
		writer.write("<!-- Compiled and minified JavaScript -->\n");
		writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n");

		// TODO Should be preset on HTML tag class.
		writer.write("<script>\n");
		writer.write("$(function() {\n");
		writer.write("  $(\"h2\").addClass(\"alert alert-warning\");\n");
		writer.write("  $(\"h3\").addClass(\"bg-success\");\n");
		writer.write("  $(\"h4\").addClass(\"bg-info\");\n");
		writer.write("  $(\"table\").addClass(\"table table-bordered\");\n");
		writer.write("});\n");
		writer.write("</script>\n");

		writer.write("</body>\n");
		writer.write("</html>");
	}

	/**
	 * 
	 * @param titleString
	 *            like 'md2html'.
	 * @param outputData
	 * @param targetHtml
	 * @return
	 * @throws IOException
	 */
	public static boolean checkWriteNecessary(final String titleString,
			final String outputData, final File targetHtml) throws IOException {
		if (targetHtml.exists() == false) {
			System.out.println(titleString + ": add: "
					+ targetHtml.getCanonicalPath());
			return true;
		} else {
			final String origOutputHtmlString = IgapyonV3Util
					.readTextFile(targetHtml);
			if (outputData.equals(origOutputHtmlString)) {
				System.out.println(titleString + ": non: "
						+ targetHtml.getCanonicalPath());
				return false;
			} else {
				System.out.println(titleString + ": upd: "
						+ targetHtml.getCanonicalPath());
				return true;
			}
		}
	}

	public static List<String> stringToList(final String stringWithNewline)
			throws IOException {
		final List<String> result = new ArrayList<String>();
		final BufferedReader reader = new BufferedReader(new StringReader(
				stringWithNewline));
		for (;;) {
			final String line = reader.readLine();
			if (line == null) {
				break;
			}
			result.add(line);
		}
		return result;
	}

	public static String listToString(final List<String> stringList) {
		final StringBuilder builder = new StringBuilder();
		for (String look : stringList) {
			builder.append(look);
			builder.append('\n');
		}
		return builder.toString();
	}
}