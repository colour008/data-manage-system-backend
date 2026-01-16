package com.example.datamanagesystem.entity;

import lombok.Data;

import java.util.Map;

/**
 * 接收前端提交的动态数据
 */
@Data
public class DataDTO {
	/**
	 * 业务表标识
	 */
	private String tableCode;

	/**
	 * 动态字段值：key=fieldCode，value=字段值
	 */
	private Map<String, String> fieldValues;
}
