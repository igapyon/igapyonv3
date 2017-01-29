package jp.igapyon.diary.v3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

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
				+ "* baseurl: " + settings.getBaseurl() + "\n" //
				+ "* sourcebaseurl: " + settings.getSourcebaseurl() + "\n" //
				+ "* author: " + settings.getAuthor();
		// NOTICE: rootdir not to be shown.
	}
}
