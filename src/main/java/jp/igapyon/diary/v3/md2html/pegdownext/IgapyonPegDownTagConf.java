/**************************************************************************
 * Copyright (c) 2015, Toshiki Iga, All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 * If not, see <http://www.gnu.org/licenses/>.
 *********************************************************************** */
/**************************************************************************
 * Copyright 2015 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *********************************************************************** */
package jp.igapyon.diary.v3.md2html.pegdownext;

import java.util.HashMap;
import java.util.Map;

public class IgapyonPegDownTagConf {
	protected final Map<String, IgapyonPegDownTagAttr> tagAttrMap = new HashMap<String, IgapyonPegDownTagAttr>();

	public static IgapyonPegDownTagConf getDefault() {
		final IgapyonPegDownTagConf tagConf = new IgapyonPegDownTagConf();

		tagConf.setAttrClassValue("h1", "alert alert-danger");
		tagConf.setAttrClassValue("h2", "alert alert-warning");
		tagConf.setAttrClassValue("h3", "bg-success");
		tagConf.setAttrClassValue("h4", "bg-info");
		tagConf.setAttrClassValue("table", "table table-bordered");

		return tagConf;
	}

	public void setAttrClassValue(final String tagName, final String tagClassValue) {
		final IgapyonPegDownTagAttr tagAttr = new IgapyonPegDownTagAttr();
		tagAttr.setTagClassValue(tagClassValue);

		tagAttrMap.put(tagName, tagAttr);
	}

	public String getAttrClassValue(final String tagName) {
		final IgapyonPegDownTagAttr tagAttr = tagAttrMap.get(tagName);
		if (tagAttr != null) {
			return tagAttr.getTagClassValue();
		}

		return null;
	}
}
