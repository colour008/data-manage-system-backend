动态数据管理系统 - 后端服务
轻量后端服务，支持动态字段配置、数据 CRUD，适配前端页面
技术栈：Spring Boot + MyBatis-Plus + MySQL
核心：字段配置、数据增删改查、标准化 API
环境要求
JDK 1.8+/17、MySQL 5.7+/8.0+、Maven 3.6+
本地启动
新建数据库 dynamic_data_manage，修改 application.yml 配置数据库账号密码
执行 sql/init.sql 初始化表结构
启动：mvn spring-boot:run，访问 http://localhost:8080/api/health 验证（返回 200 即正常）
核心接口（前缀：/api）
字段配置
POST /field/add：添加字段（传 tableCode、fieldName、fieldCode、fieldType）
GET /field/list：查询字段（传 tableCode 参数）
DELETE /field/delete/{id}：删除字段（路径传 id）
数据管理
POST /data/add：新增数据（传 tableCode、fieldValues）
GET /data/list：查询数据（传 tableCode 参数）
PUT /data/update/{id}：修改数据（路径传 id，body 传 fieldValues）
DELETE /data/delete/{id}：删除数据（路径传 id）
生产部署
配置 application-prod.yml 生产数据库信息
打包：mvn clean package -Pprod
后台启动：nohup java -jar 包名.jar > app.log 2>&1 &
备注
跨域已配置，前端可直接调用
统一响应格式：{code:200, msg:"success", data:{}}
