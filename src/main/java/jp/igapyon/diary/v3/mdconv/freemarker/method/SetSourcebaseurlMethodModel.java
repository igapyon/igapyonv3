package jp.igapyon.diary.v3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class SetSourcebaseurlMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public SetSourcebaseurlMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() != 1) {
			throw new TemplateModelException("setSourcebaseurl needs 1 arg.");
		}

		final TemplateScalarModel arg0 = (TemplateScalarModel) argList.get(0);
		settings.setSourcebaseurl(arg0.getAsString());

		if (arg0.getAsString().endsWith("/")) {
			return "ERROR: setSourcebaseurl: value must not ends with '/' [" + arg0.getAsString() + "].";
		}

		// result blank string
		return "";
	}
}
