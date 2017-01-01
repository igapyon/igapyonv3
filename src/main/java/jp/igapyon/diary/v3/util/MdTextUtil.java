package jp.igapyon.diary.v3.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Markdown テキストのためのユーティリティです。
 * 
 * @author Toshiki Iga
 */
public class MdTextUtil {
	/**
	 * 対象とするシンプルなURLリンクパターン。
	 */
	public static final String URL_LINK_PATTERN = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?";

	/**
	 * MD などで利用されるリンクのパターン。それ以外にも、はてな形式もこれで回避できます。
	 * 
	 * TODO あとはダブルクオートやシングルクオートなども読み飛ばし対象にすべきか???
	 */
	public static final String SKIPPING_MARKED_LINK_PATTERN = "(\\[.*?\\]|\\(.*?\\))";

	/**
	 * シンプルな URL を MD リンク形式に変換します。
	 * 
	 * @param source
	 * @return
	 */
	public static String convertSimpleUrl2MdLink(final String source) {

		final Pattern patMdLink = Pattern.compile(SKIPPING_MARKED_LINK_PATTERN);
		final Matcher matMdLink = patMdLink.matcher(source);

		final Pattern patURL = Pattern.compile(URL_LINK_PATTERN);
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
