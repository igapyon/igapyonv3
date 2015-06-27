package jp.igapyon.diary.v3.proto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Now checking...
 * https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Mediawiki2HTML
 * 
 * @author Toshiki Iga
 */
public class ProtoWiki2Html {
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
		writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\">\n");
		writer.write("</head>\n");
		writer.write("<body>\n");
		writer.write("<div class=\"container\">");

		{
			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(
							"./src/jp/igapyon/diary/v3/proto/test001.wiki")));
			final StringWriter wikiInput = new StringWriter();
			for (;;) {
				final String line = reader.readLine();
				if (line == null) {
					break;
				}
				wikiInput.write(line);
				wikiInput.write("\n");
			}
			wikiInput.close();

			final IgapyonWikiModel model = new IgapyonWikiModel("", "");
			// model.addCodeFormatter("java", new JavaCodeFilter());

			writer.write(model.render(wikiInput.toString(), false));
		}

		writer.write("</div>\n");

		writer.write("<script src=\"//ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>\n");
		writer.write("<script src=\"//maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>\n");
		writer.write("</body>\n");
		writer.write("</html>\n");

		writer.close();
		System.out.println(writer.toString());
	}
}
