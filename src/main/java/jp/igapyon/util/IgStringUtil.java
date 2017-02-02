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

package jp.igapyon.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Simple String util.
 * 
 * @author Toshiki Iga
 */
public class IgStringUtil {
	/**
	 * get abbreviated string with 13 and 38
	 * 
	 * such as hardcoded StringUtils.abbreviateMiddle
	 * 
	 * @param message
	 *            input message.
	 * @return abbreviated message.
	 */
	public static String abbreviateMiddle(final String message) {
		// hardcoded prefix length.
		final int PRELEN = 13;
		// hardcoded maxlen by regarding iPhone SE width.
		final int MAXLEN = 38;

		if (message.length() <= MAXLEN) {
			return message;
		}

		return message.substring(0, PRELEN)
				+ StringUtils.abbreviateMiddle(message.substring(PRELEN), "...", MAXLEN - PRELEN);
	}
}
