# igapyonv3 プログラム一覧メモ

このファイルは、運用時に意味のあるプログラムエントリポイントを整理するためのメモ。

## IgDiaryProcessor から呼ばれる機能

ここではまず、メインとなるクラスである `IgDiaryProcessor` から呼ばれる機能を 2 つに分類する。

- メインストリーム: `settings.src.md` の設定に関わらず必ず通る処理。
- サブメインストリーム: `settings.src.md` の設定変更で実行される/されない処理。

### メインストリームのエントリポイント一覧

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

### サブメインストリームのエントリポイント一覧

`settings.src.md` の設定値を切り替えることで、同じ `IgDiaryProcessor` 内で追加実行される入口。

- `jp.igapyon.diary.igapyonv3.gendiary.TodayDiaryGenerator`
  - `setGeneratetodaydiary("true")` のとき実行される。
  - 今日の日付の空の日記ファイルを自動生成。

- `jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html`
  - `setConvertmarkdown2html("true")` のとき実行される。
  - `.md` → `.html` 変換を実施する。
  - pom.xmlから直接呼び出すので、`settings.src.md` の設定により実行を抑止する。

## IgDiaryProcessor 以外から呼ばれる機能

運用 pom などから直接起動され、`IgDiaryProcessor` を経由しない入口。

- `jp.igapyon.diary.igapyonv3.indexing.keyword.KeywordMdTextGenerator`
  - `maven-antrun-plugin` から直接実行される。
  - キーワード `.md` の生成を単体で行う。

- `jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html`
  - `maven-antrun-plugin` から直接実行される。
  - `.md` → `.html` 変換を単体で行う。

## 移行ツール

移行用途で使うエントリポイント。

- `jp.igapyon.diary.igapyonv3.migration.html2md.IgapyonV2Html2MdConverter`
  - 旧版である いがぴょんv2 の HTML から、Markdown 形式へ変換する CLI。
  - 旧版から新版への移行時にのみ利用。

- `jp.igapyon.diary.igapyonv3.migration.hatena2md.HatenaXml2SeparatedTextConverter`
  - はてなダイアリーのエクスポート XML を入力に分割テキストに変換する CLI。
  - はてなダイアリーから新版への移行時にのみ利用。

## その他

- `jp.igapyon.diary.igapyonv3.util.SimpleTagSoupUtil`
  - TagSoup を使った HTML 正規化のサンプル兼ユーティリティ。
  - 破綻した HTML ファイルを妥当な形式に変換。
