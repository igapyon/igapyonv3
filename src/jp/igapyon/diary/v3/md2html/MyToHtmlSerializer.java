package jp.igapyon.diary.v3.md2html;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.HeaderNode;

public class MyToHtmlSerializer extends ToHtmlSerializer {

	public MyToHtmlSerializer(LinkRenderer linkRenderer) {
		super(linkRenderer);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void visit(HeaderNode node) {
		// printTag(node, "h" + node.getLevel());
		final String tag = "h" + node.getLevel();

		if (tag.equals("h2")) {
			printer.print('<').print(tag)
					.print(" class=\"alert alert-warning\"").print('>');
			visitChildren(node);
			printer.print('<').print('/').print(tag).print('>');
		} else if (tag.equals("h3")) {
			printer.print('<').print(tag).print(" class=\"bg-success\"")
					.print('>');
			visitChildren(node);
			printer.print('<').print('/').print(tag).print('>');
		} else if (tag.equals("h4")) {
			printer.print('<').print(tag).print(" class=\"bg-info\"")
					.print('>');
			visitChildren(node);
			printer.print('<').print('/').print(tag).print('>');
		} else {
			printTag(node, "h" + node.getLevel());
		}
	}
}
