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
package jp.igapyon.diary.v3.md2html;

import org.pegdown.Extensions;

public class IgapyonMd2HtmlSettings {
	/**
	 * if null: use CDN.
	 */
	protected String targetPathJQuery;

	/**
	 * if null: use CDN.
	 */
	protected String targetPathBootstrap;

	protected String htmlTitle;

	protected String htmlDescription;

	protected int pegdownProcessorExtensions = Extensions.AUTOLINKS
			| Extensions.STRIKETHROUGH | Extensions.FENCED_CODE_BLOCKS
			| Extensions.TABLES | Extensions.WIKILINKS;

	public String getTargetPathJQuery() {
		return targetPathJQuery;
	}

	public void setTargetPathJQuery(String targetPathJQuery) {
		this.targetPathJQuery = targetPathJQuery;
	}

	public String getTargetPathBootstrap() {
		return targetPathBootstrap;
	}

	public void setTargetPathBootstrap(String targetPathBootstrap) {
		this.targetPathBootstrap = targetPathBootstrap;
	}

	public int getPegdownProcessorExtensions() {
		return pegdownProcessorExtensions;
	}

	public void setPegdownProcessorExtensions(int pegdownProcessorExtensions) {
		this.pegdownProcessorExtensions = pegdownProcessorExtensions;
	}

	// html section

	public String getHtmlTitle() {
		return htmlTitle;
	}

	public void setHtmlTitle(String htmlTitle) {
		this.htmlTitle = htmlTitle;
	}

	public String getHtmlDescription() {
		return htmlDescription;
	}

	public void setHtmlDescription(String htmlDescription) {
		this.htmlDescription = htmlDescription;
	}
}
