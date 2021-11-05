# Arc-Graphql-Client-example

example For `arc-graphql-client`

## 如何使用

```shell script
mvn clean install
java -jar target/arc-graphql-client-example-1.6.0.jar 
```

访问 http://localhost:8080/rest?name=Arc

得到返回值 

```json
{
    "data": {
        "echo": "nil says: Hello Arc"
    }
}
```