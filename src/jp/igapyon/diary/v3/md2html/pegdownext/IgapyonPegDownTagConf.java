package jp.igapyon.diary.v3.md2html.pegdownext;

import java.util.HashMap;
import java.util.Map;

public class IgapyonPegDownTagConf {
	protected final Map<String, IgapyonPegDownTagAttr> tagAttrMap = new HashMap<>();

	public static IgapyonPegDownTagConf getDefault() {
		final IgapyonPegDownTagConf tagConf = new IgapyonPegDownTagConf();

		tagConf.setAttrClassValue("h2", "alert alert-warning");
		tagConf.setAttrClassValue("h3", "bg-success");
		tagConf.setAttrClassValue("h4", "bg-info");
		tagConf.setAttrClassValue("table", "table table-bordered");

		return tagConf;
	}

	public void setAttrClassValue(final String tagName,
			final String tagClassValue) {
		final IgapyonPegDownTagAttr tagAttr = new IgapyonPegDownTagAttr();
		tagAttr.setTagClassValue(tagClassValue);

		tagAttrMap.put(tagName, tagAttr);
	}

	public String getAttrClassValue(final String tagName) {
		final IgapyonPegDownTagAttr tagAttr = tagAttrMap.get(tagName);
		if (tagAttr != null) {
			return tagAttr.getTagClassValue();
		}

		return null;
	}
}
