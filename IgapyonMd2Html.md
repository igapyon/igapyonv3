IgapyonMd2Html
==============

IgapyonMd2Html is command line utility which convert Markdown text (*.md) to Html text with Bootstrap (*.html). 

## Parts 
- Hashtag: #‎igapyonv3‬
- Data format: ‪#‎Markdown
- Web design: ‪#‎Bootstrap‬
- Written in #‎Java

## Depends
IgapyonMd2Html depends on several great OSSs. Great thanks to great OSSs.
- [pegdown](https://github.com/sirthias/pegdown)
- [parboiled](https://github.com/sirthias/parboiled)
- [ASM](http://asm.ow2.org/)
- [Bootstrap](http://getbootstrap.com/)

## Main project
- [igapyonv3](https://github.com/igapyon/igapyonv3/blob/master/README.md)

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
```xml

## Refs
- [Bootstrap Components](http://getbootstrap.com/components/)

## Similar project
- [Apache Doxia](https://maven.apache.org/doxia/) : Doxia is full featured conveter. IgapyonMd2Html is focused in for Dairy.

## TODO
- Consider CDN off mode. for offline.

## Trash memo
- using glyphicon as h* is not so good. 
