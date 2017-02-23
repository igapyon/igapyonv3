package jp.igapyon.diary.igapyonv3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

public class ShowSettingsMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public ShowSettingsMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() > 0) {
			throw new TemplateModelException("showSettings do not accept arg.");
		}

		// result blank string
		return "" //
				+ "* verbose: " + settings.isVerbose() + "\n" //
				+ "* debug: " + settings.isDebug() + "\n" //
				+ "* baseurl: " + settings.getBaseurl() + "\n" //
				+ "* sourcebaseurl: " + settings.getSourcebaseurl() + "\n" //
				+ "* author: " + settings.getAuthor() + "\n" //
				+ "* generatetodaydiary: " + settings.isGenerateTodayDiary() + "\n" //
				+ "* convertmarkdown2html: " + settings.isConvertMarkdown2Html() + "\n" //
				+ "* duplicatefakehtmlmd: " + settings.isDuplicateFakeHtmlMd() + "\n" //
				+ "* generatekeywordifneeded: " + settings.isGenerateKeywordIfNeeded() + "\n" //
				+ "* sitetitle: " + settings.getSiteTitle(); // site seriese:
																// for
																// inittemplate

		// NOTICE: rootdir not to be shown.
	}
}
