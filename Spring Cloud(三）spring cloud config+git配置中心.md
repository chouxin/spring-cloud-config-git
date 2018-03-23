# Spring Cloud(三）spring cloud config+git配置中心


 1. **在git上创建文件夹，放上开发，测试，生产环境的配置文件**
 2. **在本地创建config-server-git项目**
 3. **完成application.yml配置**
 4. **启动ConfigServerGitApplication.java**

[spring-config原理参考](http://www.ityouknow.com/springcloud/2017/05/22/springcloud-config-git.html)

***Server端***
 - GitHub目录结构
 ![github上config管理目录结构](https://img-blog.csdn.net/20180323164422443?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Nob3UzNDIxNzU4Njc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
 - 本地创建项目config-server-git
 ![config-server-git](https://img-blog.csdn.net/20180323170111942?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Nob3UzNDIxNzU4Njc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
 
 - application.yml

```
server:
  port: 8000   #注册中心端口是8001

spring:
  application:
    name: spring-cloud-config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/chouxin/spring-cloud-config-repo/
          search-paths: config-repo
          username: 342xxxxx@qq.com
          password: xxxxxxxx
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8001/eureka/
```

 - ConfigServerGitApplication.java

```
@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerGitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigServerGitApplication.class, args);
	}
}
```
***测试***

 - 在浏览器输入 http://localhost:8000/cn-qx-config/test

![config-server](https://img-blog.csdn.net/20180323171307871?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Nob3UzNDIxNzU4Njc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

***Client端***

 1. 创建config-client-git项目
 2. 创建bootstrap.yml文件
 3. 创建测试demo

spring-config-client项目目录结构
![config-client项目目录结构](https://img-blog.csdn.net/20180323174839983?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Nob3UzNDIxNzU4Njc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

```
**特别注意：上面这些与spring-cloud相关的属性必须配置在
bootstrap.properties中，config部分内容才能被正确加载。因为config
的相关配置会先于application.properties，而bootstrap.properties
的加载也是先于application.properties。**
```

 - application.yml
 
 

```
spring:
  application:
    name: config-client-git

server:
  port: 9000


eureka:
  client:
    service-url:
      defaultZone: http://localhost:8001/eureka/
```

 - bootstrap.yml
 

```
spring:
  cloud:
    config:
      name: cn-qx-config
      profile: dev
      uri: http://localhost:8000/
      label: master
server:
  port: 9000

#spring.application.name：对应{application}部分
#spring.cloud.config.name：对应配置文件名称 cn-qx-config-dev.yml
#spring.cloud.config.profile：对应{profile}部分
#spring.cloud.config.label：对应git的分支。如果配置中心使用的是本地存储，则该参数无用
#spring.cloud.config.uri：配置中心的具体地址
#spring.cloud.config.discovery.service-id：指定配置中心的service-id，便于扩展为高可用配置集群。

```

 - 启动类
 

```
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigClientGitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientGitApplication.class, args);
	}
}
```

 - 浏览器调用

```
@RestController
public class HelloController {

    @Value("${qx.hello}")
    private String gitConfigName;

    @GetMapping("/hello")
    public String fromGitHubServer(){
        return gitConfigName;
    }

}
```

 - ***测试***

浏览器输入
http://localhost:9000/hello 
config客户端调用config服务端，
![客户端调用服务端](https://img-blog.csdn.net/20180323175331290?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2Nob3UzNDIxNzU4Njc=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

完成啦 

[spring-cloud-config代码示例完整](https://github.com/chouxin/spring-cloud-config-git)

[spring-cloud-config资源文件](https://github.com/chouxin/spring-cloud-config-repo)