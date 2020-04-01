# phlink-common-framework
普惠互联通用框架

## 基础技术
- JDK 1.8
- Springboot 2.1.12
- Mybatis-plus
- Postgres
- Redisson
- 分页使用 pagehelper
- Freemark（代码生成模版，邮件模版等）

## 名词说明
### core、module和service
#### core
基础核心包，用于支撑module和service
包名以com.phlink.core开头
#### module
扩展包，扩展web服务的功能
包名以com.phlink.module开头
#### serivce
服务包，可单独部署为服务，依赖core-web的支持
包名以com.phlink.serivce开头
 
## 模块说明
### core-base
基础模块
### core-web
web 的核型模块，需要开发业务，直接引入该模块即可
- 登录：
    - 支持用户名登录
    - 支持验证码登录
    - 支持手机短信登录
    - 支持登录失败尝试限制 
- 用户权限角色管理
- 统一日志管理
- 字典
- 限流
- 缓存
### module-file
文件服务模块，提供文件上传，文件删除等服务，基于fastdfs
### module-code-generator
代码生成模块，计划提供相关接口实现在线动态生成代码
### module-proto
提供 grpc 接口描述

## 整体结构
![整体结构](http://ww1.sinaimg.cn/large/7bc111b9ly1gd6astegf9j21e60nyjuj.jpg)

## 整体规划
![整体规划](http://ww1.sinaimg.cn/large/7bc111b9ly1gd6at6ecdtj21e80kltbb.jpg)

## 文档
见wiki: https://github.com/Sevncz/phlink-common-framework/wiki


## QA
### VSCODE
修改module之后Springboot-dashboard无法渲染
```
Try closing vscode and cleaning your workspace storage:

On linux: $HOME/.config/Code/User/workspaceStorage
On mac: $HOME/Library/Application\ Support/Code/User/workspaceStorage
On windows: %APPDATA%\Code\User\workspaceStorage
In case it did't help, if you have the VScodeVim extension, try disabling it and restarting vscode. This might be helpful.

```

### maven模版使用
```
# 进入到生成的archetype目录
cd target\generated-sources\archetype

# 将archetype安装到本地
mvn install

# 执行下面操作更新本地的archetype-catalog.xml
mvn archetype:crawl

新建项目时：
mvn archetype:generate -DarchetypeCatalog=local 
```
