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
description: A static site/blog generator for developers & designers
---

* Lightweight and turnkey static site/blog generator.
* Provided as a Maven Plugin.
* Supports Markdown content.
* RSS feed support.
* Freemarker and igapyonv3's additional diary directive support.
* Convert markdown into Bootstrap based HTML.

## Install

### Maven is required

igapyonv3 require maven.

### Edit pom.xml

Edit pom.xml to enabele igapyonv3.

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

### Init igapyonv3 settings

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:init
```

```sh
vi settings.src.md 
```

I strongly recommnd that modify settings.src.md to be like `${setDuplicatefakehtmlmd("true")}` .

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:inittemplate
```

### Running igapyonv3

You can generate markdown and html using command line below:

```sh
mvn compile
```

Modify XXX.src.md and run `mvn compile` again, and you will be able to get generated `md` and `html` files and many index files.

## License

[Igapyon Diary System v3](https://github.com/igapyon/igapyonv3) is released under GNU LGPL version 3 and Apache License version 2 (dual license). 
You can select either LGPL or ASL or both. 
