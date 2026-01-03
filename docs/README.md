igapyonv3
=======================

[igapyonv3](https://github.com/igapyon/igapyonv3) は、Java で書かれた開発者向けのオープンソース静的サイト/ブログジェネレータです。

* タイトル: igapyonv3
  * リポジトリ: https://github.com/igapyon/igapyonv3
  * ホームページ: https://igapyon.github.io/igapyonv3/
  * 言語: Java
  * ライセンス: Apache 2 / LGPL
  * テンプレート: Freemarker
  * 説明: 開発者向けの静的サイト/ブログジェネレータ

* 特徴
  * 軽量で手軽に使える静的サイト/ブログジェネレータ。
  * Maven プラグインとして提供。
  * Markdown コンテンツをサポート。
  * RSS フィードをサポート。
  * Freemarker と igapyonv3 独自の日記ディレクティブをサポート。
  * Markdown から Bootstrap ベースの HTML へ変換も可能。
  * GitHub Pages への出力は deprecated。

## Docs

- `docs/ARCHITECTURE.md` 内部構造のメモ。
- `docs/OPERATIONS.md` ビルド/デプロイのメモと設定例。
- `docs/PROGRAMS.md` エントリポイント一覧と分類。

## インストール

### Maven が必要

igapyonv3 には Maven が必要です。
Java も必要です。

### pom.xml を編集

以下のように pom.xml を編集して igapyonv3 を有効化します:

```xml
 <build>
    <plugins>
      <plugin>
        <groupId>jp.igapyon.diary.igapyonv3.plugin</groupId>
        <artifactId>igapyonv3-maven-plugin</artifactId>
        <version>1.2.3</version>
        <configuration>
          <basedir>${project.basedir}</basedir>
        </configuration>
        <executions>
          <execution>
            <phase>generate-resources</phase>
            <goals>
              <goal>generate</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
```

以上で igapyonv3 のインストールは完了です。

## セットアップ

### igapyonv3 設定ファイルの編集

`settings.src.md` を編集して日記の設定を調整します。Linux / Mac の例:

```sh
vi settings.src.md
```

注意: GitHub Pages への出力は deprecated のため、`setDuplicatefakehtmlmd` を使った運用は非推奨です。

### テンプレートファイルの強制更新

`settings.src.md` の変更をテンプレートに反映するには、次のコマンドを使います:

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:inittemplate
```

注意: このコマンドはテンプレートファイルを上書きするので、手元で変更している場合は注意してください。

## igapyonv3 の実行

Markdown と HTML を生成/更新するには、次のコマンドを実行します:

```sh
mvn compile
```

XXX.src.md を更新した場合は、`mvn compile` を再実行すると、更新された `md` / `html` と各種インデックスファイルが生成されます。

## ライセンス

[igapyonv3](https://github.com/igapyon/igapyonv3) は GNU LGPL v3 と Apache License v2 のデュアルライセンスで提供されます。 
LGPL または ASL、あるいは両方を選択できます。 
