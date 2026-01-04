# igapyonv3 実行方法メモ

作業者メモとして残っていた実行手順を記録します。

## ビルドとインストール

igapyonv3 開発者のビルドとインストールは `mvn clean install` で OK。

```sh
mvn clean install
```

### ビルドとインストール実行例

```sh
MAVEN_OPTS="-Djava.net.preferIPv4Stack=true" mvn -U install
```

## igapyonv3 の使用例

※前提条件: `exec-maven-plugin` の `igdiary` 実行設定を `pom.xml` に追加済みであること。利用者の実行は `mvn clean exec:java@igdiary antrun:run` が正解。

```
# 設定や生成物の反映のため、同じコマンドを2回実行する。
mvn clean exec:java@igdiary antrun:run
mvn clean exec:java@igdiary antrun:run
```

### 公開（デプロイ）例

※GitHub Pages への出力は deprecated のため、上記の `cp -vpR` コマンドで /var/www/html に公開する例です。

```
cp -vpR /home/USERNAME/git/diary/target/md2html/* /var/www/html/igapyon/diary/
cp -vpR /home/USERNAME/git/diary/images/* /var/www/html/igapyon/diary/images/
```

## settings.src.md の例とフラグの意味

例:

```md
## Settings for igapyonv3 env

This file is settings for [[igapyonv3]].

### Result

${showSettings()}

### Setting

${setVerbose("true")}
${setDebug("false")}
${setGeneratetodaydiary("false")}
${setDuplicatefakehtmlmd("true")}
${setConvertmarkdown2html("false")}
${setGeneratekeywordifneeded("true")}
${setAuthor("Toshiki Iga")}
${setSourcebaseurl("https://github.com/igapyon/diary/blob/master")}
${setSitetitle("Igapyon Diary v3")}

<#-- homepage main site -->
${setBaseurl("https://www.igapyon.jp/igapyon/diary")}
```

主なフラグ:

- `showSettings()` は現在の設定値の出力用。
- `setVerbose("true")` は詳細ログを有効化。
- `setDebug("false")` はデバッグログを無効化。
- `setGeneratetodaydiary("false")` は今日の日記の自動生成を無効化。
- `setDuplicatefakehtmlmd("true")` は `.html.md` を複製生成（GitHub Pages 向け）だが deprecated。
- `setConvertmarkdown2html("false")` は `.md` → `.html` 変換を無効化。
- `setGeneratekeywordifneeded("true")` は不足しているキーワード `.md` を生成。
- `setAuthor(...)` は作者名。
- `setSourcebaseurl(...)` はソースの base URL。
- `setSitetitle(...)` はサイトタイトル。
- `setBaseurl(...)` は公開サイトの base URL。

### settings.src.md の配置場所と役割

- 配置場所はプロジェクトのルート（`IgDiaryProcessor` の `rootdir`）直下。
- `.src.md` に対する FreeMarker の設定ファイルとして動作し、`settings`/`current` などの変数やディレクティブ/メソッドを提供する。

### 運用時の必須項目（最低限の設定セット）

最低限の設定例:

```md
${setAuthor("Your Name")}
${setSitetitle("Your Site Title")}
${setBaseurl("https://example.com")}
${setSourcebaseurl("https://github.com/your/repo/blob/master")}
```

用途に応じて次のフラグを追加する。

- `setGeneratekeywordifneeded("true")` キーワード `.md` を必要に応じて生成。
- `setConvertmarkdown2html("true")` HTML まで生成したい場合に有効化。
- `setGeneratetodaydiary("true")` 今日の日記を自動生成したい場合に有効化。

## 運用 pom の実行フロー

運用中の `pom.xml` では、次の流れで生成処理が動く構成。

1) `igapyonv3-maven-plugin:generate` が `generate-resources` フェーズで実行  
   - `.src.md` → `.md` などの生成が走る。
2) `maven-antrun-plugin:run` で追加処理を実行  
   - `KeywordMdTextGenerator` を起動してキーワード `.md` を生成。  
   - `IgapyonMd2Html` を起動して `target/md2html` に HTML を生成。
3) 生成物を `cp` で公開先へコピー（作業メモの手順に一致）。

補足:

- JDK は `maven-compiler-plugin` で 1.8 指定。
- `exec-maven-plugin` により `mvn install exec:java` で `igapyon.diary.ghpages.App` を実行可能。

## 本リポジトリの pom.xml 概要

このリポジトリ配下の `pom.xml` の要点。

- Java 11 を指定（`maven.compiler.source/target=11`）。
- 主要依存は flexmark/freemarker/rome/tagsoup/commons-*。
- `maven-assembly-plugin` で依存込み JAR を作成し、`mainClass` は `jp.igapyon.diary.igapyonv3.IgDiaryProcessor`。
- テストは `maven-surefire-plugin`（言語/地域を en/US に固定）。
- `maven-gpg-plugin` で `verify` フェーズに署名を実行。

## バージョン更新メモ

バージョンを更新する際は、次の 2 箇所を変更する。

- `pom.xml` の `<version>`
- `src/main/java/jp/igapyon/diary/igapyonv3/md2html/IgapyonMd2HtmlConstants.java` の `VERSION`

## 出力ディレクトリの規約

- `.src.md` はソース原稿。FreeMarker 展開対象。
- `.md` は `.src.md` から生成される Markdown。
- `.html` は `.md` から生成される HTML。
- `target/html` はデフォルトの HTML 出力先（`IgapyonV3Settings` の既定値）。
- `target/md2html` は運用 pom の `maven-antrun-plugin` で使われる HTML 出力先。
