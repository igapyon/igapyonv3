package jp.igapyon.diary.v3.item;

import java.util.Comparator;

/**
 * 日記アイテムの情報を蓄えるためのクラスをソートするためのコンパレータです。
 * 
 * @author Toshiki Iga
 */
public class DiaryItemInfoComparator implements Comparator<DiaryItemInfo> {
	public int compare(DiaryItemInfo itemInfo1, DiaryItemInfo itemInfo2) {
		// TODO 年の99年対応の考慮が必要なはず。
		return itemInfo1.getUri().compareTo(itemInfo2.getUri());
	}
}
