package jp.igapyon.diary.igapyonv3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import jp.igapyon.diary.igapyonv3.util.IgapyonV3Settings;

public class SetBaseurlMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public SetBaseurlMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() != 1) {
			throw new TemplateModelException("setBaseurl needs 1 arg.");
		}

		final TemplateScalarModel arg0 = (TemplateScalarModel) argList.get(0);
		if (arg0.getAsString().endsWith("/")) {
			return "ERROR: setBaseurl: value must not ends with '/' [" + arg0.getAsString() + "].";
		}

		settings.setBaseurl(arg0.getAsString());

		// result blank string
		return "";
	}
}
