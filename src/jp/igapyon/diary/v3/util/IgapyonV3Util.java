package jp.igapyon.diary.v3.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

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

	public static void writePreHtml(final Writer writer, final String title,
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
	}

	public static void writePostHtml(final Writer writer) throws IOException {
		writer.write("\n</div><!-- container-fluid -->\n");

		writer.write("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n");
		writer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>\n");
		writer.write("<!-- Compiled and minified JavaScript -->\n");
		writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n");
		writer.write("</body>\n");
		writer.write("</html>");
	}
}
