/*
 *  Igapyon Diary system v3 (IgapyonV3).
 *  Copyright (C) 2015-2017  Toshiki Iga
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 *  Copyright 2015-2017 Toshiki Iga
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package jp.igapyon.diary.v3.util;

import java.util.Comparator;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * ROME の SyndEntry アイテムをソートするためのコンパレータです。
 * 
 * @author Toshiki Iga
 */
public class SyndEntryTitleComparator implements Comparator<SyndEntry> {
	protected boolean isDesc = false;

	public SyndEntryTitleComparator(final boolean isDesc) {
		this.isDesc = isDesc;
	}

	public int compare(final SyndEntry entry1, final SyndEntry entry2) {
		if (isDesc == false) {
			return entry1.getTitle().toLowerCase().compareTo(entry2.getTitle().toLowerCase());
		} else {
			return -entry1.getTitle().toLowerCase().compareTo(entry2.getTitle().toLowerCase());
		}
	}
}
