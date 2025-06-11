# 二手书院 - 校园二手书交易平台 📚

> **让知识传递，让书香延续** - 一个现代化的校园二手书交易平台

## 📋 目录

- [项目简介](#项目简介)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [快速开始](#快速开始)
- [详细安装](#详细安装)
- [项目结构](#项目结构)
- [功能模块](#功能模块)
- [API接口](#API接口)
- [数据库设计](#数据库设计)
- [部署指南](#部署指南)
- [常见问题](#常见问题)
- [更新日志](#更新日志)
- [贡献指南](#贡献指南)

## 🚀 项目简介

二手书院是一个专为校园师生打造的二手书交易平台，旨在促进知识的传递和图书资源的有效利用。平台提供图书买卖、求书发布、用户管理等功能，采用现代化的Web技术栈构建。

### 🎯 项目目标

- 🌱 **环保理念**：减少纸质图书浪费，促进资源循环利用
- 📖 **知识共享**：让更多学生能够以实惠价格获得所需图书
- 🤝 **校园互助**：建立校园内部的图书共享社区
- 💰 **经济实惠**：为学生提供经济实惠的图书获取途径

## ✨ 功能特性

### 核心功能

- 🔐 **用户认证系统**
  - 学号密码登录
  - 用户信息管理
  - 个人资料编辑

- 📚 **图书管理**
  - 图书分类浏览
  - 关键词搜索
  - 图书详情查看
  - 图片展示支持

- 🛒 **交易功能**
  - 购物车管理
  - 图书上传销售
  - 价格品相设置
  - 库存管理

- 🔍 **求书系统**
  - 发布求书信息
  - 求书需求管理
  - 图片参考上传

- 👤 **个人中心**
  - 我的书摊管理
  - 我的求书管理
  - 联系方式设置
  - 交易记录查看

### 界面特色

- 🎨 **现代化UI设计**
  - 渐变色彩搭配
  - 毛玻璃效果
  - 响应式布局
  - 动画过渡效果

- 📱 **移动端适配**
  - 完全响应式设计
  - 触摸友好界面
  - 移动端优化

## 🛠 技术栈

### 前端技术

| 技术 | 版本 | 描述 |
|------|------|------|
| Vue.js | 3.x | 渐进式JavaScript框架 |
| Vue Router | 4.x | Vue.js官方路由管理器 |
| Axios | 1.x | HTTP客户端库 |
| Vite | 4.x | 前端构建工具 |
| CSS3 | - | 现代CSS特性 |

### 后端技术

| 技术 | 版本 | 描述 |
|------|------|------|
| Spring Boot | 2.7.0 | Java应用开发框架 |
| Spring MVC | 5.x | Web MVC框架 |
| MyBatis Plus | 3.x | MyBatis增强工具 |
| Apache Shiro | 1.x | 安全框架 |
| MySQL | 8.0 | 关系型数据库 |
| Maven | 3.x | 项目构建工具 |



## 🚀 快速开始

### 环境要求

- **Java**: JDK 11 或更高版本
- **Node.js**: 14.x 或更高版本
- **MySQL**: 8.0 或更高版本
- **Maven**: 3.6.x 或更高版本


## 📦 详细安装

### 1. 环境准备

#### Java环境
```bash
# 检查Java版本
java -version

# 如果没有安装Java 11+，请下载安装
# https://adoptopenjdk.net/
```

### 3. 后端配置

#### 配置文件修改
```yaml
# backend/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookshop?useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: root          # 修改为你的数据库用户名
    password: your_password # 修改为你的数据库密码
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 依赖安装与启动
```bash
cd backend

# 安装依赖
mvn clean install

# 启动应用
mvn spring-boot:run

# 或者直接运行主类
# 在IDE中运行 BookshopApplication.java
```

#### 验证后端启动
```bash
# 检查服务是否启动
curl http://localhost:8080/api/category/list

# 应该返回分类列表JSON数据
```

### 4. 前端配置

#### 依赖安装
```bash
cd frontend

# 安装依赖（推荐使用npm）
npm install

# 或使用yarn
yarn install
```

#### 开发环境启动
```bash
# 启动开发服务器
npm run dev

# 或
yarn dev
```

#### 生产环境构建
```bash
# 构建生产版本
npm run build

# 预览构建结果
npm run preview
```

## 📁 项目结构

```
bookshop/
├── frontend/                 # 前端Vue.js项目
│   ├── public/              # 静态资源
│   │   └── img/            # 图片资源
│   │       ├── book-list/  # 图书封面图片
│   │       └── icons/      # 图标文件
│   ├── src/                # 源代码
│   │   ├── components/     # 组件目录
│   │   ├── views/         # 页面组件
│   │   │   ├── Home.vue           # 首页
│   │   │   ├── Login.vue          # 登录页
│   │   │   ├── BookStore.vue      # 图书商城
│   │   │   ├── BookDetail.vue     # 图书详情
│   │   │   ├── AskBookStore.vue   # 求书区
│   │   │   ├── MyBookshelf.vue    # 我的书架
│   │   │   ├── Cart.vue           # 购物车
│   │   │   ├── UploadSell.vue     # 上传售书
│   │   │   ├── UploadAsk.vue      # 发布求书
│   │   │   └── ProfileEdit.vue    # 个人资料编辑
│   │   ├── router/        # 路由配置
│   │   ├── utils/         # 工具函数
│   │   └── main.js       # 入口文件
│   ├── package.json      # 依赖配置
│   └── vite.config.js    # Vite配置
│
├── backend/              # 后端Spring Boot项目
│   ├── src/main/java/com/daniel/bookshop/
│   │   ├── controller/   # 控制器层
│   │   │   ├── BookController.java      # 图书管理
│   │   │   ├── CartController.java      # 购物车管理
│   │   │   ├── CategoryController.java  # 分类管理
│   │   │   ├── UserController.java      # 用户管理
│   │   │   ├── BookImageController.java # 图片管理
│   │   │   └── FileController.java      # 文件上传
│   │   ├── service/      # 服务层
│   │   │   ├── impl/     # 服务实现
│   │   │   └── interfaces/ # 服务接口
│   │   ├── entity/       # 实体类
│   │   │   ├── Book.java        # 图书实体
│   │   │   ├── User.java        # 用户实体
│   │   │   ├── Cart.java        # 购物车实体
│   │   │   ├── Category.java    # 分类实体
│   │   │   └── BookImage.java   # 图书图片实体
│   │   ├── mapper/       # MyBatis映射器
│   │   ├── common/       # 公共类
│   │   │   ├── Response.java    # 统一响应格式
│   │   │   ├── Constants.java   # 常量定义
│   │   │   └── UserContext.java # 用户上下文
│   │   ├── config/       # 配置类
│   │   │   ├── ShiroConfig.java # Shiro安全配置
│   │   │   ├── CorsConfig.java  # 跨域配置
│   │   │   └── WebConfig.java   # Web配置
│   │   └── BookshopApplication.java # 启动类
│   ├── src/main/resources/
│   │   ├── application.yml      # 应用配置
│   │   ├── mapper/             # MyBatis XML映射
│   │   └── static/            # 静态资源
│   └── pom.xml               # Maven配置
│
├── docs/                 # 文档目录
│   ├── sql/             # 数据库脚本
│   │   └── bookshop.sql # 完整数据库脚本
│   ├── api/             # API文档
│   └── images/          # 文档图片
│
└── README.md            # 项目说明文档
```

## 🎯 功能模块

### 用户模块

**登录认证**
- 学号密码登录
- Session管理
- 登录状态验证
- 自动登录记住功能

**个人资料**
- 基本信息编辑
- 联系方式管理
- 头像性别设置
- 住址专业信息

### 图书模块

**图书浏览**
- 分类筛选（9大类别）
- 关键词搜索
- 分页展示
- 图书详情查看

**图书管理**
- 图书信息录入
- 图片上传关联
- 价格品相设置
- 库存数量管理
- 上架下架操作

### 交易模块

**购物车功能**
- 添加图书到购物车
- 数量调整
- 批量选择
- 小计计算
- 购物车清空

**交易流程**
- 立即购买
- 购物车结算
- 订单确认
- 支付处理（待开发）

### 求书模块

**求书发布**
- 求书信息填写
- 参考图片上传
- 联系方式设置
- 期望价格设定
- 急需程度标记

**求书管理**
- 我的求书列表
- 求书信息编辑
- 求书状态更新
- 求书删除

## 📡 API接口

### 用户相关接口

#### 用户登录
```http
POST /api/user/login
Content-Type: application/json

{
  "username": "1505119",
  "password": "675844"
}

# 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "余文乐",
    "studentid": "1505119",
    "sex": 0,
    "tel": "13800138000",
    "address": "宿舍A101",
    "major": "计算机科学与技术"
  }
}
```

#### 获取用户信息
```http
GET /api/user/info

# 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "余文乐",
    "studentid": "1505119",
    "sex": 0,
    "tel": "13800138000",
    "address": "宿舍A101",
    "major": "计算机科学与技术"
  }
}
```

#### 更新用户信息
```http
PUT /api/user/update
Content-Type: application/json

{
  "name": "余文乐",
  "tel": "13800138000",
  "address": "宿舍A101",
  "major": "计算机科学与技术"
}
```

### 图书相关接口

#### 获取图书列表
```http
GET /api/book/list?pageNum=1&pageSize=10&categoryId=1&keyword=Java&bookType=1&userId=1

# 参数说明
# pageNum: 页码（默认1）
# pageSize: 每页数量（默认10）
# categoryId: 分类ID（可选）
# keyword: 搜索关键词（可选）
# bookType: 图书类型（0=求书，1=售书，可选）
# userId: 用户ID（查询指定用户的图书）

# 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 88,
    "size": 10,
    "current": 1,
    "records": [
      {
        "id": 1,
        "name": "Java编程思想",
        "author": "Bruce Eckel",
        "press": "机械工业出版社",
        "originalPrice": 108.00,
        "price": 60.00,
        "degree": 8,
        "description": "Java编程经典教材",
        "stock": 1,
        "bookType": 1,
        "cid": 1,
        "uid": 1,
        "bookImage": {
          "id": 1,
          "bid": 1,
          "type": 0
        }
      }
    ]
  }
}
```

#### 获取图书详情
```http
GET /api/book/{id}

# 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "Java编程思想",
    "author": "Bruce Eckel",
    "press": "机械工业出版社",
    "originalPrice": 108.00,
    "price": 60.00,
    "degree": 8,
    "description": "Java编程经典教材，适合Java初学者和进阶者",
    "stock": 1,
    "bookType": 1,
    "cid": 1,
    "uid": 1,
    "bookImage": {
      "id": 1,
      "bid": 1,
      "type": 0
    }
  }
}
```

#### 添加图书
```http
POST /api/book
Content-Type: application/json

{
  "name": "Spring Boot实战",
  "author": "Craig Walls",
  "press": "人民邮电出版社",
  "originalPrice": 89.00,
  "price": 45.00,
  "degree": 9,
  "description": "Spring Boot开发指南",
  "stock": 1,
  "bookType": 1,
  "cid": 1
}
```

### 购物车相关接口

#### 添加到购物车
```http
POST /api/cart/add?bookId=1&quantity=1

# 响应
{
  "code": 200,
  "message": "success",
  "data": null
}
```

#### 获取购物车列表
```http
GET /api/cart/list

# 响应
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "userId": 1,
      "bookId": 1,
      "quantity": 2,
      "createTime": "2024-01-01T10:00:00",
      "updateTime": "2024-01-01T11:00:00",
      "book": {
        "id": 1,
        "name": "Java编程思想",
        "price": 60.00,
        "author": "Bruce Eckel",
        "press": "机械工业出版社",
        "degree": 8
      }
    }
  ]
}
```

#### 更新购物车数量
```http
PUT /api/cart/update?cartId=1&quantity=3
```

#### 删除购物车项目
```http
DELETE /api/cart/remove/{cartId}
```

#### 清空购物车
```http
DELETE /api/cart/clear
```

#### 获取购物车数量
```http
GET /api/cart/count

