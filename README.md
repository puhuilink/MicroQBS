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
/doc/sql 目录下的sql文件初始化 
### 配置修改
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
修改redis配置
```yaml
redis:
    database: 0
    password: qwe@123
    timeout: 3000
    #sentinel/cluster/single
    mode: single
    codec: org.redisson.codec.JsonJacksonCodec
    pool:
      #连接池配置
      max-idle: 16
      min-idle: 8
      max-active: 8
      max-wait: 3000
      conn-timeout: 3000
      so-timeout: 3000
      size: 10
    #单机配置
    single:
      address: "redis://127.0.0.1:6379"
    #集群配置
    cluster:
      scan-interval: 1000
      nodes:
      read-mode: SLAVE
      retry-attempts: 3
      failed-attempts: 3
      slave-connection-pool-size: 64
      master-connection-pool-size: 64
      retry-interval: 1500
    #哨兵配置
    sentinel:
      master: business-master
      nodes:
      master-onlyWrite: true
      fail-max: 3
```

使用web-file-service时，修改fdfs
```yaml
fdfs:
  tracker-list:            #TrackerList参数,支持多个
    - 127.0.0.1:22122
```

### 基本约定
#### MVC各层命名及结构
Controller：xxxController
Service: xxxService/xxxServiceImpl
DAO: xxxMapper

#### Controller 注解
使用@RestController，注意和@Controller的区别


#### 返回格式
```json
{
    "success": true,
    "message": "success",
    "code": "200",
    "timestamp": 1584251528246,
    "result": {}
}
```

#### HTTP 方法
- GET: 资源查询接口
- POST: 资源创建接口
- PUT: 资源修改接口
- DELETE: 资源删除接口

#### request body 校验
在需要校验的字段上加入
- OnAdd
- OnCheckID
- OnUpdate
用于Validated校验，可配置多个
```java
// 方法：
@Validated(value = {OnAdd.class})
@PostMapping
public void example(@RequestBody @Valid SaveEntityVO entitVO){ 
    service.save(entityVO);
}

// VO实体
class SaveEntityVO {
    @NotBlank(message = "{required}", groups = {OnAdd.class, OnUpdate.class})
    private String name;
}
```

#### 实体基本字段
所有实体对象都需要继承该BaseEntity类
```java
public class PhlinkBaseEntity {
    @Id
    @TableId
    @NotNull(message = "{required}", groups = {OnCheckID.class})
    @ApiModelProperty(value = "唯一标识")
    private String id = String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());

    @ApiModelProperty(value = "创建者")
    @TableField(fill = FieldFill.INSERT) // 自动填入创建用户
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT) // 自动填入创建时间
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    @TableField(fill = FieldFill.UPDATE) // 自动填入更新用户
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.UPDATE) // 自动填入更新时间
    private Date updateTime;

    @JsonIgnore
    @TableLogic
    @ApiModelProperty(value = "删除标志 默认0")
    private Integer delFlag = CommonConstant.STATUS_NORMAL;
}
```

#### 异常处理
在业务层显式抛出异常，由框架统一处理成标准返回结构
默认异常Code：
```
SUCCESS("200", "成功!"),
// 系统失败
BODY_NOT_MATCH("400","请求的数据格式不符!"),
SIGNATURE_NOT_MATCH("401","请求的数字签名不匹配!"),
FORBIDDEN("403","抱歉，您没有访问权限!"),
NOT_FOUND("404", "未找到该资源!"),
TOO_MANY_REQUESTS("429", "接口访问超出频率限制!"),
INTERNAL_SERVER_ERROR("500", "服务器内部错误!"),
SERVER_BUSY("503","服务器正忙，请稍后再试!"),
// 业务失败
FAIL("-100", "操作失败!"),
LOGIN_FAIL_MANY_TIMES("-101", "登录失败次数过多!"),
```
默认自定义异常：
- BizException
- CheckedException
- LimitAccessException
- LoginFailLimitException

#### 基本VO
- PageVO
- Result
- SearchVO
