package com.example.datamanagesystem.service.impl;

import com.example.datamanagesystem.entity.FieldDefine;
import com.example.datamanagesystem.mapper.FieldDefineMapper;
import com.example.datamanagesystem.service.FieldDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service; // 必须加，标识为Spring服务类

import java.util.List;

@Service // 核心：确保该注解存在，否则Spring不会管理这个Service
public class FieldDefineServiceImpl implements FieldDefineService {

	@Autowired
	private FieldDefineMapper fieldDefineMapper;

	@Override
	public boolean addField(FieldDefine fieldDefine) {
		// 校验字段编码是否重复
		FieldDefine existField = fieldDefineMapper.selectByCode(fieldDefine.getTableCode(), fieldDefine.getFieldCode());
		if (existField != null) {
			return false; // 字段编码已存在
		}
		return fieldDefineMapper.insert(fieldDefine) > 0;
	}

	@Override
	public List<FieldDefine> getFieldList(String tableCode) {
		return fieldDefineMapper.selectByTableCode(tableCode);
	}
}