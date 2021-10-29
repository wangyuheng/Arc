# Graphql Client

提供便捷调用Graphql Server Api 的能力

通过 `interface`+`annotation` 的方式注册 **Spring Bean**，在 `resource` 目录下通过 **graphql** 文件维护查询语句

## 使用

### 1. 添加maven依赖

最新版本为 **1.6.0**

```xml
<dependency>
    <groupId>io.github.wangyuheng</groupId>
    <artifactId>arc-graphql-client</artifactId>
    <version>1.6.0</version>
</dependency>
```
#### 2. 在resources目录下添加graphql 查询语句 

Example

新建文件 `resources/ql/echo.graphql`

```graphql
query hello($echoText: String!) {
    echo(text:$echoText)
}
```

#### 3. 编写代码 

1. 通过`@EnableGraphqlClients`启用并指定扫描包
2. `GraphqlClient`指定api url 并注册SpringBean
3. `GraphqlMapping`关联resource目录下的查询语句

Example

```java
@EnableGraphqlClients(basePackages = "io.github.wangyuheng.arcgraphqlclientsample")
@RestController
@SpringBootApplication
public class ArcGraphqlClientSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArcGraphqlClientSampleApplication.class, args);
    }

    @Autowired
    private GitlabGraphqlClient gitlabGraphqlClient;

    @RequestMapping("rest")
    public Object rest(@RequestParam(required = false, defaultValue = "Arc") String name) {
        return gitlabGraphqlClient.echo("Hello " + name).getData();
    }

    @GraphqlClient(value = "gitlabGraphqlClient", url = "https://gitlab.com/api/graphql")
    interface GitlabGraphqlClient {

        @GraphqlMapping(path = "ql/echo.graphql")
        GraphqlResponse<Object> echo(@GraphqlParam("echoText") String text);

    }

}
```