package jp.igapyon.diary.igapyonv3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

public class SetGenerateKeywordIfNeededMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public SetGenerateKeywordIfNeededMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() != 1) {
			throw new TemplateModelException("setGeneratekeywordifneeded needs 1 arg.");
		}

		final TemplateScalarModel arg0 = (TemplateScalarModel) argList.get(0);
		if ("true".equals(arg0.getAsString().toLowerCase()) || "yes".equals(arg0.getAsString().toLowerCase())) {
			settings.setGenerateKeywordIfNeeded(true);
		} else if ("false".equals(arg0.getAsString().toLowerCase()) || "no".equals(arg0.getAsString().toLowerCase())) {
			settings.setGenerateKeywordIfNeeded(false);
		} else {
			return "ERROR: setGeneratekeywordifneeded: Unknown value [" + arg0.getAsString() + "].";
		}

		// result blank string
		return "";
	}
}
