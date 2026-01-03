# pegdown から flexmark への移行メモ

このファイルは、pegdown 依存を flexmark に置き換える作業に特化したメモです。

## 状態

- 移行は完了済み（Issue #39 close）。
- pegdown 依存と名称は削除済み。関連クラスは flexmark ベースの名称へ整理済み。

## 目的

- pegdown 依存の除去。
- Markdown から HTML 変換の維持。
- 既存の出力を大きく変えないことを優先。

## 対象範囲（当時の対象）

- `pom.xml` の `org.pegdown:pegdown` 依存。
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/` の pegdown 前提コード。
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/tagconf/` の前身（旧 pegdownext）。
- `IgapyonMd2Html` の変換ロジック。

## 作業の流れ（実施済み）

1) flexmark の保守対象版へ依存を更新。
2) pegdown 系クラスを flexmark ベースへ置き換え。
3) `IgapyonMd2Html` の変換ロジックを flexmark ベースで再実装。
4) HTML 出力の差分を確認。

## pegdown 互換依存の扱い（実施済み）

`flexmark-profile-pegdown` 依存は削除済みで、通常の flexmark 拡張構成へ移行済み。
オプション定義は `FlexmarkOptions` で管理する。

具体化案:

- `pom.xml` から `flexmark-profile-pegdown` を削除し、必要な拡張を個別に追加する。
  - テーブル: `com.vladsch.flexmark:flexmark-ext-tables`
  - WikiLink: `com.vladsch.flexmark:flexmark-ext-wikilink`
  - 取り消し線: `com.vladsch.flexmark:flexmark-ext-gfm-strikethrough`（必要なら `flexmark-ext-strikethrough` を選択）
- `FlexmarkOptions` を「通常の flexmark オプション定義」として利用する。
  - `MutableDataSet` と拡張リストで `Parser` / `HtmlRenderer` を構築。
  - 既存の `Extensions.*` で有効化していた機能（STRIKETHROUGH / FENCED_CODE_BLOCKS / TABLES / WIKILINKS）を、flexmark 拡張で再現する。
- `FlexmarkUtil` が参照する `FlexmarkOptions` の差し替えと、必要な import の更新。

例（置き換えイメージ）:

```java
MutableDataSet options = new MutableDataSet();
options.set(Parser.EXTENSIONS, Arrays.asList(
    TablesExtension.create(),
    WikiLinkExtension.create(),
    StrikethroughExtension.create()
));
Parser parser = Parser.builder(options).build();
HtmlRenderer renderer = HtmlRenderer.builder(options).build();
```

対応方針（実施済み）:

1) `FlexmarkOptions` を通常の flexmark 拡張設定として利用する。
2) `flexmark-profile-pegdown` 依存を削除する。
3) HTML 出力差分を確認し、必要なら調整する。

受け入れ条件（確認項目）:

- `mvn -U package` が成功する。
- `flexmark-profile-pegdown` 依存が不要である。
- HTML 出力が実運用で許容できる範囲に収まる。

## 置き換え内容（実施済み）

- pegdown 相当のパーサ処理を flexmark に置換。
- 見出し抽出（タイトル/description）の処理を flexmark API で再実装。
- `IgapyonMdTagConf` / `IgapyonLinkRenderer` 相当の処理を flexmark の拡張で対応。

## 互換性チェック項目

- 見出し抽出ロジック（最初の見出し/次の見出しの扱い）。
- リンク生成や属性付与の挙動。
- 出力 HTML のタグ構造差分。

## 参考

- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonMd2Html.java`
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/tagconf/`
