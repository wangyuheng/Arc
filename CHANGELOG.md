# 1.1.0

## highlight

- 新增generator模块，用于代码生成 [#5](https://github.com/YituHealthcare/Arc/issues/5)

### Common

1. 移除lombok依赖 [#19](https://github.com/YituHealthcare/Arc/issues/19)

### Generator

1. 根据GraphqlSchema生成DgraphSchema [#10](https://github.com/YituHealthcare/Arc/issues/10)
2. 根据GraphqlSchema生成Java代码 [#5](https://github.com/YituHealthcare/Arc/issues/5)

### Dgraph

1. 通过配置初始化数据库Schema [#7](https://github.com/YituHealthcare/Arc/issues/7)


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
