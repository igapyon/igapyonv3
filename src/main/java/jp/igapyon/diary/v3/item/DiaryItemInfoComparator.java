package jp.igapyon.diary.v3.item;

import java.util.Comparator;

public class DiaryItemInfoComparator implements Comparator<DiaryItemInfo> {
	public int compare(DiaryItemInfo itemInfo1, DiaryItemInfo itemInfo2) {
		return itemInfo1.getUri().compareTo(itemInfo2.getUri());
	}
}
