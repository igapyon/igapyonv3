package jp.igapyon.diary.v3.hatena;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HatenaTextUtil {
	/**
	 * はてなリンク形式を md リンク形式に変換します。
	 * 
	 * @param source
	 * @return
	 */
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
		String linkURL = linkString.substring(1, linkMat.start());
		// タイトルは後半部分です。カッコは除去。
		final String linkTitle = linkString.substring(linkMat.end(), linkString.length() - 1);

		// URL
		{
			if (linkURL.indexOf("http://d.hatena.ne.jp/igapyon/") >= 0) {
				linkURL = "https://igapyon.github.io/diary/" + linkURL.substring(30, 34) + "/ig"
						+ linkURL.substring(32, 38) + ".html";
			}
		}

		// md 形式のリンクへとおきかえます。
		final String mdLink = "[" + linkTitle + "](" + linkURL + ")";

		// 該当箇所以降の置換は再帰処理とします。
		return source.substring(0, mat.start()) + mdLink + convertHatenaLink2MdLink(source.substring(mat.end()));
	}

	public static String convertSimpleUrl2MdLink(final String source) {
		final String URL_PATTERN = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";
		// リンクパターン。小さいマッチのために「?」を利用しています。
		final String MD_LINK_PATTERN = "(\\[.*?\\]|\\(.*?\\))";

		final Pattern patMdLink = Pattern.compile(MD_LINK_PATTERN);
		final Matcher matMdLink = patMdLink.matcher(source);

		final Pattern patURL = Pattern.compile(URL_PATTERN);
		final Matcher matURL = patURL.matcher(source);

		final boolean isMdLinkFound = matMdLink.find();
		final boolean isURLFound = matURL.find();

		if (isMdLinkFound == false && isURLFound == false) {
			// いずれも存在せず。処理せず戻します。
			return source;
		}
		if (isMdLinkFound && isURLFound == false) {
			// MDリンクのみ。リンクの終了場所まで読み飛ばしたうえで再帰処理します。
			return source.substring(0, matMdLink.end()) + convertSimpleUrl2MdLink(source.substring(matMdLink.end()));
		}
		if (isMdLinkFound && isURLFound) {
			// 両方見つかりました。それでは、どちらが先に登場するのでしょうか。
			if (matMdLink.start() < matURL.start()) {
				// MDリンクが先に登場しました。リンクの終了場所まで読み飛ばしたうえで再帰処理します。
				return source.substring(0, matMdLink.end())
						+ convertSimpleUrl2MdLink(source.substring(matMdLink.end()));
			}
			// 生リンクの勝ちです。
		}

		// それでは生リンクの埋め込み処理を実施します。
		return source.substring(0, matURL.start()) + "[" + matURL.group() + "](" + matURL.group() + ")"
				+ convertSimpleUrl2MdLink(source.substring(matURL.end()));
	}
}
