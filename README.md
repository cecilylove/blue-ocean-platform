# 🌊 Blue Ocean Platform (蓝海平台)

> **Build Less, Create More.** 基于 Spring Boot 3.5.x + JDK 21 的高内聚、开箱即用的企业级开发底座。

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JDK](https://img.shields.io/badge/JDK-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)

---

## 📖 1. 项目简介

**Blue Ocean Platform** 是一套标准化的技术基石，旨在解决企业级开发中“重复造轮子”、“规范不统一”和“依赖地狱”等痛点。它通过高度抽象的 Starter 机制，将多租户、全局异常、审计日志、序列化精度等复杂底层逻辑下沉，让开发者只需关注核心业务。

### ✨ 核心特性
*   🎯 **版本仲裁**：采用**独立外置 BOM** (`blue-ocean-dependencies`) 模式，统一管理第三方与自身模块版本，彻底告别 Jar 冲突。
*   🚀 **现代基石**：全面拥抱 **JDK 21**（虚拟线程就绪）与 **Spring Boot 3.5.x**。
*   🛡️ **企业级增强**：内建企业级 `GlobalExceptionHandler`、MyBatis-Plus 多租户隔离、Jackson 序列化精度修复等。
*   🧩 **聚合配置**：采用模块化聚合配置（`WebAutoConfiguration`），支持按需插拔与用户自定义 Bean 无缝覆盖。

---

## 🏗️ 2. 工程架构

本项目采用 **Maven 多模块** 架构，遵循 **BOM -> Core -> Starter** 的演进路径。

```text
blue-ocean-platform (Root Aggregator)
├── blue-ocean-dependencies (BOM - 独立发布的版本控制中心)
├── blue-ocean-core (核心通用模块 - 纯净 Java 工具、POJO、枚举)
├── blue-ocean-spring-boot-starter (基础底座 - Spring 上下文感知)
├── blue-ocean-spring-boot-starter-web (Web 增强 - MVC 全局拦截、Jackson 增强)
└── blue-ocean-spring-boot-starter-mybatis-plus (数据增强 - 自动填充、多租户、安全拦截)
```

---

## 🚀 3. 快速开始 (Quick Start)


### 3.1 引入版本管理 (BOM)
在业务服务的 `pom.xml` 中优先引入独立 BOM。这确保了所有子模块都能获得正确的版本号，无需手动填写 `<version>`。

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.cecilylove</groupId>
            <artifactId>blue-ocean-dependencies</artifactId>
            <version>1.0.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 3.2 引入 Starter
根据业务需求引入对应功能：

```xml
<dependencies>
    <!-- Web 开发 (含全局异常、Jackson 增强) -->
    <dependency>
        <groupId>com.cecilylove</groupId>
        <artifactId>blue-ocean-spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 数据库开发 (含多租户、自动填充) -->
    <dependency>
        <groupId>com.cecilylove</groupId>
        <artifactId>blue-ocean-spring-boot-starter-mybatis-plus</artifactId>
    </dependency>
</dependencies>
```

---

## ⚙️ 4. 核心配置清单

底座组件提供了统一的 `blue-ocean` 前缀配置。

```yaml
blue-ocean:
  # 🌐 Web 模块 (默认开启)
  web:
    enabled: true
    global-exception-handler:
      enabled: true  # 开启企业级全局异常拦截
    jackson:
      enabled: true
      enable-date-format: true
      date-format: "yyyy-MM-dd HH:mm:ss"
      enable-long-to-string: false # 解决前端 JS 精度丢失，默认 false，按需开启
  
  # 💾 MyBatis-Plus 模块 (默认开启)
  mybatis-plus:
    enabled: true
    enable-tenant-line: true # 开启多租户自动隔离插件
    enable-pagination: true   # 开启物理分页
    enable-block-attack: true # 开启防全表更新/删除保护
```

---

## 🛠️ 5. 扩展与定制 (Extension)

### 5.1 如何覆盖框架默认逻辑？
框架内所有核心 Bean 均使用 `@ConditionalOnMissingBean` 标注。

*   **Jackson 定制**：只需在您的项目中定义任何 `Jackson2ObjectMapperBuilderCustomizer`，Spring 将会自动聚合您的配置。
*   **异常处理器定制**：您可以直接继承 `GlobalExceptionHandler` 并标注 `@RestControllerAdvice` 注册自己的 Bean 即可全量接管异常逻辑。

### 5.2 业务开发规范建议
1.  **异常捕获**：业务层严禁吞掉异常，应直接 `throw new BlueOceanBusinessException(...)`，由底座进行标准化 JSON 返回。
2.  **用户信息**：通过 `UserContextUtil.getUserId()` 随时随地获取当前登录用户。
3.  **懒加载 ext**：`CurrentUserInfo.getExt()` 支持懒加载初始化的原始引用，可直接进行 `put` 操作。

---

## 📅 6. 路线图 (Roadmap)
*   [x] 1.0.0 - 核心底座、BOM 独立、Web/MP 增强
*   [ ] 1.1.0 - Redis 分布式锁与缓存 Starter
*   [ ] 1.2.0 - 基于虚拟线程的 Log Starter (TraceId)

---

**Maintenance by cecilylove.** 🌊
