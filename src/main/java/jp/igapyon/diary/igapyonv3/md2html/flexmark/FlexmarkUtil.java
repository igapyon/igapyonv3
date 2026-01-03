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

package jp.igapyon.diary.igapyonv3.md2html.flexmark;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ext.tables.TableBlock;
import com.vladsch.flexmark.ext.wikilink.WikiLink;
import com.vladsch.flexmark.html.AttributeProvider;
import com.vladsch.flexmark.html.AttributeProviderFactory;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.html.HtmlWriter;
import com.vladsch.flexmark.html.renderer.AttributablePart;
import com.vladsch.flexmark.html.renderer.LinkResolverContext;
import com.vladsch.flexmark.html.renderer.NodeRenderer;
import com.vladsch.flexmark.html.renderer.NodeRendererContext;
import com.vladsch.flexmark.html.renderer.NodeRendererFactory;
import com.vladsch.flexmark.html.renderer.NodeRenderingHandler;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.ast.TextCollectingVisitor;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.html.MutableAttributes;

import jp.igapyon.diary.igapyonv3.md2html.pegdownext.IgapyonPegDownTagConf;

public final class FlexmarkUtil {
	private FlexmarkUtil() {
		// util class
	}

	public static Parser getParser() {
		return FlexmarkPegdownOpts.PARSER;
	}

	public static String renderMarkdown(final String markdown, final IgapyonPegDownTagConf tagConf) {
		final HtmlRenderer renderer = buildRenderer(FlexmarkPegdownOpts.OPTIONS, tagConf);
		final Node document = getParser().parse(markdown);
		return renderer.render(document);
	}

	public static Heading getFirstHeading(final Node document) {
		final List<Heading> headings = collectHeadings(document);
		return headings.isEmpty() ? null : headings.get(0);
	}

	public static Heading getSecondHeading(final Node document) {
		final List<Heading> headings = collectHeadings(document);
		return headings.size() > 1 ? headings.get(1) : null;
	}

	public static String collectText(final Node node) {
		if (node == null) {
			return "";
		}
		return new TextCollectingVisitor().collectAndGetText(node).toString();
	}

	public static String collectTextFromMarkdown(final String markdown) {
		final Node document = getParser().parse(markdown);
		return collectText(document);
	}

	private static List<Heading> collectHeadings(final Node document) {
		final List<Heading> headings = new ArrayList<Heading>();
		collectHeadingsRecursive(document, headings);
		return headings;
	}

	private static void collectHeadingsRecursive(final Node node, final List<Heading> headings) {
		for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
			if (child instanceof Heading) {
				headings.add((Heading) child);
			}
			collectHeadingsRecursive(child, headings);
		}
	}

	private static HtmlRenderer buildRenderer(final DataHolder options, final IgapyonPegDownTagConf tagConf) {
		return HtmlRenderer.builder(options)
				.attributeProviderFactory(new AttributeProviderFactory() {
					@Override
					public Set<Class<?>> getAfterDependents() {
						return java.util.Collections.emptySet();
					}

					@Override
					public Set<Class<?>> getBeforeDependents() {
						return java.util.Collections.emptySet();
					}

					@Override
					public boolean affectsGlobalScope() {
						return false;
					}

					@Override
					public AttributeProvider apply(final LinkResolverContext contextOptions) {
						return new TagClassAttributeProvider(tagConf);
					}
				})
				.nodeRendererFactory(new NodeRendererFactory() {
					@Override
					public NodeRenderer apply(final DataHolder contextOptions) {
						return new WikiLinkNodeRenderer();
					}
				}).build();
	}

	private static class TagClassAttributeProvider implements AttributeProvider {
		private final IgapyonPegDownTagConf tagConf;

		TagClassAttributeProvider(final IgapyonPegDownTagConf tagConf) {
			this.tagConf = tagConf;
		}

		@Override
		public void setAttributes(final Node node, final AttributablePart part, final MutableAttributes attributes) {
			if (tagConf == null || part != AttributablePart.NODE) {
				return;
			}
			final String tagName = getTagName(node);
			if (tagName == null) {
				return;
			}
			final String classValue = tagConf.getAttrClassValue(tagName);
			if (classValue != null) {
				attributes.replaceValue("class", classValue);
			}
		}

		private String getTagName(final Node node) {
			if (node instanceof Heading) {
				return "h" + ((Heading) node).getLevel();
			}
			if (node instanceof TableBlock) {
				return "table";
			}
			return null;
		}
	}

	private static class WikiLinkNodeRenderer implements NodeRenderer {
		@Override
		public Set<NodeRenderingHandler<?>> getNodeRenderingHandlers() {
			final Set<NodeRenderingHandler<?>> set = new HashSet<NodeRenderingHandler<?>>();
			set.add(new NodeRenderingHandler<WikiLink>(WikiLink.class, this::render));
			return set;
		}

		private void render(final WikiLink node, final NodeRendererContext context, final HtmlWriter html) {
			final String text = node.getText().toString();
			final String url = buildHatenaKeywordUrl(text);
			html.attr("href", url).withAttr().tag("a");
			html.text(text);
			html.tag("/a");
		}

		private String buildHatenaKeywordUrl(final String text) {
			try {
				return "http://d.hatena.ne.jp/keyword/" + URLEncoder.encode(text.replace(' ', '-'), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}
