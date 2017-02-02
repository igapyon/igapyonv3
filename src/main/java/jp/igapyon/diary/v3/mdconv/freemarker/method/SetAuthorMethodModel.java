package jp.igapyon.diary.v3.mdconv.freemarker.method;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;
import jp.igapyon.diary.v3.util.IgapyonV3Settings;

public class SetAuthorMethodModel implements TemplateMethodModelEx {
	private IgapyonV3Settings settings = null;

	public SetAuthorMethodModel(final IgapyonV3Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") final List argList) throws TemplateModelException {
		if (argList.size() != 1) {
			throw new TemplateModelException("setAuthor needs 1 arg.");
		}

		final TemplateScalarModel arg0 = (TemplateScalarModel) argList.get(0);
		settings.setAuthor(arg0.getAsString());

		// result blank string
		return "";
	}
}
