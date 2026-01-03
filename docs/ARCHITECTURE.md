# igapyonv3 内部構造メモ

このファイルは、igapyonv3 の内部構造を把握した内容を簡潔に記録するためのメモです。
必要に応じて追記していきます。

## 概要

- igapyonv3 は Java 製の静的サイト/ブログ生成ツール。
- 主な処理は `.src.md` を起点とした変換パイプラインと、インデックス/キーワード/RSS の生成。

## 主な処理フロー

エントリポイントは `IgDiaryProcessor`。

1) `settings.src.md` の読み込みと展開
2) 今日の日記の自動生成（設定で有効化時）
3) `keyword` / `memo` ディレクトリの作成
4) インデックス用 Atom の生成
5) キーワード用 Atom の生成
6) 必要に応じてキーワード `.md` の生成
7) `.src.md` → `.md` / `.html.md` 変換
8) Markdown → HTML 変換（設定で有効化時）

## 主要クラス

### エントリポイント

- `src/main/java/jp/igapyon/diary/igapyonv3/IgDiaryProcessor.java`
  - 全体処理の起点。
  - 設定の読み込み、日記生成、インデックス生成、変換処理を順に実行。

### `.src.md` → `.md` 変換

- `src/main/java/jp/igapyon/diary/igapyonv3/mdconv/DiarySrcMd2MdConverter.java`
  - FreeMarker 展開後のテキストを行単位で整形し、`.md` を生成。
  - 設定により `.html.md` を重複生成（gh-pages 用）。
- `src/main/java/jp/igapyon/diary/igapyonv3/mdconv/freemarker/IgapyonV3FreeMarkerUtil.java`
  - FreeMarker の実行基盤。
  - Pre-parse で `current` 情報（タイトル/URL/キーワード）を生成。

### Markdown → HTML

- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonMd2Html.java`
  - flexmark で Markdown を解析し、HTML を出力。
  - 最初の見出しをタイトル、次の見出しまでを description として扱う。

### 設定

- `src/main/java/jp/igapyon/diary/igapyonv3/util/IgapyonV3Settings.java`
  - ルートディレクトリ、出力ディレクトリ、URL などの基本設定。
  - `generateTodayDiary` / `convertMarkdown2Html` などのフラグを保持。
  - キーワードの初期値や外部 Atom 取り込みもここに集約。

## FreeMarker 拡張

FreeMarker のディレクティブとメソッドが多数用意されている。

- ディレクティブ: `src/main/java/jp/igapyon/diary/igapyonv3/mdconv/freemarker/directive/`
- メソッド: `src/main/java/jp/igapyon/diary/igapyonv3/mdconv/freemarker/method/`

`settings.src.md` の中でこれらが呼び出され、サイト設定やリンク生成に反映される。

## 補助機能

- 日記生成: `src/main/java/jp/igapyon/diary/igapyonv3/gendiary/`
- インデックス/キーワード生成: `src/main/java/jp/igapyon/diary/igapyonv3/indexing/`
- 移行ツール: `src/main/java/jp/igapyon/diary/igapyonv3/migration/`
