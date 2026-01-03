# pegdown から flexmark への移行メモ

このファイルは、pegdown 依存を flexmark に置き換える作業に特化したメモです。

## 目的

- pegdown 依存の除去。
- Markdown から HTML 変換の維持。
- 既存の出力を大きく変えないことを優先。

## 対象範囲

- `pom.xml` の `org.pegdown:pegdown` 依存。
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/` の pegdown 前提コード。
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/pegdownext/` 一式。
- `IgapyonMd2Html` の変換ロジック。

## 作業の流れ（案）

1) flexmark の最新版（または保守対象版）へ依存を更新。
2) pegdown 系クラスの置き換え方針を決定。
3) `IgapyonMd2Html` の変換ロジックを flexmark ベースで再実装。
4) HTML 出力の差分を確認。

## pegdown 互換依存の扱い

現在は `flexmark-profile-pegdown` に依存しており、`FlexmarkPegdownOpts` で pegdown 互換のオプションを利用している。
最終的には、通常の flexmark 拡張構成へ移行し、`flexmark-profile-pegdown` を不要にする。

対応方針（案）:

1) `FlexmarkPegdownOpts` を通常の flexmark 拡張設定に置き換える。
2) `flexmark-profile-pegdown` 依存を削除する。
3) HTML 出力差分を確認し、必要なら調整する。

受け入れ条件:

- `mvn -U package` が成功する。
- `flexmark-profile-pegdown` 依存が不要になる。
- HTML 出力が実運用で許容できる範囲に収まる。

## 置き換え候補

- `IgapyonPegDownProcessor` 相当のパーサ処理を flexmark に置換。
- 見出し抽出（タイトル/description）の処理を flexmark API で再実装。
- `IgapyonPegDownTagConf` / `IgapyonLinkRenderer` 相当の処理を flexmark の拡張で対応。

## 互換性チェック項目

- 見出し抽出ロジック（最初の見出し/次の見出しの扱い）。
- リンク生成や属性付与の挙動。
- 出力 HTML のタグ構造差分。

## 参考

- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonMd2Html.java`
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/pegdownext/`
