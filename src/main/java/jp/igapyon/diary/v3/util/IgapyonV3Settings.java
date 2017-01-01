package jp.igapyon.diary.v3.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * いがぴょんの日記v3 システムのための基本設定クラス。
 * 
 * @author Toshiki Iga
 */
public class IgapyonV3Settings {
	/**
	 * TODAY! for newly diary.
	 */
	private Date today = null;

	private List<String[]> doubleKeywordList = new ArrayList<String[]>();

	/**
	 * Default double keywords.
	 */
	public static final String[][] DEFAULT_DOUBLE_KEYWORDS = { { "Axis2", "https://axis.apache.org/axis2/java/core/" },
			{ "RAD Studio", "https://www.embarcadero.com/jp/products/rad-studio" },
			{ "Delphi", "https://www.embarcadero.com/jp/products/delphi" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" } };

	public IgapyonV3Settings() {
		today = new Date();

		for (String[] lookup : DEFAULT_DOUBLE_KEYWORDS) {
			doubleKeywordList.add(lookup);
		}
	}

	public Date getToday() {
		return today;
	}

	public List<String[]> getDoubleKeywordList() {
		return doubleKeywordList;
	}
}
