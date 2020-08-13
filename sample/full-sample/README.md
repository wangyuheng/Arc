# Dgraph Sample

## 使用

### 1. 本机安装dgraph

```shell script
docker run --rm -it -p 8080:8080 -p 9080:9080 -p 8000:8000 -v ~/dgraph:/dgraph dgraph/standalone:v20.03.0
```

1. 浏览器通过 http://localhost:8000/?latest 访问
2. 应用程序通过 9080 访问

```yaml
arc:
  dgraph:
    urls: localhost:9080
```

### 2. 本机安装zipkin(可选)

```shell script
docker run -d -p 9411:9411 openzipkin/zipkin-slim:2.21
```

1. 浏览器通过 http://localhost:9411 访问

```yaml
spring:
  zipkin:
    enabled: true
    base-url: http://localhost:9411
  sleuth:
    sampler:
      probability: 1
```

### 3. 启动

通过 `FullSampleApplication` 启动后在浏览器访问 

- http://localhost:8402/playground 访问工作台及示例数据
- http://localhost:8402/voyager 查看schema结构及关联关系
