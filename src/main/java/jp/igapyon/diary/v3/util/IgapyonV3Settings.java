package jp.igapyon.diary.v3.util;

import java.util.ArrayList;
import java.util.List;

public class IgapyonV3Settings {
	private List<String[]> doubleKeywordList = new ArrayList<String[]>();

	public static final String[][] DEFAULT_DOUBLE_KEYWORDS = { { "Axis2", "https://axis.apache.org/axis2/java/core/" },
			{ "RAD Studio", "https://www.embarcadero.com/jp/products/rad-studio" },
			{ "Delphi", "https://www.embarcadero.com/jp/products/delphi" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" } };

	public IgapyonV3Settings() {
		for (String[] lookup : DEFAULT_DOUBLE_KEYWORDS) {
			doubleKeywordList.add(lookup);
		}
	}

	public List<String[]> getDoubleKeywordList() {
		return doubleKeywordList;
	}

	public void setDoubleKeywordList(List<String[]> doubleKeywordList) {
		this.doubleKeywordList = doubleKeywordList;
	}
}
