package jp.igapyon.diary.v3.proto;

import info.bliki.wiki.model.WikiModel;

public class IgapyonWikiModel extends WikiModel {
	public IgapyonWikiModel(final String imageBaseURL, final String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		// TODO Auto-generated constructor stub
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
