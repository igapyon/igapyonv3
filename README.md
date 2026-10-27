Igapyon Diary System v3
=======================

[Igapyon Diary System v3](https://github.com/igapyon/igapyonv3)（#igapyonv3）は、Java 製の OSS 日記システムです。
Markdown 原稿（*.md）から HTML（*.html）を生成する機能を中核に据えています。
HTML 出力は Tailwind CSS を前提とした class を付与します。
最短の利用手順は「Maven プラグインを使わずに igapyonv3 を実行する」を参照してください。

### 主要エントリポイント
- `jp.igapyon.diary.igapyonv3.IgDiaryProcessor` : 生成処理の主入口。設定読み込み、インデックス/キーワード生成、`.src.md` 変換を実行。
- `jp.igapyon.diary.igapyonv3.gendiary.TodayDiaryGenerator` : 設定で有効化時に、今日の日記テンプレートを生成。
- `jp.igapyon.diary.igapyonv3.md2html.IgapyonMd2Html` : Markdown（.md）を HTML（.html）に変換する CLI。
- `jp.igapyon.diary.igapyonv3.indexing.keyword.KeywordMdTextGenerator` : キーワード `.md` を単体で生成する CLI。
- `jp.igapyon.diary.igapyonv3.migration.html2md.IgapyonV2Html2MdConverter` : 旧 igapyonv2 の HTML を Markdown に移行する CLI。
- `jp.igapyon.diary.igapyonv3.migration.hatena2md.HatenaXml2SeparatedTextConverter` : はてなダイアリーの XML を分割 Markdown に変換する CLI。

### Java の互換性

- ビルド確認環境: JDK 17。
- 本体のclass出力: Java 11形式（class major 55）。`pom.xml`の`maven-compiler-plugin`で`release=11`を指定しています。
- 実行環境: JDK 11以上。

本体と依存するflexmark-java 0.64.8の実行要件をJava 11以上に揃えています。通常のJARと依存込みJARのどちらも、実行にはJDK 11以上が必要です。

### GitHub Releasesの配布JAR

POMのバージョンと一致する`v<version>`タグをpushすると、GitHub ActionsがJDK 17でテスト・ビルドし、次の2ファイルをGitHub Releaseへ添付します。

- 実行用JAR（依存ライブラリ込み）: `igapyonv3-<version>.jar`
- ソースJAR: `igapyonv3-sources-<version>.jar`

本体のclass出力はPOMの`release=11`に従います。詳しくは[運用手順](docs/OPERATIONS.md#github-releasesの配布jar生成)を参照してください。

### ライセンス
[Igapyon Diary System v3](https://github.com/igapyon/igapyonv3) は GNU LGPL v3 と Apache License v2 のデュアルライセンスで提供されます。  
LGPL または ASL、あるいは両方を選択できます。

### 依存関係
Igapyon Diary System v3（#igapyonv3）は以下の OSS に依存しています。

- [JUnit Jupiter](https://junit.org/junit5/)（テスト）
- [Apache Ant](http://ant.apache.org/)
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/)
- [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)
- [Apache Commons Text](https://commons.apache.org/proper/commons-text/)
- [Apache Commons Codec](https://commons.apache.org/proper/commons-codec/)
- [TagSoup](https://home.ccil.org/~cowan/XML/tagsoup/)
- [FreeMarker](https://freemarker.apache.org/)
- [ROME](https://rometools.github.io/rome/)
- [flexmark-java](https://github.com/vsch/flexmark-java)（core + tables/wikilink/gfm-strikethrough）

## Maven プラグインを使わずに igapyonv3 を実行する

日記ディレクトリに移動して以下を実行します（`exec-maven-plugin` の `igdiary` 実行設定を `pom.xml` に追加済みであることが前提）。

```
mvn clean exec:java@igdiary antrun:run
```
