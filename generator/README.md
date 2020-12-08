# Generator

根据`GraphQL Schema`生成`Java`代码及`DgraphSchema`

参考 [graphql-sample](./generator/src/test/java/ai/care/arc/generator/codegen/GeneratorIntegralTest.java)

## 使用

### 1. 添加maven依赖

最新版本为 **1.4.0-SNAPSHOT**

```xml
<dependency>
    <groupId>com.github.yituhealthcare</groupId>
    <artifactId>arc-generator</artifactId>
    <version>1.4.0-SNAPSHOT</version>
</dependency>
```
#### 2. 添加graphql schema文件 

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

#### 3. 编写代码

1. 编写单测case并执行

Example

```java
public class Generator {

    @Test
    public void generate_java_code() throws IOException {
        ClassPathResource schema = new ClassPathResource("graphql/schema.graphqls");
        CodeWriter codeWriter = new CodeWriter(Paths.get(".", "/src/main/java"));
        CodeGenConfig config = new CodeGenConfig(Collections.emptyList());
        new JavaCodeGenerator(codeWriter, config).generate(schema.getInputStream(), "com.github.yituhealthcare.arc.samplegenerator");
    }


    @Test
    public void generate_dgraph_schema() throws IOException {
        List<String> sql = new DgraphSchemaGenerator().generate(new ClassPathResource("graphql/schema.graphqls").getInputStream());
        Files.write(Paths.get(".", "/src/main/resources", "dgraph", "schema.dgraph"), sql);
    }
}

```

运行后会在根据配置在指定目录生成相关业务代码