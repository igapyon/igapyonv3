package jp.igapyon.diary.v3.proto;

/**
 * Now checking...
 * https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Mediawiki2HTML
 * 
 * @author Toshiki Iga
 */
public class ProtoWiki2Html {
	public static void main(final String[] args) {
		final IgapyonWikiModel model = new IgapyonWikiModel("", "");
		final String result = model.render(
				"もんげーシンプルな [[Hello World]] wiki タグなう.", false);
		System.out.println("[" + result + "]");
	}
}
