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

public class IgapyonPegDownProcessor extends PegDownProcessor {
	public IgapyonPegDownProcessor(int options) {
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
		return markdownToHtml(markdownSource, linkRenderer,
				verbatimSerializerMap, new ArrayList<ToHtmlSerializerPlugin>());
	}

	public String markdownToHtml(char[] markdownSource,
			LinkRenderer linkRenderer,
			Map<String, VerbatimSerializer> verbatimSerializerMap,
			List<ToHtmlSerializerPlugin> plugins) {
		try {
			RootNode astRoot = parseMarkdown(markdownSource);
			return new IgapyonToHtmlSerializer(linkRenderer).toHtml(astRoot);
		} catch (ParsingTimeoutException e) {
			return null;
		}
	}
}
