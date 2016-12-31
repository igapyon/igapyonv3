package jp.igapyon.diary.v3.item;

/**
 * 日記アイテムの情報を蓄えるためのクラスです。
 * 
 * @author Toshiki Iga
 */
public class DiaryItemInfo {
	private String uri;
	private String title;
	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
