# Arc Maven Plugin

Maven 插件, 方便使用Arc. 目前支持的功能

- Generator 代码生成

## 使用

### 添加maven插件

```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.github.wangyuheng</groupId>
            <artifactId>arc-maven-plugin</artifactId>
            <version>1.6.0</version>
        </plugin>
    </plugins>
</build>
```

## Generator 

生成java code + dgraph

```shell script
mvn arc:generate
```
或者
```shell script
mvn arc:generate -Dtarget=all
```

只生成java code

```shell script
mvn arc:generate -Dtarget=java
```

只生成dgraph schema 

```shell script
mvn arc:generate -Dtarget=dgraph
```

### Generator配置文件

默认路径 classpath: arc-generator.json 可以在 maven plugin 中指定

```xml
<plugin>
    <groupId>io.github.wangyuheng</groupId>
    <artifactId>arc-maven-plugin</artifactId>
    <version>1.6.0</version>
    <configuration>
        <configJson>${basedir}/src/main/resources/arc-generator.json</configJson>
    </configuration>
</plugin>
```

参考配置

```json
{
  "basePackage": "io.github.wangyuheng.arcgeneratorsample",
  "dropAll": false,
  "genStrategies": [
    {
      "codeGenOperation": "OVERRIDE",
      "codeGenType": "TYPE"
    },
    {
      "codeGenOperation": "OVERRIDE",
      "codeGenType": "API"
    }
  ],
  "ignoreJavaFileNames": [
    "User"
  ],
  "dgraphPath": "dgraph/schema.dgraph"
}
```