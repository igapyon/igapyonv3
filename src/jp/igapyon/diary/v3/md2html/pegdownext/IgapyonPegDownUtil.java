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

import org.pegdown.ast.AnchorLinkNode;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.Node;
import org.pegdown.ast.ParaNode;
import org.pegdown.ast.RootNode;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.TextNode;

public class IgapyonPegDownUtil {
	public static HeaderNode getFistHeader(final RootNode rootNode) {
		for (Node node : rootNode.getChildren()) {
			if (node instanceof HeaderNode) {
				return (HeaderNode) node;
			}
		}
		return null;
	}

	public static HeaderNode getSecondHeader(final RootNode rootNode) {
		boolean isFirstHeader = true;
		for (Node node : rootNode.getChildren()) {
			if (node instanceof HeaderNode) {
				if (isFirstHeader) {
					isFirstHeader = false;
				} else {
					return (HeaderNode) node;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String getElementText(final Node node) {
		if (node instanceof AnchorLinkNode) {
			final AnchorLinkNode lookNode = (AnchorLinkNode) node;
			return lookNode.getText();
		} else if (node instanceof ParaNode) {
			final ParaNode lookNode = (ParaNode) node;
			return getElementChildText(lookNode);
		} else if (node instanceof SuperNode) {
			final SuperNode lookNode = (SuperNode) node;
			return getElementChildText(lookNode);
		} else if (node instanceof TextNode) {
			final TextNode lookNode = (TextNode) node;
			return lookNode.getText();
		} else {
			System.out.println("TRACE: unknown node: " + node.toString());
			return "";
		}
	}

	public static String getElementChildText(final Node rootNode) {
		return getElementChildText(rootNode, 0, Integer.MAX_VALUE);
	}

	public static String getElementChildText(final Node rootNode,
			final int startIndex, final int endIndex) {
		StringBuilder builder = null;
		for (Node node : rootNode.getChildren()) {
			if (node.getStartIndex() < startIndex) {
				continue;
			}
			if (node.getEndIndex() > endIndex) {
				continue;
			}
			if (builder == null) {
				builder = new StringBuilder();
			}
			builder.append(getElementText(node));
		}
		if (builder == null) {
			return null;
		}
		return builder.toString();
	}
}
