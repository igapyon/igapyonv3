package jp.igapyon.diary.v3.converter.hatenadiary;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IgapyonHatenaDiaryItem {
	protected Date date;
	protected String title;
	protected String body;

	final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getString() {
		return "[" + format.format(getDate()) + "] " + getTitle() + " ["
				+ getBody() + "]";
	}
}
