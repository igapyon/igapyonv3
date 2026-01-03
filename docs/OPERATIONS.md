# igapyonv3 実行方法メモ

作業者メモとして残っていた実行手順を記録します。

## ビルドとデプロイ手順

```sh
mvn clean install antrun:run
mvn clean install antrun:run
cp -vpR /home/USERNAME/git/diary/target/md2html/* /var/www/html/igapyon/diary/
cp -vpR /home/USERNAME/git/diary/images/* /var/www/html/igapyon/diary/images/
```
