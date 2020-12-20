igapyonv3
=======================

[igapyonv3](https://github.com/igapyon/igapyonv3) is an open source static site/blog generator for developers written in Java.

* title: igapyonv3
  * repo: https://github.com/igapyon/igapyonv3
  * homepage: https://igapyon.github.io/igapyonv3/
  * language: Java
  * license: Apache 2 / LGPL
  * templates: Freemarker
  * description: A static site/blog generator for developers

* characteristics
  * Lightweight and turnkey static site/blog generator.
  * Provided as a Maven Plugin.
  * Supports Markdown content.
  * RSS feed support.
  * Freemarker and igapyonv3's additional diary directive support.
  * Supports github gh-pages.
  * Also support to convert markdown into Bootstrap based HTML.

## Install

### Maven is required

igapyonv3 requires Maven.
Java is required too.

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

That is all to install igayponv3!

## Setup

### Init igapyonv3 directory

You can initialize directory using command line like below:

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:init
```

Default directory will be set on your current directory.

### Edit igapyonv3 setting file.

Modify `settings.src.md` to fit your favor to adjust diary settings. Linux / Mac will be like below:

```sh
vi settings.src.md
```

Notice: If you want to publish your contents at `gh-pages`, change settings.src.md to be like `${setDuplicatefakehtmlmd("true")}`.

### Force update template files.

Update template files to apply `settings.src.md` changes, using command line below:

```sh
mvn jp.igapyon.diary.igapyonv3.plugin:igapyonv3-maven-plugin:inittemplate
```

Notice: Be case that the command will overwrite template files if you may changed them.

## Running igapyonv3

To generate/update markdown files and html files, use command line like below:

```sh
mvn compile
```

If you modify XXX.src.md and you want to update, run `mvn compile` again, and you will be able to get updated `md` and `html` files and many index files.

## License

[igapyonv3](https://github.com/igapyon/igapyonv3) is released under GNU LGPL version 3 and Apache License version 2 (dual license). 
You can select either LGPL or ASL or both. 
