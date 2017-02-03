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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.pegdown.LinkRenderer;
import org.pegdown.ast.WikiLinkNode;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * ※初期の github gh-pages 対応では、これは利用しません。
 * 
 * @author Toshiki Iga
 */
public class IgapyonLinkRenderer extends LinkRenderer {
	/**
	 * Override Wiki link. for [[]] style.
	 * 
	 * TODO support WikiPedia en/jp.
	 */
	@Override
	public Rendering render(final WikiLinkNode node) {
		try {
			// Treat as Hatena keywords.
			final String url = "http://d.hatena.ne.jp/keyword/"
					+ URLEncoder.encode(node.getText().replace(' ', '-'), "UTF-8") + "";
			return new Rendering(url, node.getText());
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException();
		}
	}
}
