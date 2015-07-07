package jp.igapyon.diary.v3.md2html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.pegdown.LinkRenderer;
import org.pegdown.ParsingTimeoutException;
import org.pegdown.PegDownProcessor;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class MyPegDownProcessor extends PegDownProcessor {
	public MyPegDownProcessor(int options) {
		super(options);
	}

	public String markdownToHtml(String markdownSource,
			LinkRenderer linkRenderer) {
		return markdownToHtml(markdownSource.toCharArray(), linkRenderer);
	}

	public String markdownToHtml(char[] markdownSource,
			LinkRenderer linkRenderer) {
		return markdownToHtml(markdownSource, linkRenderer,
				Collections.<String, VerbatimSerializer> emptyMap());
	}

	public String markdownToHtml(char[] markdownSource,
			LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializerMap) {
		List<ToHtmlSerializerPlugin> plugins = new ArrayList<ToHtmlSerializerPlugin>();
		return markdownToHtml(markdownSource, linkRenderer,
				verbatimSerializerMap, plugins);
	}

	public String markdownToHtml(char[] markdownSource,
			LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializerMap,
			List<ToHtmlSerializerPlugin> plugins) {
		try {
			RootNode astRoot = parseMarkdown(markdownSource);
			return new MyToHtmlSerializer(linkRenderer).toHtml(astRoot);
		} catch (ParsingTimeoutException e) {
			return null;
		}
	}
}
