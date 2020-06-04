# ITC

> itaobao_cloud社交平台



## 简介

社交类平台，富有交友，发帖，评论，文章管理，搜索等模块

## 技术介绍

### 后台框架

采用SpringCloud系列

- 部署及调度：
    - 各个模块的部署采用docker进行，服务都向eureka注册

- 请求处理：

    用户请求交给Web网关处理，管理系统请求交给Manager网关处理

### 其他技术

- Mysql
    - 所有重要数据的存储
- redis
    - 点赞处理，验证码备份
- MongoDB
    - 对文章类复杂类型数据进行存储
- Elasticsearch
    - 站内搜索服务
    - Logstash：依据指定数据库数据对索引进行同步更新
- RabbitMQ
    - 消息总线Bus服务
    - 验证码服务

### 接口风格

- RestFul风格
- 接口API开发采用Swagger

## 依赖

| 模块名称          | 作用           | 模块端口 | 模块依赖                                   |
| ----------------- | -------------- | -------- | ------------------------------------------ |
| itaobao_eureka    | 微服务注册     | 6868     |                                            |
| itaobao_article   | 文章模块       | 9001     | mysql，redis                               |
| itaobao_base      | 基础微服务     | 9002     | mysql                                      |
| itaobao_spit      | 评论微服务     | 9003     | mongodb(spitdb)，redis                     |
| itaobao_friend    | 交友微服务     | 9005     | mysql                                      |
| itaobao_gathering | 活动微服务     | 9006     | mysql                                      |
| itaobao_manager   | 后台微服务网关 | 9007     |                                            |
| itaobao_qa        | 问答微服务     | 9008     | mysql，itaobao_base，user模块的jwt鉴定权限 |
| itaobao_recruit   | 推荐消息微服务 | 9009     | mysql                                      |
| itaobao_search    | 搜索微服务     | 9010     | es                                         |
| itaobao_sms       | 短信微服务     | 9011     | rabbitMQ                                   |
| itaobao_user      | 用户微服务     | 9012     | mysql，redis，itaobao_sms, itaobao_common  |
| itaobao_common    | 基础工具模块   |          |                                            |
| itaobao_config    | 配置中心模块   |          | 远程配置地址（例如：gitee，github）        |

> 注：以上各个模块均依赖itaobao_config

## 尝试

> 部署此系统，我们希望你的部署环境RAM达到8G，否则可能导致一些模块启动失败
>
> 如果启动部分，我们建议需要先将其依赖模块启动

### 1.部署环境

> 我们希望你能在服务器（也可以是本地虚拟机）上安装如下软件，端口建议保留其默认端口（如果需要修改，建议在相关联模块中的配置文件处进行同步修改）

- Docker
- Mysql5.7
- MongoDB
- Elasticsearch
    - ik
    - head
- Redis
- Registry

### 2.项目导入

- 下拉项目到本地：

    clone本项目，编辑软件（例如IDEA）打开

    ```shell
    git clone https://github.com/herycs/itaobao_cloud.git
    ```

### 3.配置文件修改

对项目内配置等进行修改

1. 修改各个模块的bootstrap.yml文件

    将IP改为目标服务器（或虚拟机）IP，其余部分可以不用修改

2. 远程配置文件

    配置文件地址在Config模块的bootstrap.yml文件中，此处建议将项目根路径下的config/目录下的文件发布到远程git上

    - 例如：
        - 你可以注册gitee账号，并创建一个**公开**项目（若是私有的的话，需要在/resource/bootstrap.yml中uri模块填写用户名，密码）
        - 完成上述步骤后将：config模块的/resource/bootstrap.yml文件中的git:uri地址修改为你的git地址

3. 修改远程配置文件

    将远程项目的所有的yml中的地址修改为你服务器（或虚拟机）地址

4. 修改每个模块的pom文件中的**dockerHost**

    ```xml
    <build>
        <defaultGoal>compile</defaultGoal>
        <finalName>app</finalName>
        <plugins>
                    ...
                    <dockerHost>http://XXX.XXX.XXX:2375</dockerHost>
            	</configuration>
        	</plugin>
    	</plugins>
    </build>
    ```

### 3.向服务器推送

> 以下为IDEA中操作方式

- 选中要推送模块，例如：itaobao_config，右键--->Open in terminal

- 在窗口执行

    ```shell
    mvn clean package docker:build -DpushImage
    ```

### 4.执行

```shell
创建容器并运行
运行顺序：config ---> eureka --->others
```

## docker

### 快捷操作

```shell
# 批量停止与删除
docker stop `docker ps | awk '{print($1)}'`
docker rmi `docker images | grep none | awk '{print $3}'`
```

### 服务发布示例命令

### 微服务

