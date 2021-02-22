# Generator

根据`GraphQL Schema`生成`Java`代码及`DgraphSchema`

参考 [graphql-sample](./generator/src/test/java/ai/care/arc/generator/codegen/GeneratorIntegralTest.java)

## 使用

### 1. 添加maven依赖

最新版本为 **1.6.0-SNAPSHOT**

```xml
<dependency>
    <groupId>com.github.yituhealthcare</groupId>
    <artifactId>arc-generator</artifactId>
    <version>1.6.0-SNAPSHOT</version>
</dependency>
```
### 2. 添加graphql schema文件 

新建文件 `test/resources/graphql/schema.graphqls`

Example

```graphql
scalar DateTime

schema{
    query: Query,
    mutation: Mutation
}

type Query{
    project(
        id: String
    ): Project
}

type Mutation{
    createProject(
        payload: ProjectInput
    ): Project
}
type Project{
    id: String!
    name: String!
    description: String!
    createTime: DateTime!
    milestone(
        id: String
    ): Milestone
    milestones: [Milestone]
}

type Milestone{
    id: String!
    name: String!
    description: String!
    version: String!
    createTime: DateTime!
    endTime: DateTime
}

input ProjectInput{
    name: String!
    description: String!
    dsl: String!
    vendorBranches: [String!]!
}
```

### 3. 新建配置文件

新建配置文件， 默认路径为 `resources:arc-generator.json` 可以通过plugin配置修改

```json
{
  "basePackage": "com.github.yituhealthcare.arcgeneratorsample",
  "dropAll": false,
  "genStrategies": [
    {
      "codeGenOperation": "SKIP",
      "codeGenType": "REPO"
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

### 4. 执行maven命令

```shell script
mvn arc:generate
```

可以通过参数`-Dtarget`决定生成**java** or **dgraph schema** 如

```shell script
mvn arc:generate -Dtarget=java
```

运行后会在根据配置在指定目录生成相关业务代码

## 其他

### 1. 内置directive

- directive @Action on FIELD_DEFINITION 区分field是字段还是方法
