# Dgraph

### 什么是Dgraph

- https://dgraph.io/docs/

> Dgraph is an open-source, scalable, distributed, highly available and fast graph database, designed from the ground up to be run in production.

#### GraphQL+-

- https://dgraph.io/docs/tips/

### 官方java客户端

- https://github.com/dgraph-io/dgraph4j

### 弊端

1. DB无table概念，字段跨领域复用(company name & user name)
2. query & mutation 是两种语法
3. client没有sql解析，通过字符串拼接
4. client没有Spring封装
5. DB数据与javaBean之间无法相互转换

### 解决方案

#### DB schema约束

1. 字段增加prefix(值同dgraph.type)，等同于table name
2. 增加domainClass字段，用于反序列化

#### Query

1. 通过xml编写GraphQL+-
    1. 支持xml定义通用fragment
    2. TODO 支持graphql变量动态输入
    3. Spring容器启动后扫描相应目录下的xml文件并加载至内存
    4. 通过xml指定id执行对应GraphQL+-并返回结果
2. TODO 定义通用getOne、queryAll方法

### Q&A

#### 1. 为什么要定义 schema type ？

使用函数简化参数，比如 `expand()` 