# 响应
{
  "code": 200,
  "message": "success",
  "data": 5
}
```

### 分类相关接口

#### 获取分类列表
```http
GET /api/category/list

# 响应
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "计算机/互联网",
      "description": "编程、软件开发、网络技术等"
    },
    {
      "id": 2,
      "name": "经济管理",
      "description": "经济学、管理学、金融投资等"
    }
  ]
}
```

### 文件上传接口

#### 上传文件
```http
POST /api/file/upload
Content-Type: multipart/form-data

# 表单数据
file: [选择的文件]

# 响应
{
  "code": 200,
  "message": "success",
  "data": "文件上传成功"
}
```

### 图书图片关联接口

#### 创建图片关联
```http
POST /api/bookImage
Content-Type: application/json

{
  "bid": 1,
  "type": 0
}

# 响应
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "bid": 1,
    "type": 0
  }
}
```

## 🗄 数据库设计

### 用户表 (user)
```sql
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` varchar(50) NOT NULL COMMENT '用户姓名',
  `studentid` varchar(20) NOT NULL UNIQUE COMMENT '学号',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `sex` tinyint DEFAULT '0' COMMENT '性别(0=男,1=女)',
  `tel` varchar(20) DEFAULT NULL COMMENT '电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `major` varchar(100) DEFAULT NULL COMMENT '专业',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

### 图书表 (book)
```sql
CREATE TABLE `book` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图书ID',
  `name` varchar(200) NOT NULL COMMENT '书名',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `press` varchar(100) DEFAULT NULL COMMENT '出版社',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `degree` int DEFAULT NULL COMMENT '新旧程度(1-10)',
  `description` text COMMENT '描述',
  `stock` int DEFAULT '1' COMMENT '库存',
  `book_type` tinyint NOT NULL DEFAULT '1' COMMENT '图书类型(0=求书,1=售书)',
  `cid` int DEFAULT NULL COMMENT '分类ID',
  `uid` int NOT NULL COMMENT '用户ID',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_book_type` (`book_type`),
  KEY `idx_category` (`cid`),
  KEY `idx_user` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书表';
```

