IgapyonMd2Html
==============

[IgapyonMd2Html](https://github.com/igapyon/igapyonv3/blob/master/IgapyonMd2Html.md) is a command line utility which convert Markdown text (*.md) to Html text with Bootstrap (*.html). 

## Basic info
- Homepage: https://github.com/igapyon/igapyonv3/blob/master/IgapyonMd2Html.md

### Simple sample
IgapyonMd2Html convert like below:

Convert|Image
-------|------
Input  |![IgapyonMd2Html-input.png](https://raw.githubusercontent.com/igapyon/igapyonv3/master/doc/image/IgapyonMd2Html-input.png)
Output |![IgapyonMd2Html-output.png](https://raw.githubusercontent.com/igapyon/igapyonv3/master/doc/image/IgapyonMd2Html-output.png)

### Parts 
- Hashtag: #‎igapyonv3‬
- Data format: ‪#‎Markdown
- Web design: ‪#‎Bootstrap‬
- Written in #‎Java

### Depends
IgapyonMd2Html depends on several great OSSs. Great thanks to great OSSs.

- [pegdown](https://github.com/sirthias/pegdown)
- [parboiled](https://github.com/sirthias/parboiled)
- [ASM](http://asm.ow2.org/)
- [Bootstrap](http://getbootstrap.com/)
- [jQuery](https://jquery.com/)
- [Apache Ant](http://ant.apache.org/)
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Apache Commons IO](https://commons.apache.org/proper/commons-io/)

## Project info
### Main project
- [igapyonv3](https://github.com/igapyon/igapyonv3/blob/master/README.md)

### Similar project
- [Apache Doxia](https://maven.apache.org/doxia/) : Doxia is full featured conveter. IgapyonMd2Html is focused in for Dairy.

## Usage
### Java usage

```Java
    new IgapyonMd2Html().processDir("./", "./", false);
```

### ant usage
Ant usage is below:

```xml
	<target name="md2html">
		<echo>run project</echo>
		<taskdef name="md2html" classname="jp.igapyon.diary.v3.md2html.task.IgapyonMd2HtmlTask">
			<classpath>
				<fileset dir="./">
					<include name="igapyonv3-*.jar" />
				</fileset>
				<fileset dir="./lib/pegdown">
					<include name="*.jar" />
				</fileset>
				<fileset dir="./lib/ant">
					<include name="*.jar" />
				</fileset>
			</classpath>
		</taskdef>
		<md2html source="./" target="./" />
		<md2html source="./test/data/src" target="./test/data/output" recursivedir="true" />
	</target>
```

### Refs
- [Bootstrap Components](http://getbootstrap.com/components/)


### TODO
- Up to website to display what this product enable.
- img tag for Face.
- consider keyword impl.
- Return to index link.
- adv area.
- is date area is from filename?
- tail format for About igapyon.
- tail format for last modified (or other impl like metaarea)
- Next and Prev like igapyonv2. based on idxdiary?
- Consider CDN off mode. for offline.
- Supporting without CDN needs to analyze deapth of dir.

### Trash memo
- using glyphicon as h* is not so good. 

# META-INF
`META-INF` is reserved word in IgapyonMd2Html on \<h1\>, and is used in \<meta keywords\> and will be discard in html.

## Date
First one is used in html.

- 2015-07-24: Small change.
- 2015-07-23: Newly created.

## Author
- Toshiki Iga

## Keywords
Keywords list description. You can use , or - or both.

- Igapyon, diary
- Bootstrap, Markdown

## Todo
- Consider new word to describe md's relative location.
