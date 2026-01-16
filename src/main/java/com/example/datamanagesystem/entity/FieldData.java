package com.example.datamanagesystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 动态字段数据表实体（与field_data表匹配）
 */
@Data
@TableName("field_data")
public class FieldData {
	/**
	 * 主键（自增）
	 */
	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 关联data_main主键（基础数据ID）
	 */
	private Long mainId;

	/**
	 * 关联field_define主键（字段定义ID）
	 */
	private Long fieldId;

	/**
	 * 字段值
	 */
	private String fieldValue;

	/**
	 * 创建时间（数据库自动赋值）
	 */
	private Date createTime;
}