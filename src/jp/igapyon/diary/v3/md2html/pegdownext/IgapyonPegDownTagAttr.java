package jp.igapyon.diary.v3.md2html.pegdownext;

public class IgapyonPegDownTagAttr {
	/**
	 * <tag class="value" />
	 * 
	 * ex: [alert alert-warning]
	 */
	protected String tagClassValue;

	public String getTagClassValue() {
		return tagClassValue;
	}

	public void setTagClassValue(String tagClassValue) {
		this.tagClassValue = tagClassValue;
	}
}
