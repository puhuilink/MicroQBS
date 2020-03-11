# phlink-common-framework
普惠互联通用框架

## 基础技术
- JDK 1.8
- Springboot 2.1.12
- Mybatis-plus
- Postgres
- Redisson
- 分页使用 pagehelper

## 模块说明
### web-common
web 模块的通用模块
### web-base
web 模块的基础模块，需要开发业务，直接引入该模块即可
### web-file-service
文件服务模块，提供文件上传，文件删除等服务，基于fastdfs
### proto
提供 grpc 接口描述
### web-demo
web-base的使用示例

## 整体结构
![整体结构](https://github.com/Sevncz/phlink-common-framework/blob/master/doc/base1.png)

## 整体规划
![整体规划](https://github.com/Sevncz/phlink-common-framework/blob/master/doc/base2.png)

## 使用文档
### 初始化数据库
./doc 目录下 init.sql
### TODO 配置修改
#### redisson.yaml
修改以下两项为使用的redis服务
```yaml
address: "redis://host:port"
password: password
```
#### application.yaml
修改数据库配置
```yaml
datasource:
    primary:
      username: postgres
      password: postgres
      driver-class-name: org.postgresql.Driver
      url: jdbc:postgresql://localhost:5432/phlink_demo
```

使用web-file-service时，修改fdfs
```yaml
fdfs:
  tracker-list:            #TrackerList参数,支持多个
    - 127.0.0.1:22122
```

### 基本约定