Igapyon Diary System v3
=======================

[igapyonv3](https://github.com/igapyon/igapyonv3) (#igapyonv3) is a Java based open source static site/blog generator for developers written in Java.

---
title: igapyonv3
repo: https://github.com/igapyon/igapyonv3
homepage: https://igapyon.github.io/igapyonv3/
language: Java
license: Apache 2
templates: Freemarker
description: A static site/blog generator for developers
---

* Lightweight and turnkey static site/blog generator.
* Provided as a Maven Plugin.
* Supports Markdown content.
* RSS feed support.
* Freemarker and igapyonv3's additional diary directive support.
* Convert markdown into Bootstrap based HTML.

## Install

### Maven is required

igapyonv3 requires maven.

### Edit pom.xml

Edit pom.xml to enable igapyonv3 like below:

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

## Setup

### Init igapyonv3 directory

You can init directory using command line below:

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:init
```

Default directory will be your current directory.

### Edit igapyonv3 setting file.

Modify `settings.src.md` to adjust diary that to fit to your favor.

```sh
vi settings.src.md
```

If you want to publish output of igapyonv3, setting `${setDuplicatefakehtmlmd("true")}` in settings.src.md is strongly recommended.

### Force update template files.

Update template files to apply `settings.src.md` changes, using command line below:

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:inittemplate
```

## Running igapyonv3

You can generate compiled markdown files and html files, using command line below:

```sh
mvn compile
```

Modify XXX.src.md and run `mvn compile` again, and you will be able to get generated `md` and `html` files and many index files.

## License

[Igapyon Diary System v3](https://github.com/igapyon/igapyonv3) is released under GNU LGPL version 3 and Apache License version 2 (dual license). 
You can select either LGPL or ASL or both. 
