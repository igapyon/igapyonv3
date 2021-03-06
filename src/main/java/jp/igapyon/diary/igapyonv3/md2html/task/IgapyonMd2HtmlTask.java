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

package jp.igapyon.diary.igapyonv3.md2html.task;

import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html;
import jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2HtmlConstants;

/**
 * Igapyon's Markdown to Html converter.
 * 
 * ※初期の github gh-pages 対応では、これは利用しません。
 * 
 * @author Toshiki Iga
 */
public class IgapyonMd2HtmlTask extends Task {
	protected String source;
	protected String target;
	protected boolean recursivedir = false;

	public String getSource() {
		return source;
	}

	public void setSource(final String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(final String target) {
		this.target = target;
	}

	public boolean isRecursivedir() {
		return recursivedir;
	}

	public void setRecursivedir(final boolean recursivedir) {
		this.recursivedir = recursivedir;
	}

	@Override
	public void execute() throws BuildException {
		checkAttr();

		System.out.println(
				"md2html: " + IgapyonMd2HtmlConstants.PROGRAM_DISPLAY_NAME + " ver" + IgapyonMd2HtmlConstants.VERSION);
		System.out.println("   source:[" + source + "]");
		System.out.println("   target:[" + target + "]");
		System.out.println("   recursivedir:" + recursivedir);

		try {
			new IgapyonMd2Html().processDir(source, target, recursivedir);
		} catch (IOException e) {
			throw new BuildException("error occured.", e);
		}
	}

	protected void checkAttr() throws BuildException {
		if (source == null) {
			throw new BuildException("source is required.");
		}
		if (target == null) {
			throw new BuildException("target is required.");
		}
	}
}