### 购物车表 (cart)
```sql
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` int NOT NULL COMMENT '用户ID',
  `book_id` int NOT NULL COMMENT '图书ID',
  `quantity` int NOT NULL DEFAULT '1' COMMENT '数量',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_book` (`user_id`,`book_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_book_id` (`book_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';
```

### 分类表 (category)
```sql
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `description` varchar(200) DEFAULT NULL COMMENT '分类描述',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';
```

### 图书图片表 (bookimage)
```sql
CREATE TABLE `bookimage` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `bid` int NOT NULL COMMENT '图书ID',
  `type` tinyint DEFAULT '0' COMMENT '图片类型(0=封面)',
  `create_time` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_book_id` (`bid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书图片表';
```

### 数据库关系

```
用户表 (user)
    ├── 一对多 → 图书表 (book)
    └── 一对多 → 购物车表 (cart)

图书表 (book)
    ├── 多对一 → 用户表 (user)
    ├── 多对一 → 分类表 (category)
    ├── 一对多 → 购物车表 (cart)
    └── 一对一 → 图书图片表 (bookimage)

购物车表 (cart)
    ├── 多对一 → 用户表 (user)
    └── 多对一 → 图书表 (book)

分类表 (category)
    └── 一对多 → 图书表 (book)

图书图片表 (bookimage)
    └── 多对一 → 图书表 (book)
