package jp.igapyon.diary.v3.proto;

import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.SectionHeader;
import info.bliki.wiki.filter.WikipediaParser;
import info.bliki.wiki.model.ITableOfContent;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;

public class IgapyonWikiModel extends WikiModel {
	static {
		// see:
		// https://bitbucket.org/axelclk/info.bliki.wiki/wiki/Mediawiki2HTML
		//
		// By default the rendering engine doesn't allow the style attribute to
		// avoid cross-site scripting risks. You can define the style attribute
		// as allowed in a static block of your WikiModel implementation.
		TagNode.addAllowedAttribute("style");
	}

	public IgapyonWikiModel(final String imageBaseURL, final String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Append a new head to the table of content
	 * 
	 * @param rawHead
	 * @param headLevel
	 */
	@Override
	public ITableOfContent appendHead(String rawHead, int headLevel,
			boolean noToC, int headCounter, int startPosition, int endPosition) {
		TagStack localStack = WikipediaParser.parseRecursive(rawHead.trim(),
				this, true, true);

		WPTag headTagNode = new WPTag("h" + headLevel);
		TagNode spanTagNode = new TagNode("span");
		// Example:
		// <h2><span class="mw-headline" id="Header_level_2">Header level
		// 2</span></h2>
		spanTagNode.addChildren(localStack.getNodeList());
		headTagNode.addChild(spanTagNode);
		String tocHead = headTagNode.getBodyString();
		String anchor = Encoder.encodeDotUrl(tocHead);
		createTableOfContent(false);
		if (!noToC && (headCounter > 3)) {
			fTableOfContentTag.setShowToC(true);
		}
		if (fToCSet.contains(anchor)) {
			String newAnchor = anchor;
			for (int i = 2; i < Integer.MAX_VALUE; i++) {
				newAnchor = anchor + '_' + Integer.toString(i);
				if (!fToCSet.contains(newAnchor)) {
					break;
				}
			}
			anchor = newAnchor;
		}
		fToCSet.add(anchor);
		SectionHeader strPair = new SectionHeader(headLevel, startPosition,
				endPosition, tocHead, anchor);
		addToTableOfContent(fTableOfContent, strPair, headLevel);
		if (getRecursionLevel() == 1) {
			buildEditLinkUrl(fSectionCounter++);
		}
		spanTagNode.addAttribute("class", "mw-headline222", true);
		spanTagNode.addAttribute("id", anchor, true);

		append(headTagNode);
		return fTableOfContentTag;
	}

	public void appendInternalLink(String topic, String hashSection,
			String topicDescription, String cssClass, boolean parseRecursive) {
		super.appendInternalLink(topic, hashSection, topicDescription,
				cssClass, parseRecursive);
		System.out.println("TRACE: WikiModel#appendInternalLink(" + topic + ","
				+ hashSection + "," + topicDescription + "," + cssClass + ","
				+ parseRecursive + ")");
	}

	public void parseInternalImageLink(final String imageNamespace,
			final String rawImageLink) {
		super.parseInternalImageLink(imageNamespace, rawImageLink);
		System.out.println("TRACE: WikiModel#parseInternalImageLink("
				+ imageNamespace + "," + rawImageLink + ")");
	}
}
