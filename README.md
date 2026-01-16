# 动态数据管理系统 - 后端服务

轻量后端服务，支持动态字段配置、数据 CRUD，适配前端可视化管理页面。

## 核心信息
- **技术栈**：Spring Boot + MyBatis-Plus + MySQL
- **核心能力**：字段配置、数据增删改查、标准化 API 响应

## 环境要求
| 依赖  | 版本要求  |
| ----- | --------- |
| JDK   | 1.8+/17   |
| MySQL | 5.7+/8.0+ |
| Maven | 3.6+      |

## 核心接口（前缀：/api）

### 字段配置

| 请求方式 |      接口地址      |     功能     |                  关键参数                  |
| :------: | :----------------: | :----------: | :----------------------------------------: |
|   POST   |     /field/add     |   添加字段   | tableCode、fieldName、fieldCode、fieldType |
|   GET    |    /field/list     | 查询字段列表 |           tableCode（URL 参数）            |
|  DELETE  | /field/delete/{id} |   删除字段   |               id（路径参数）               |

### 数据管理

| 请求方式 |     接口地址      |     功能     |               关键参数                |
| :------: | :---------------: | :----------: | :-----------------------------------: |
|   POST   |     /data/add     |   新增数据   |        tableCode、fieldValues         |
|   GET    |    /data/list     | 查询数据列表 |         tableCode（URL 参数）         |
|   PUT    | /data/update/{id} |   修改数据   | id（路径参数）、fieldValues（请求体） |
|  DELETE  | /data/delete/{id} |   删除数据   |            id（路径参数）             |

## 备注

- 已配置全局跨域，前端可直接调用接口

- 统一响应格式：

  ```json
  {
    "code": 200,
    "msg": "success",
    "data": {}
  }
  ```

  
