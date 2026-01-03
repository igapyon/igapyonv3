# igapyonv3 プログラム一覧メモ

このファイルは、運用時に意味のあるプログラムエントリポイントを整理するためのメモ。

## メインストリームのエントリポイント一覧

前提: `settings.src.md` が以下の値で運用される想定。

- `setGeneratetodaydiary("false")`
- `setConvertmarkdown2html("false")`
- `setGeneratekeywordifneeded("true")`
- `setDuplicatefakehtmlmd("true")`

エントリポイント:

- `jp.igapyon.diary.igapyonv3.IgDiaryProcessor`
  - 設定読み込み → インデックス生成 → キーワード生成 → `.src.md` 変換の一連を実行する主入口。

この設定での主な内部処理:

- `DiaryIndexAtomGenerator` による index 用 Atom 生成。
- `KeywordAtomByTitleGenerator` によるキーワード Atom 生成。
- `KeywordMdTextGenerator` によるキーワード `.md` 生成（必要時）。
- `DiarySrcMd2MdConverter` による `.src.md` → `.md` / `.html.md` 変換。

※ `setGeneratetodaydiary("false")` のため `TodayDiaryGenerator` は実行されない。  
※ `setConvertmarkdown2html("false")` のため `IgapyonMd2Html` は実行されない。
