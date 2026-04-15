# 🌊 Blue Ocean Platform (蓝海平台)

> **Build Less, Create More.** 基于 Spring Boot 3.5.x + JDK 21 的高内聚、开箱即用的企业级开发底座。

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.10-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![JDK](https://img.shields.io/badge/JDK-21-orange.svg)](https://www.oracle.com/java/technologies/downloads/#java21)

---

## 📖 1. 项目简介

**Blue Ocean Platform** 是一套标准化的技术基石，旨在解决企业级开发中“重复造轮子”、“规范不统一”和“依赖地狱”等痛点。它通过高度抽象的 Starter 机制，将多租户、全局异常、审计日志、分布式锁、缓存增强等复杂底层逻辑下沉，让开发者只需关注核心业务。

### ✨ 核心特性
*   🎯 **版本管理 (BOM)**：采用 **独立外置 BOM** (`blue-ocean-dependencies`) 模式，一站式接管第三方库（Hutool, Redisson, MP 等）版本，消除 Jar 冲突。
*   🚀 **现代基石**：全面拥抱 **JDK 21**（虚拟线程适配）与 **Spring Boot 3.5.x** 高级特性。
*   🛡️ **企业级增强**：内建生产级 `GlobalExceptionHandler`、MyBatis-Plus 多租户隔离、Jackson 序列化精度修复。
*   ⚡ **Redis 强化**：基于 Redisson 4.3.0，支持插拔式分布式锁 (`@DistributedLock`)、静态代理式 `RedisUtils`。
*   🧩 **聚合配置**：遵循 Spring Boot 自动装配规约，所有 Bean 均支持 `@ConditionalOnMissingBean` 按需覆盖。

---

## 🏗️ 2. 工程架构

本项目采用 **Maven 多模块** 架构，遵循 **BOM -> Parent -> Starter** 的职能解耦。

```text
blue-ocean-platform (Root Aggregator)
├── blue-ocean-dependencies (BOM - 版本控制中心)
├── blue-ocean-boot-starter-parent (业务接入父工程 - 承载官方环境与平台 BOM)
├── blue-ocean-core (核心通用模块 - 纯净 Java 工具、业务异常、标准 Result)
├── blue-ocean-spring-boot-starter (基础底座 - SpringContextAware & 基础增强)
├── blue-ocean-spring-boot-starter-web (Web 增强 - MVC 全局拦截、JSON 序列化精度修复)
├── blue-ocean-spring-boot-starter-redis (缓存增强 - Redisson 集成、分布式锁、RedisUtils)
└── blue-ocean-spring-boot-starter-mybatis-plus (数据增强 - 自动填充、多租户、安全拦截)
```

---

## 🚀 3. 快速开始 (Quick Start)

### 3.1 业务项目接入 (推荐)

在业务服务的 `pom.xml` 中直接继承我们的 Parent 工程，即可一站式获得 Spring Boot 运行环境以及 Blue Ocean 的所有版本管理：

```xml
<parent>
    <groupId>com.cecilylove</groupId>
    <artifactId>blue-ocean-boot-starter-parent</artifactId>
    <version>1.0.0</version>
    <relativePath/>
</parent>

<dependencies>
    <!-- 按需引入 Starter -->
    <dependency>
        <groupId>com.cecilylove</groupId>
        <artifactId>blue-ocean-spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

### 3.2 手动引入 BOM (备选)

如果你无法修改项目的 `<parent>`，也可以通过导入 BOM 的方式接入：

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

---

## ⚙️ 4. 核心配置清单

```yaml
blue-ocean:
  # 🌐 Web 模块 (默认开启)
  web:
    enabled: true
    jackson:
      enabled: true
      enable-long-to-string: true # 解决前端 JS 精度丢失 (Long -> String)
  
  # 💾 MyBatis-Plus (含多租户隔离)
  mybatis-plus:
    enabled: true
    enable-tenant-line: true # 开启多租户字段过滤
    db-type: mysql           # 指定数据库类型以优化分页 SQL
```

---

## 🛠️ 5. 扩展与定制 (Extension)

### 5.1 如何覆盖框架默认逻辑？
框架内所有核心 Bean 均使用 `@ConditionalOnMissingBean` 标注。

*   **Jackson 定制**：只需自定义 `Jackson2ObjectMapperBuilderCustomizer` 即可实现配置聚合。
*   **分布式锁定制**：直接定义自己的 `DistributedLockAspect` 即可通过 Bean 覆盖接管原有逻辑。

### 5.2 开发规约
1.  **异常捕获**：业务层严禁吞掉异常，应抛出 `BlueOceanBusinessException`。支持 SLF4J 风格占位符：
    `throw new BlueOceanBusinessException(500, "用户{}不存在", userId);`
2.  **断言工具**：优先使用 `BizAssertUtils` 进行业务校验，逻辑失败时会自动抛出标准业务异常。
3.  **命名规范**：Boolean 属性严禁使用 `is` 前缀（如 `deleted`），确保护持序列化兼容性。
