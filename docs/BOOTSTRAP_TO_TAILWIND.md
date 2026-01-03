# Bootstrap から Tailwind への移行計画

この計画は、既存の HTML 構造と出力挙動を維持しつつ、
Bootstrap ベースのスタイリングを Tailwind ベースに置き換える。

## 目的

- 出力 HTML の構造とジェネレータ挙動を変更しない。
- Bootstrap の class を Tailwind の class に置換する。
- 生成物から Bootstrap 依存を取り除く。

## 対象外

- テンプレートの構造変更やレイアウト再設計。
- コンテンツ階層やセマンティクスの再設計。
- Markdown から HTML への変換ロジック変更。

## 対象範囲

- HTML 生成に使うテンプレートおよびレイアウト。
- 生成側が付与する class（見出し、テーブルなど）。
- 出力物に含まれる静的アセットと CSS 参照。

## インベントリ（要記入）

- テンプレートファイル:
  - 生成されるテンプレート: `template-header.md` / `template-footer.md`
  - 既定値の定義: `src/main/java/jp/igapyon/diary/igapyonv3/IgDiaryConstants.java`
- ジェネレータ側の class 付与:
  - HTML 骨格/Bootstrap CDN（`container-fluid`/`jumbotron` など）: `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonV3Util.java`
  - タグへの class 付与（見出し/テーブル）: `src/main/java/jp/igapyon/diary/igapyonv3/md2html/tagconf/IgapyonMdTagConf.java`
- 静的アセット:
  - ローカル Bootstrap: `lib.js/bootstrap/3.3.5/`
  - ローカル jQuery: `lib.js/jquery/1.11.3/`
- ビルド/パッケージ参照:
  - Bootstrap/JQuery の参照設定: `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonMd2HtmlSettings.java`

## 作業項目

1) HTML 出力に使われるテンプレート/レイアウトを特定する。
2) テンプレートおよび生成物に含まれる Bootstrap class を列挙する。
3) Bootstrap 使用箇所ごとに Tailwind class への置換方針を決める。
4) テンプレートと class 付与処理を更新する。
5) 出力物から Bootstrap のアセット/参照を削除する。
6) 既存構造に合わせた Tailwind のビルド/出力フローを用意する。
7) 出力 HTML と見た目の差分を確認する。

## 受け入れ条件

- 出力 HTML の構造は維持される（差分は class レベルのみ）。
- Bootstrap の CSS/JS 参照が削除されている。
- Tailwind CSS が適切に読み込まれている。
- jQuery の参照が削除されている（Bootstrap JS 撤去に伴う）。
- 見た目が許容範囲内に収まっている。

## リスク

- Bootstrap のコンポーネント class が Tailwind に 1:1 対応しない。
- Bootstrap が提供していたグローバルスタイル（例: Typography）が不足する。
- Bootstrap 変数に依存したテンプレート固有の上書きがある。

## 確認チェックリスト

- `mvn -U package` が成功する。
- 生成 HTML に欠落した CSS がない。
- テーブル/見出し/コードブロック/リストが正しく表示される。
- Home/index ページが従来通りの構造で表示される。

## 未決事項

- テンプレートファイルの配置場所はどこか。
- 現在の CSS はどう参照/同梱されているか。
- Tailwind の取り込み方式は何を採用するか（事前ビルド vs ビルド工程追加）。
