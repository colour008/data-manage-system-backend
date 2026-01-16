package com.example.datamanagesystem.service;

import com.example.datamanagesystem.entity.FieldDefine;

import java.util.List;

public interface FieldDefineService {
	/**
	 * 添加动态字段
	 */
	boolean addField(FieldDefine fieldDefine);

	/**
	 * 根据业务表标识查询字段列表
	 */
	List<FieldDefine> getFieldList(String tableCode);
}