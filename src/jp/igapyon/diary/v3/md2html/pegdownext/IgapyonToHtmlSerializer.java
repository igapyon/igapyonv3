/**************************************************************************
 * Copyright (c) 2015, Toshiki Iga, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************** */
/**************************************************************************
 * Copyright 2015 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *********************************************************************** */
package jp.igapyon.diary.v3.md2html.pegdownext;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.TableNode;

public class IgapyonToHtmlSerializer extends ToHtmlSerializer {

	public IgapyonToHtmlSerializer(LinkRenderer linkRenderer) {
		super(linkRenderer);
	}

	@Override
	public void visit(HeaderNode node) {
		final String tag = "h" + node.getLevel();

		// TODO class should be locate at outside.

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
			// Original
			printTag(node, "h" + node.getLevel());
		}
	}

	@Override
	public void visit(TableNode node) {
		if (true) {
			currentTableNode = node;

			printer.println().print('<').print("table")
					.print(" class=\"table table-bordered\"").print('>')
					.indent(+2);
			visitChildren(node);
			printer.indent(-2).println().print('<').print('/').print("table")
					.print('>');

			currentTableNode = null;
		} else {
			// Original
			currentTableNode = node;
			printIndentedTag(node, "table");
			currentTableNode = null;
		}
	}
}
