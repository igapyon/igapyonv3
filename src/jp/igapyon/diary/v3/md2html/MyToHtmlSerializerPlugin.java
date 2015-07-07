package jp.igapyon.diary.v3.md2html;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class MyToHtmlSerializerPlugin implements ToHtmlSerializerPlugin {
	@Override
	public boolean visit(Node node, Visitor visitor, Printer printer) {
		System.out.println("aaa" + node.toString());
		if (node instanceof SuperNode) {
			SuperNode supernode = (SuperNode) node;
			System.out.println(supernode.toString());
		}

		return false;
	}
}
