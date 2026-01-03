# igapyonv3 実行方法メモ

作業者メモとして残っていた実行手順を記録します。

## ビルドとデプロイ手順

```sh
mvn clean install antrun:run
mvn clean install antrun:run
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
- `setDuplicatefakehtmlmd("true")` は `.html.md` を複製生成（gh-pages 用）。
- `setConvertmarkdown2html("false")` は `.md` → `.html` 変換を無効化。
- `setGeneratekeywordifneeded("true")` は不足しているキーワード `.md` を生成。
- `setAuthor(...)` は作者名。
- `setSourcebaseurl(...)` はソースの base URL。
- `setSitetitle(...)` はサイトタイトル。
- `setBaseurl(...)` は公開サイトの base URL。
