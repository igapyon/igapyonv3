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
  - ローカル Tailwind（ビルド生成）: `lib.css/tailwind.css`
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

## 机上確認から導出した変換ルール（README.md → README-tw.html）

### HTML 骨格/ヘッダ

- `<link rel="stylesheet" ...bootstrap...>` を削除し、`<link rel="stylesheet" href="https://igapyon.jp/lib.css/tailwind.css">` を追加する。
- `<body>` に `class="text-slate-900 leading-7"` を付与する。
- Bootstrap JS と jQuery の `<script>` を削除する。
  - NOTE: CSS パスは現状ハードコード。将来的に設定化する余地あり。

### Tailwind CSS の生成（原因切り分け用）

- 入力: `tailwind-input.css`
- 設定: `tailwind.config.js`
- 生成例: `npx tailwindcss@3.4.15 -i ./tailwind-input.css -o lib.css/tailwind.css --minify`

### レイアウト

- `container-fluid` → `mx-auto w-full px-4 sm:px-6 lg:px-8`
- `jumbotron` → `my-8 rounded-lg bg-slate-100 p-6 sm:p-8`

### 見出し

- `h1.alert.alert-danger` → `mb-4 rounded-md bg-red-100 px-4 py-2 text-2xl font-bold text-red-900`
- `h2.alert.alert-warning` → `mt-8 mb-3 rounded-md bg-amber-100 px-3 py-2 text-xl font-semibold text-amber-900`
- `h3.bg-success` → `mt-6 mb-2 rounded-md bg-emerald-100 px-3 py-1.5 text-lg font-semibold text-emerald-900`
- `h4.bg-info` → `mt-5 mb-2 rounded-md bg-sky-100 px-3 py-1.5 text-base font-semibold text-sky-900`
- 追加補正: `h1` に `text-3xl font-bold tracking-tight` を付与する。

### 本文/リスト/コード/テーブル

- `p` → `mt-2`
- `ul` → `list-disc pl-6`
- `ol` → `list-decimal pl-6`
- `pre` → `my-4 overflow-auto rounded bg-slate-900 p-4 text-slate-100 text-sm`
- `table.table.table-bordered` → `w-full border border-slate-300 text-left text-sm`
- `th` → `border border-slate-300 bg-slate-50 px-3 py-2 font-semibold`
- `td` → `border border-slate-300 px-3 py-2`
- 特例強調: inline `code` に必要なら `rounded bg-red-50 px-1.5 py-0.5 font-semibold text-red-800`

### リンク（下線なし）

- `<a>` に `text-slate-700 font-medium hover:text-sky-800 hover:bg-sky-50 rounded px-0.5 -mx-0.5`

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
