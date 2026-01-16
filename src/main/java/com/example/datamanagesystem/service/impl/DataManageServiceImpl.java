package com.example.datamanagesystem.service.impl;

import com.example.datamanagesystem.entity.DataDTO;
import com.example.datamanagesystem.entity.DataMain;
import com.example.datamanagesystem.entity.FieldData;
import com.example.datamanagesystem.entity.FieldDefine;
import com.example.datamanagesystem.mapper.DataMainMapper;
import com.example.datamanagesystem.mapper.FieldDataMapper;
import com.example.datamanagesystem.mapper.FieldDefineMapper;
import com.example.datamanagesystem.service.DataManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DataManageServiceImpl implements DataManageService {

	@Autowired
	private DataMainMapper dataMainMapper;

	@Autowired
	private FieldDefineMapper fieldDefineMapper;

	@Autowired
	private FieldDataMapper fieldDataMapper;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addData(DataDTO dataDTO) {
		// 1. 参数校验
		if (dataDTO == null || dataDTO.getTableCode() == null) {
			log.error("新增数据失败：参数为空（tableCode必填）");
			return false;
		}

		// 2. 新增基础数据
		DataMain dataMain = new DataMain();
		dataMain.setTableCode(dataDTO.getTableCode());
		int mainResult = dataMainMapper.insert(dataMain);
		if (mainResult <= 0) {
			log.error("新增数据失败：data_main插入失败");
			return false;
		}
		Long mainId = dataMain.getId();
		if (mainId == null || mainId <= 0) {
			log.error("新增数据失败：mainId无效，值为{}", mainId);
			return false;
		}
		log.info("插入data_main成功，mainId={}", mainId);

		// 3. 批量新增动态字段值
		List<FieldDefine> fieldList = fieldDefineMapper.selectByTableCode(dataDTO.getTableCode());
		if (fieldList.isEmpty()) {
			log.info("业务表{}暂无字段定义，无需插入动态字段值", dataDTO.getTableCode());
			return true;
		}

		List<FieldData> fieldDataList = new ArrayList<>();
		for (FieldDefine field : fieldList) {
			String fieldValue = dataDTO.getFieldValues() == null ? null : dataDTO.getFieldValues().get(field.getFieldCode());
			// 必填字段校验
			if (field.getIsRequired() == 1 && (fieldValue == null || fieldValue.trim().isEmpty())) {
				log.error("新增数据失败：字段{}为必填项", field.getFieldCode());
				throw new RuntimeException("字段" + field.getFieldCode() + "为必填项"); // 触发事务回滚
			}
			if (fieldValue == null) {
				continue;
			}
			FieldData fieldData = new FieldData();
			fieldData.setMainId(mainId);
			fieldData.setFieldId(field.getId());
			fieldData.setFieldValue(fieldValue.trim());
			fieldDataList.add(fieldData);
		}

		if (!fieldDataList.isEmpty()) {
			int fieldResult = fieldDataMapper.batchInsert(fieldDataList);
			if (fieldResult <= 0) {
				log.error("新增数据失败：动态字段值插入失败");
				return false;
			}
		}
		return true;
	}

	@Override
	public List<Map<String, Object>> getDataList(String tableCode) {
		// 参数校验
		if (tableCode == null || tableCode.trim().isEmpty()) {
			log.error("查询数据失败：tableCode为空");
			return new ArrayList<>();
		}

		// 1. 查询基础数据列表
		List<DataMain> mainList = dataMainMapper.selectByTableCode(tableCode);
		if (mainList.isEmpty()) {
			log.info("业务表{}暂无基础数据", tableCode);
			return new ArrayList<>();
		}

		// 2. 查询字段定义列表
		List<FieldDefine> fieldList = fieldDefineMapper.selectByTableCode(tableCode);

		List<Map<String, Object>> resultList = new ArrayList<>();
		for (DataMain main : mainList) {
			Map<String, Object> dataMap = new HashMap<>();
			dataMap.put("id", main.getId()); // 基础数据ID

			// 3. 查询该条数据的动态字段值
			List<FieldData> fieldDataList = fieldDataMapper.selectByMainId(main.getId());

			// 4. 拼接字段值（key=fieldCode，value=fieldValue）
			for (FieldDefine field : fieldList) {
				String fieldValue = "";
				for (FieldData fieldData : fieldDataList) {
					if (fieldData.getFieldId().equals(field.getId())) {
						fieldValue = fieldData.getFieldValue();
						break;
					}
				}
				dataMap.put(field.getFieldCode(), fieldValue);
			}
			resultList.add(dataMap);
		}
		return resultList;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteData(Long mainId) {
		// 1. 参数校验
		if (mainId == null || mainId <= 0) {
			log.error("删除数据失败：mainId无效，值为{}", mainId);
			return false;
		}

		// 2. 校验基础数据是否存在
		DataMain existMain = dataMainMapper.selectById(mainId);
		if (existMain == null) {
			log.error("删除数据失败：data_main表中无id={}的记录", mainId);
			return false;
		}

		// 3. 级联删除动态字段值
		fieldDataMapper.deleteByMainId(mainId);

		// 4. 删除基础数据
		int deleteCount = dataMainMapper.deleteById(mainId);
		if (deleteCount > 0) {
			log.info("删除数据成功：data_main表中id={}的记录已删除", mainId);
		} else {
			log.error("删除数据失败：data_main表中id={}的记录删除失败", mainId);
		}
		return deleteCount > 0;
	}

	/**
	 * 编辑更新数据（核心新增方法）
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateData(Long mainId, DataDTO dataDTO) {
		// 1. 参数校验
		if (mainId == null || mainId <= 0) {
			log.error("更新数据失败：mainId无效，值为{}", mainId);
			return false;
		}
		if (dataDTO == null || dataDTO.getTableCode() == null || dataDTO.getFieldValues() == null) {
			log.error("更新数据失败：参数为空（tableCode/fieldValues必填）");
			return false;
		}

		// 2. 校验基础数据是否存在
		DataMain existMain = dataMainMapper.selectById(mainId);
		if (existMain == null) {
			log.error("更新数据失败：data_main表中无id={}的记录", mainId);
			return false;
		}

		// 3. 获取该业务表的字段定义
		List<FieldDefine> fieldList = fieldDefineMapper.selectByTableCode(dataDTO.getTableCode());
		if (fieldList.isEmpty()) {
			log.error("更新数据失败：业务表{}暂无字段定义", dataDTO.getTableCode());
			return false;
		}

		// 4. 遍历字段，更新/新增动态字段值
		Map<String, String> fieldValues = dataDTO.getFieldValues();
		for (FieldDefine field : fieldList) {
			String fieldCode = field.getFieldCode();
			String fieldValue = fieldValues.get(fieldCode);

			// 校验必填字段
			if (field.getIsRequired() == 1 && (fieldValue == null || fieldValue.trim().isEmpty())) {
				log.error("更新数据失败：字段{}为必填项", fieldCode);
				throw new RuntimeException("字段" + field.getFieldName() + "为必填项"); // 触发事务回滚
			}

			// 构建字段数据对象
			FieldData fieldData = new FieldData();
			fieldData.setMainId(mainId);
			fieldData.setFieldId(field.getId());
			fieldData.setFieldValue(fieldValue == null ? "" : fieldValue.trim());

			// 先尝试更新，若更新行数为0（说明该字段值不存在），则新增
			int updateCount = fieldDataMapper.updateByMainIdAndFieldId(fieldData);
			if (updateCount <= 0) {
				log.info("字段{}的值不存在，执行新增逻辑", fieldCode);
				fieldDataMapper.batchInsert(List.of(fieldData)); // 批量插入单条数据
			}
		}

		log.info("更新数据成功：mainId={}，业务表={}", mainId, dataDTO.getTableCode());
		return true;
	}
}