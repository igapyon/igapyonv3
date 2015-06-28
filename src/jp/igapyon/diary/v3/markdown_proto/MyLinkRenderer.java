package jp.igapyon.diary.v3.markdown_proto;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.WikiLinkNode;

public class MyLinkRenderer extends LinkRenderer {
	@Override
	public Rendering render(WikiLinkNode node) {
		System.out.println("TRACE LinkRenderer#MyLinkRenderer()");
		try {
			// To Hatena keywords.
			String url = "http://d.hatena.ne.jp/keyword/"
					+ URLEncoder.encode(node.getText().replace(' ', '-'),
							"UTF-8") + "";
			return new Rendering(url, node.getText());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}

}
