package jp.igapyon.diary.v3.markdown_proto;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import jp.igapyon.diary.v3.util.IgapyonV3Util;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

public class ProtoMd2Html2 {
	public static void main(final String[] args) throws IOException {
		final StringWriter writer = new StringWriter();
		writer.write("<!DOCTYPE html>\n");
		writer.write("<html lang=\"ja\">\n");
		writer.write("<head>\n");
		writer.write("<meta charset=\"utf-8\">\n");
		writer.write("<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n");
		writer.write("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n");
		writer.write("<meta name=\"description\" content=\"\">\n");
		writer.write("<meta name=\"author\" content=\"\">\n");
		writer.write("<title>タイトル</title>\n");
		writer.write("<!-- Compiled and minified CSS -->\n");
		writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n");
		writer.write("<!-- Optional theme -->\n");
		writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\">\n");
		writer.write("</head>\n");
		writer.write("<body>\n");
		writer.write("<div class=\"container\">");

		final PegDownProcessor processor = new PegDownProcessor(
				Extensions.FENCED_CODE_BLOCKS /* , PegDownPlugins */);
		String aaa = processor
				.markdownToHtml(IgapyonV3Util.readTextFile(new File(
						"./src/jp/igapyon/diary/v3/markdown_proto/test001.md")));
		writer.write(aaa);

		writer.write("</div>\n");

		writer.write("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n");
		writer.write("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>\n");
		writer.write("<!-- Compiled and minified JavaScript -->\n");
		writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n");
		writer.write("</body>\n");
		writer.write("</html>\n");

		writer.close();
		System.out.println(writer.toString());
	}
}
