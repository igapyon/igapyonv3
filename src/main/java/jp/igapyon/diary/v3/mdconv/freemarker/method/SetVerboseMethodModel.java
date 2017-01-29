package jp.igapyon.diary.v3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class SetVerboseMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public SetVerboseMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() < 1) {
			throw new TemplateModelException("setVerbose needs arg.");
		}

		final TemplateScalarModel arg0 = (TemplateScalarModel) argList.get(0);
		return "Exams" + arg0.getAsString();
	}
}