```shell
//发布微服务
docker run -di --name=itaobao_config -p 12000:12000 122.51.8.208:5000/itaobao_config:1.0-SNAPSHOT
2af0a73bf2c591ad0581058d16e38df3a3fe0c955418033578b41b61a585e05e

docker run -di --name=itaobao_eureka -p 6868:6868 122.51.8.208:5000/itaobao_eureka:1.0-SNAPSHOT
0f9c6c4445cca1224b8f39b7bcfc351c36e83de82e3f75efb4bcf1ac74bc3d3e
 
docker run -di --name=itaobao_article -p 9001:9001 122.51.8.208:5000/itaobao_article:1.0-SNAPSHOT
62871f46b0bc1c243de4e309dfbc938844212182d5098e050d62c4630e93b3d2

docker run -di --name=itaobao_base -p 9002:9002 122.51.8.208:5000/itaobao_base:1.0-SNAPSHOT
eaad07cec28675e2602f08a2583ad670275840be581222c993c5d655205e97e5

docker run -di --name=itaobao_spit -p 9003:9003 122.51.8.208:5000/itaobao_spit:1.0-SNAPSHOT
642011e5a3c91bb11aef06daac533c009c99ddc35bca19999ecff881dc5c7fd5

docker run -di --name=itaobao_web -p 9004:9004 122.51.8.208:5000/itaobao_web:1.0-SNAPSHOT
0148e304824a2abd61cc489ccf07489a1b01b1466bb189fadcbc0cadebfe85ac

docker run -di --name=itaobao_friend -p 9005:9005 122.51.8.208:5000/itaobao_friend:1.0-SNAPSHOT
docker run -di --name=itaobao_gathering -p 9006:9006 122.51.8.208:5000/itaobao_gathering:1.0-SNAPSHOT
docker run -di --name=itaobao_manager -p 9007:9007 122.51.8.208:5000/itaobao_manager:1.0-SNAPSHOT
58c40c79dc9891ef92e401fa4a3c28cccbbc17ac69505a24d5689f0d3fb82f40

docker run -di --name=itaobao_qa -p 9008:9008 122.51.8.208:5000/itaobao_qa:1.0-SNAPSHOT
f0030df5448ff72045bc4055c125895ef14fb963de6089f35d9a81ec46b7444b

docker run -di --name=itaobao_recuit -p 9009:9009 122.51.8.208:5000/itaobao_recruit:1.0-SNAPSHOT
04e249085b0d896225f1b24781daa02d0d41b175a97e5d71070e11172e9c8509

docker run -di --name=itaobao_search -p 9010:9010 122.51.8.208:5000/itaobao_search:1.0-SNAPSHOT
b5c7d486c88fb4238ec5315e4e1c875138121a8e022b23959bcc691dd22e5fbb

docker run -di --name=itaobao_sms -p 9011:9011 122.51.8.208:5000/itaobao_sms:1.0-SNAPSHOT
4c781aa90c7be236eea3fa21ee4f4eb84268b9d87fc977c902b2e1b1e7cfb107

docker run -di --name=itaobao_user -p 9012:9012 122.51.8.208:5000/itaobao_user:1.0-SNAPSHOT
60486ed60477d7ade24426207c3660e73024e95ea976e3054984594971b2b717

docker run -di --name=itaobao_file -p 9016:9016 122.51.8.208:5000/itaobao_file:1.0-SNAPSHOT
0881dba3a06b51c0015ec6d6ffeaa1e59105af54c4fcb973a87f1e78dc9175f9
```

## 报错处理

### 报错处理

1. 公共模块找不到，无法解决对公共模块的依赖（例如：common模块)

    > Could not resolve dependencies for project com.itaobao:itaobao_user:jar:1.0-SNAPSHOT: Failu
    > re to find com.itaobao:itaobao_common:jar:1.0-SNAPSHOT in https://repo.spring.io/snapshot was cached in the local repository, resolution will not b
    > e reattempted until the update interval of spring-snapshots has elapsed or updates are forced

- 公共模块下不需要打包插件，否则在第二次打包编译时会去尝试执行main方法
- 在确保pom文件无异常情况下可以执行，clean, compose, install更新文件到本地后再试

2. ssl权限问题

    在pom的build部分添加（不添加的话对于一些安全方面的校验存在问题）

    ```xml
    <defaultGoal>compile</defaultGoal>
    ```

3. 多次尝试不成功

    ```shell
    检查各个模块的pom文件无异常情况下尝试在root模块进行install操作,以更新各个模块pom文件及数据
    ```

### net

1.WARNING: IPv4 forwarding is disabled. Networking will not work.  

```shell
vim  /usr/lib/sysctl.d/00-system.conf

添加
net.ipv4.ip_forward=1

重启服务
systemctl restart network
```

### mvn

1.问题：

 Exception caught: java.util.co
ncurrent.ExecutionException: com.spotify.docker.client.shaded.javax.ws.rs.ProcessingException: javax.net.ssl.SSLHandshakeException: sun.security.va
lidator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certificati
on path to requested target

处理

```xml
<build>
	//增加：
	<defaultGoal>compile</defaultGoal>
</build>
```

## 