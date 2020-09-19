# 1.1.0

## highlight

- 新增generator模块，用于代码生成

### generator

1. 根据GraphqlSchema生成DgraphSchema

### Dgraph

1. 通过配置初始化数据库Schema

# 1.0.0

### Graphql

- add embed playground
- add embed voyager
- graphql client 用于服务端之间Graphql调用
- graphql 自定义业务异常处理及http status
- graphql 通过domainClass自动解析union type 及interface
- 增加 payload 解析工具

### Dgraph

- dgraph 增加 interceptor 支持
- dgraph client 通过 properties 配置
- xml增加 mutation 方法定义及解析
- dgraph 提供relationship操作
- dgraph 增加RDF处理工具
- dgraph 自动增加变量前缀
- dgraph config bean 自动配置
- dgraph xml location 可配置
- relationship custom name
- modify search index
- 增加upsert方法

### mq

- 基于VM Queue实现
