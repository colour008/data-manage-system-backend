package com.example.datamanagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 字段定义实体类（与 field_define 表严格匹配）
 */
@Data
@TableName("field_define") // 绑定数据库表名
public class FieldDefine {
	/**
	 * 主键（自增）
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 关联业务表标识（如 user_data）
	 */
	private String tableCode;

	/**
	 * 字段名称（前端显示，如「用户名」）
	 */
	private String fieldName;

	/**
	 * 字段编码（唯一，如「user_name」）
	 */
	private String fieldCode;

	/**
	 * 字段类型（VARCHAR/INT/DATETIME 等）
	 */
	private String fieldType;

	/**
	 * 是否必填 0-否 1-是
	 */
	private Integer isRequired;

	/**
	 * 显示排序
	 */
	private Integer sortNum;

	/**
	 * 创建时间（数据库自动赋值，无需手动传）
	 */
	private Date createTime;

	// 已移除：fieldLength（数据库表中无该字段，Mapper插入也无需）
}