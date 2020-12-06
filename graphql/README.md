# Graphql

为Spring项目提供Graphql能力.

示例项目可以参考 [graphql-sample](./sample/graphql-sample)

## 使用

### 1. 添加maven依赖

最新版本为 **1.3.0**

```xml
<dependency>
    <groupId>com.github.yituhealthcare</groupId>
    <artifactId>arc-graphql</artifactId>
    <version>1.3.0</version>
</dependency>
```
#### 2. 添加graphql schema文件 

默认路径为 `resources/graphql/schema.graphqls` , 可通过配置文件`arc.graphql.define`修改路径

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

1. class bean 需要声明 `@Graphql` 
2. 相关方法需要根据类型声明`@GraphqlMutation`或者`@GraphqlQuery`. 如果是自定义type, 可以使用`GraphqlMethod`

Example

```java
@Graphql
public class ProjectDataFetcher {

    @Autowired
    private Dao dao;

    @GraphqlMethod(type = "Project")
    public DataFetcher<Milestone> milestone(){
        return dataFetchingEnvironment -> {
            Project source = dataFetchingEnvironment.getSource();
            String id = dataFetchingEnvironment.getArgument("id");
            return dao.getMilestoneByProjectIdAndId(source.getId(), id);
        };
    }

    @GraphqlQuery
    public DataFetcher<Project> project() {
        return dataFetchingEnvironment -> {
            String id = dataFetchingEnvironment.getArgument("id");
            return dao.getProjectById(id);
        };
    }

    @GraphqlMutation
    public DataFetcher<Project> createProject() {
        return dataFetchingEnvironment -> {
            log.info(JSON.toJSONString(dataFetchingEnvironment.getArguments()));
            Project project = GraphqlPayloadUtil.resolveArguments(dataFetchingEnvironment.getArguments(), Project.class);
            return dao.saveProject(project);
        };
    }
}
```

#### 4. 启动容器通过http访问

1. 浏览器访问 http://localhost:port/voyager 查看依赖关系
2. 浏览器访问 http://localhost:port/playground 查看内置IDE
3. 应用通过 POST http://localhost:port/schema 查看graphql schema, 可通过配置文件`arc.graphql.path.schema`修改路径
4. 应用通过 POST http://localhost:port/graphql 访问graphql接口, 可通过配置文件`arc.graphql.path`修改路径

![voyager](./doc/voyager.jpeg)

![playground](./doc/playground.jpeg)
