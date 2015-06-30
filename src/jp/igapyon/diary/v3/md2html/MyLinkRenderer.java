package jp.igapyon.diary.v3.md2html;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.WikiLinkNode;

public class MyLinkRenderer extends LinkRenderer {
	/**
	 * Override Wiki link. for [[]] style.
	 */
	@Override
	public Rendering render(final WikiLinkNode node) {
		try {
			// Treat as Hatena keywords.
			final String url = "http://d.hatena.ne.jp/keyword/"
					+ URLEncoder.encode(node.getText().replace(' ', '-'),
							"UTF-8") + "";
			return new Rendering(url, node.getText());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}
}
