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
