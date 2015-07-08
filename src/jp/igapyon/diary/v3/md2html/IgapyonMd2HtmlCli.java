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

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class IgapyonMd2HtmlCli {
	public void process(final String[] args) throws IOException {
		final Options options = new Options();
		options.addOption("s", true,
				"source directory which contains .md file.");
		options.addOption("t", true,
				"target directory which will contains .html file.");
		options.addOption("r", false, "treat directory recursive.");
		options.addOption("h", false, "show help.");

		final CommandLineParser parser = new DefaultParser();
		CommandLine commandLine;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.toString());
			return;
		}

		if (commandLine.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("md2html", options);
			return;
		}

		if (commandLine.hasOption("s") == false) {
			throw new IllegalArgumentException("-s must be set.");
		}
		if (commandLine.hasOption("t") == false) {
			throw new IllegalArgumentException("-t must be set.");
		}

		final String source = commandLine.getOptionValue("s");
		final String target = commandLine.getOptionValue("t");
		final boolean recursivedir = commandLine.hasOption("r");

		System.out.println(IgapyonMd2HtmlConstants.PROGRAM_DISPLAY_NAME
				+ " ver:" + IgapyonMd2HtmlConstants.VERSION);
		System.out.println("  source:[" + source + "]");
		System.out.println("  target:[" + target + "]");
		System.out.println("  recursivedir=" + recursivedir);

		new IgapyonMd2Html().processDir(source, target, recursivedir);
	}
}