```

## 🚀 部署指南

### 开发环境部署

#### 前端部署
```bash
# 安装依赖
cd frontend
npm install

# 启动开发服务器
npm run dev

# 访问地址：http://localhost:5173
```

#### 后端部署
```bash
# 编译打包
cd backend
mvn clean package

# 运行JAR包
java -jar target/bookshop-1.0.0.jar

# 或直接运行
mvn spring-boot:run

# 访问地址：http://localhost:8080
```

### 生产环境部署

#### 前端生产部署
```bash
# 构建生产版本
cd frontend
npm run build

# 将dist目录部署到Web服务器
# 例如：nginx、apache等
```


#### 后端生产部署
```bash
# 1. 构建JAR包
mvn clean package -Dmaven.test.skip=true

# 2. 上传到服务器
scp target/bookshop-1.0.0.jar user@server:/path/to/app/

# 3. 创建systemd服务
sudo vi /etc/systemd/system/bookshop.service
```

systemd服务配置：
```ini
[Unit]
Description=Bookshop Spring Boot Application
After=network.target

[Service]
Type=simple
User=bookshop
WorkingDirectory=/path/to/app
ExecStart=/usr/bin/java -jar bookshop-1.0.0.jar
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# 4. 启动服务
sudo systemctl daemon-reload
sudo systemctl enable bookshop
sudo systemctl start bookshop

# 5. 查看状态
sudo systemctl status bookshop
```



```bash
# 启动所有服务
docker-compose up -d

# 查看日志
docker-compose logs -f

# 停止服务
docker-compose down
```

## ❓ 常见问题
**Q: 数据库连接失败？**
```yaml
# 检查数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookshop?useUnicode=true&characterEncoding=utf-8&allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```
