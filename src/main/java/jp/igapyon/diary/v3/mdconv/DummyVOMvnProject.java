package jp.igapyon.diary.v3.mdconv;

/**
 * FreeMarker の解釈器が 日記上の Maven 記述を検知してしまう問題を回避するためだけのクラス。
 * 
 * @author Toshiki Iga
 */
public class DummyVOMvnProject {
	protected DummyVOMvnProjectBuild build = new DummyVOMvnProjectBuild();

	public DummyVOMvnProjectBuild getBuild() {
		return build;
	}

}