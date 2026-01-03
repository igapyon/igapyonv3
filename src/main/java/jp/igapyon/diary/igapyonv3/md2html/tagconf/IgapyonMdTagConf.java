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

package jp.igapyon.diary.igapyonv3.md2html.tagconf;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @author Toshiki Iga
 */
public class IgapyonMdTagConf {
	protected final Map<String, IgapyonMdTagAttr> tagAttrMap = new HashMap<String, IgapyonMdTagAttr>();

	public static IgapyonMdTagConf getDefault() {
		final IgapyonMdTagConf tagConf = new IgapyonMdTagConf();

		tagConf.setAttrClassValue("h1",
				"mt-8 mb-4 rounded-md bg-red-100 px-4 py-2 text-3xl font-bold tracking-tight text-red-900");
		tagConf.setAttrClassValue("h2",
				"mt-8 mb-3 rounded-md bg-amber-100 px-3 py-2 text-xl font-semibold text-amber-900");
		tagConf.setAttrClassValue("h3",
				"mt-6 mb-2 rounded-md bg-emerald-100 px-3 py-1.5 text-lg font-semibold text-emerald-900");
		tagConf.setAttrClassValue("h4",
				"mt-5 mb-2 rounded-md bg-sky-100 px-3 py-1.5 text-base font-semibold text-sky-900");
		tagConf.setAttrClassValue("p", "mt-2");
		tagConf.setAttrClassValue("ul", "list-disc pl-6");
		tagConf.setAttrClassValue("ol", "list-decimal pl-6");
		tagConf.setAttrClassValue("pre", "my-4 overflow-auto rounded bg-slate-900 p-4 text-slate-100 text-sm");
		tagConf.setAttrClassValue("table", "w-full border border-slate-300 text-left text-sm");
		tagConf.setAttrClassValue("th", "border border-slate-300 bg-slate-50 px-3 py-2 font-semibold");
		tagConf.setAttrClassValue("td", "border border-slate-300 px-3 py-2");
		tagConf.setAttrClassValue("a",
				"text-slate-700 font-medium hover:text-sky-800 hover:bg-sky-50 rounded px-0.5 -mx-0.5");

		return tagConf;
	}

	public void setAttrClassValue(final String tagName, final String tagClassValue) {
		final IgapyonMdTagAttr tagAttr = new IgapyonMdTagAttr();
		tagAttr.setTagClassValue(tagClassValue);

		tagAttrMap.put(tagName, tagAttr);
	}

	public String getAttrClassValue(final String tagName) {
		final IgapyonMdTagAttr tagAttr = tagAttrMap.get(tagName);
		if (tagAttr != null) {
			return tagAttr.getTagClassValue();
		}

		return null;
	}
}
