/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.diary.igapyonv3.md2html.pegdownext;

import org.pegdown.LinkRenderer;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.TableNode;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * ※初期の github gh-pages 対応では、これは利用しません。
 * 
 * @author Toshiki Iga
 */
public class IgapyonToHtmlSerializer extends ToHtmlSerializer {
	protected IgapyonPegDownTagConf tagConf;

	public IgapyonToHtmlSerializer(final LinkRenderer linkRenderer, final IgapyonPegDownTagConf tagConf) {
		super(linkRenderer);
		this.tagConf = tagConf;
	}

	@Override
	public void visit(HeaderNode node) {
		final String tag = "h" + node.getLevel();

		/**
		 * if (tag.equals("h2")) { printer.print('<').print(tag); if
		 * (tagConf.getAttrClassValue(tag) != null) { printer.print(" class=\""
		 * + tagConf.getAttrClassValue(tag) + "\""); } printer.print('>');
		 * visitChildren(node);
		 * printer.print('<').print('/').print(tag).print('>'); } else if
		 * (tag.equals("h3")) { printer.print('<').print(tag); if
		 * (tagConf.getAttrClassValue(tag) != null) { printer.print(" class=\""
		 * + tagConf.getAttrClassValue(tag) + "\""); } printer.print('>');
		 * visitChildren(node);
		 * printer.print('<').print('/').print(tag).print('>'); } else if
		 * (tag.equals("h4")) { printer.print('<').print(tag); if
		 * (tagConf.getAttrClassValue(tag) != null) { printer.print(" class=\""
		 * + tagConf.getAttrClassValue(tag) + "\""); } printer.print('>');
		 * visitChildren(node);
		 * printer.print('<').print('/').print(tag).print('>'); } else { //
		 * Original printTag(node, "h" + node.getLevel()); }
		 */

		if (tagConf.getAttrClassValue(tag) != null) {
			printer.print('<').print(tag);
			if (tagConf.getAttrClassValue(tag) != null) {
				printer.print(" class=\"" + tagConf.getAttrClassValue(tag) + "\"");
			}
			printer.print('>');
			visitChildren(node);
			printer.print('<').print('/').print(tag).print('>');
		} else {
			// Original
			printTag(node, "h" + node.getLevel());
		}
	}

	@Override
	public void visit(TableNode node) {
		if (tagConf.getAttrClassValue("table") != null) {
			currentTableNode = node;

			printer.println().print('<');
			printer.print("table");

			if (tagConf.getAttrClassValue("table") != null) {
				printer.print(" class=\"" + tagConf.getAttrClassValue("table") + "\"");
			}

			printer.print('>').indent(+2);
			visitChildren(node);
			printer.indent(-2).println().print('<').print('/').print("table").print('>');

			currentTableNode = null;
		} else {
			// Original
			currentTableNode = node;
			printIndentedTag(node, "table");
			currentTableNode = null;
		}
	}
}
