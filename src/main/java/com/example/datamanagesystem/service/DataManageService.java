package com.example.datamanagesystem.service;

import com.example.datamanagesystem.entity.DataDTO;

import java.util.List;
import java.util.Map;

public interface DataManageService {
	/**
	 * 新增数据（含动态字段）
	 */
	boolean addData(DataDTO dataDTO);

	/**
	 * 查询业务表所有数据（含动态字段值）
	 */
	List<Map<String, Object>> getDataList(String tableCode);

	/**
	 * 删除数据（级联删除动态字段值）
	 */
	boolean deleteData(Long mainId);

	/**
	 * 编辑更新数据（核心新增）
	 */
	boolean updateData(Long mainId, DataDTO dataDTO);
}