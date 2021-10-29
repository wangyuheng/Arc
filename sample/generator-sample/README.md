# Generator Sample

## 使用

### 1. 添加maven插件

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

### 2. 按需修改配置文件

classpath: arc-generator.json

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
  ]
}
```

### 3. 执行maven指令

```shell script
mvn arc:generate
```