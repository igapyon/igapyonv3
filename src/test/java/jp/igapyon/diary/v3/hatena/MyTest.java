package jp.igapyon.diary.v3.hatena;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class MyTest {

	@Test
	public void test() throws Exception {
		String source = "-[http://d.hatena.ne.jp/igapyon/20161222:title=2016-12-22 maven インストール] などなど。[http://aaa/:title=abc]";

		if (true) {
			System.out.println(convertHatenaLink2MdLink(source));
			return;
		}

		// Hatena link.
		final Pattern pat = Pattern.compile("\\[.*?\\:title=.*?\\]");
		final Matcher mat = pat.matcher(source);

		if (mat.find()) {
			// 発見.
			final String linkString = mat.group();

			System.out.println(linkString);

			final Pattern linkPat = Pattern.compile("\\:title=");
			final Matcher linkMat = linkPat.matcher(linkString);
			if (linkMat.find()) {
				System.out.println("OK");

				System.out.println("start:" + linkMat.start());
				System.out.println("end  :" + linkMat.end());

				final String linkURL = linkString.substring(1, linkMat.start());
				final String linkTitle = linkString.substring(linkMat.end(), linkString.length() - 1);

				final String mdLink = "[" + linkTitle + "](" + linkURL + ")";

				System.out.println(mat.replaceFirst(mdLink));
			}

		}

	}

	public static String convertHatenaLink2MdLink(final String source) {
		// はてなリンクパターン。小さいマッチのために「?」を利用しています。
		final Pattern pat = Pattern.compile("\\[.*?\\:title=.*?\\]");
		final Matcher mat = pat.matcher(source);

		if (mat.find() == false) {
			// 一致しませんでした。置換する箇所はありませんでした。処理を終えて、このまま返却します。
			return source;
		}

		// 一致した文字列。
		final String linkString = mat.group();

		// title の前後で分断します。
		final Pattern linkPat = Pattern.compile("\\:title=");
		final Matcher linkMat = linkPat.matcher(linkString);
		if (linkMat.find() == false) {
			throw new IllegalArgumentException("Unexpected state.");
		}

		// URLパートは前半部分です。カッコは除去。
		final String linkURL = linkString.substring(1, linkMat.start());
		// タイトルは後半部分です。カッコは除去。
		final String linkTitle = linkString.substring(linkMat.end(), linkString.length() - 1);

		// md 形式のリンクへとおきかえます。
		final String mdLink = "[" + linkTitle + "](" + linkURL + ")";

		// 該当箇所以降の置換は再帰処理とします。
		return source.substring(0, mat.start()) + mdLink + convertHatenaLink2MdLink(source.substring(mat.end()));
	}
}
