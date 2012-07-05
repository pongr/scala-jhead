# scala-jhead

A minimal scala wrapper around [jhead](http://www.sentex.net/~mwandel/jhead/).

## Install

### sbt 
```scala
"com.pongr" %% "scala-jhead" % "0.1"
```

### Maven

```xml
<dependency>
    <groupId>com.pongr</groupId>
    <artifactId>scala-jhead_2.9.1</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```
## Usage

```scala
val jhead = JHead(imageBytes)
val imageInfo = jhead.info._1
val errorMessages = jhead.info._2
jhead.autorot
jhead.purejpg
...
```

## License

scala-jhead is licensed under the [Apache 2 License](http://www.apache.org/licenses/LICENSE-2.0.txt).
