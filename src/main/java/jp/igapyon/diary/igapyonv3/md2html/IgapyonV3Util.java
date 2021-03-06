/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.diary.igapyonv3.md2html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.pegdown.LinkRenderer;

import jp.igapyon.diary.igapyonv3.md2html.pegdownext.IgapyonLinkRenderer;
import jp.igapyon.diary.igapyonv3.md2html.pegdownext.IgapyonPegDownProcessor;
import jp.igapyon.diary.igapyonv3.md2html.pegdownext.IgapyonPegDownTagConf;

/**
 * utils for igapyonv3
 * 
 * @author Toshiki Iga
 * @deprecated tobe Apache FileUtils
 */
public class IgapyonV3Util {
	/**
	 * Read string from text file.
	 * 
	 * @param file
	 *            file to read.
	 * @return string of file.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static String readTextFile(final File file) throws IOException {
		final FileInputStream inStream = new FileInputStream(file);
		try {
			return IOUtils.toString(inStream, "UTF-8");
		} finally {
			IOUtils.closeQuietly(inStream);
		}
	}

	/**
	 * Write html file.
	 * 
	 * @param strHtml
	 *            string to write.
	 * @param file
	 *            file to be written.
	 * @throws IOException
	 *             io exception occurs.
	 */
	public static void writeHtmlFile(final String strHtml, final File file) throws IOException {
		final FileOutputStream outStream = new FileOutputStream(file);
		try {
			IOUtils.write(strHtml, outStream, "UTF-8");
			outStream.flush();
		} finally {
			IOUtils.closeQuietly(outStream);
		}
	}

	public static void writePreHtml(final IgapyonMd2HtmlSettings settings, final IgapyonPegDownTagConf tagConf,
			final Writer writer, final String mdStringHead, final String author) throws IOException {

		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n");
		// FIXME lang should be variable
		writer.write("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"ja\" lang=\"ja\">");
		writer.write("<head>\n");
		writer.write("<meta charset=\"utf-8\">\n");
		writer.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
		writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
		writer.write("<meta name=\"description\" content=\"" + settings.getHtmlDescription() + "\">\n");
		writer.write("<meta name=\"author\" content=\"" + author + "\">\n");
		writer.write("<meta name=\"generator\" content=\"" + IgapyonMd2HtmlConstants.PROGRAM_DISPLAY_NAME + " ver"
				+ IgapyonMd2HtmlConstants.VERSION + "\">\n");
		writer.write("<title>" + settings.getHtmlTitle() + "</title>\n");
		writer.write("<!-- Compiled and minified CSS -->\n");
		writer.write(
				"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n");

		// see: http://getbootstrap.com/examples/theme/
		// try to remove optional theme
		// writer.write("<!-- Optional theme -->\n");
		// writer.write("<link rel=\"stylesheet\"
		// href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">\n");

		writer.write("</head>\n");
		writer.write("<body>\n");
		// Use container-fluid instead container.
		writer.write("<div class=\"container-fluid\">\n");

		writer.write("<div class=\"jumbotron\">\n");

		if (mdStringHead.length() > 0) {
			final String bodyMarkdown = simpleMd2Html(settings, tagConf, mdStringHead, new IgapyonLinkRenderer());
			writer.write(bodyMarkdown);
		}

		writer.write("</div>\n");

		// TODO container should be outside.
		writer.write("<div class=\"container-fluid\">\n");
	}

	public static String simpleMd2Html(final IgapyonMd2HtmlSettings settings, final IgapyonPegDownTagConf tagConf,
			final String mdString, final LinkRenderer linkRenderer) {
		final IgapyonPegDownProcessor processor = new IgapyonPegDownProcessor(settings.getPegdownProcessorExtensions());
		processor.setTagConf(tagConf);
		return processor.markdownToHtml(mdString, new IgapyonLinkRenderer());
	}

	public static void writePostHtml(final Writer writer) throws IOException {
		writer.write("\n</div><!-- container-fluid -->\n");

		writer.write("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n");
		writer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>\n");
		writer.write("<!-- Compiled and minified JavaScript -->\n");
		writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n");

		if (false) {
			// Codes for direct jQuery call.
			writer.write("<script>\n");
			writer.write("$(function() {\n");
			// writer.write(" $(\"h2\").addClass(\"alert alert-warning\");\n");
			// writer.write(" $(\"h3\").addClass(\"bg-success\");\n");
			// writer.write(" $(\"h4\").addClass(\"bg-info\");\n");
			// writer.write(" $(\"table\").addClass(\"table
			// table-bordered\");\n");
			writer.write("});\n");
			writer.write("</script>\n");
		}

		writer.write("</body>\n");
		writer.write("</html>");
	}
}