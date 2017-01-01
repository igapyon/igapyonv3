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
	public static final String[][] DEFAULT_DOUBLE_KEYWORDS = { //

			// blanco Framework
			{ "blanco Framework", "https://ja.osdn.net/projects/blancofw/wiki/blancofw" },
			{ "Blanco2g", "https://ja.osdn.net/projects/blancofw/wiki/Blanco2g" },
			{ "blancoCg", "https://github.com/igapyon/blancoCg" }, { "Ubuntu", "https://www.ubuntu.com/" },
			{ "blancoDb", "http://www.igapyon.jp/blanco/blancodb.html" },
			{ "blancoDbDotNet", "http://www.igapyon.jp/blanco/blancodbdotnet.html" },
			{ "blancoResourceBundle", "http://www.igapyon.jp/blanco/blancoresourcebundle.html" },
			{ "blancoMailCore", "http://www.igapyon.jp/blanco/blancomailcore.html" },
			{ "Chrome", "https://www.google.co.jp/chrome/browser/" }, { "Selenium", "http://www.seleniumhq.org/" },
			{ "Groovy", "http://www.groovy-lang.org/" },
			{ "Java", "http://www.oracle.com/technetwork/java/index.html" },
			{ "Object Pascal", "https://ja.wikipedia.org/wiki/Object_Pascal" },
			{ "Force.com", "https://www.salesforce.com/products/platform/products/force/" },
			{ "LLVM", "http://llvm.org/" }, { "OpenDocument", "https://ja.wikipedia.org/wiki/OpenDocument" },

			// oss
			{ "Apache", "https://www.apache.org/" }, { "Axis2", "https://axis.apache.org/axis2/java/core/" },
			{ "JExcelApi", "http://jexcelapi.sourceforge.net/" }, { "OmegaT", "http://www.omegat.org/ja/omegat.html" },
			{ "Jersey", "https://jersey.java.net/" },

			// it
			{ "IoT", "https://ja.wikipedia.org/wiki/%E3%83%A2%E3%83%8E%E3%81%AE%E3%82%A4%E3%83%B3%E3%82%BF%E3%83%BC%E3%83%8D%E3%83%83%E3%83%88" },
			{ "AirPrint", "https://support.apple.com/ja-jp/HT201311" }, { "Cordova", "https://cordova.apache.org/" },
			{ "VMware", "http://www.vmware.com/jp.html" }, { "iOS", "http://www.apple.com/jp/ios/ios-10/" },

			// embarcadero
			{ "RAD Studio", "https://www.embarcadero.com/jp/products/rad-studio" },
			{ "Delphi", "https://www.embarcadero.com/jp/products/delphi" },
			{ "Appmethod", "https://ja.wikipedia.org/wiki/Appmethod" },
			{ "InterBase", "https://ja.wikipedia.org/wiki/InterBase" },
			{ "FireUI", "https://www.embarcadero.com/jp/products/rad-studio/fireui" },
			{ "VCL", "https://ja.wikipedia.org/wiki/Visual_Component_Library" },
			{ "C++Builder", "https://www.embarcadero.com/jp/products/cbuilder" },

			// names
			{ "ネコバス", "http://nlab.itmedia.co.jp/nl/articles/1607/15/news147.html" },//
	};

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
