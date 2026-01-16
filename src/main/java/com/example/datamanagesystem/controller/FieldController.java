package com.example.datamanagesystem.controller;

import com.example.datamanagesystem.common.Result;
import com.example.datamanagesystem.entity.FieldDefine;
import com.example.datamanagesystem.mapper.FieldDataMapper;
import com.example.datamanagesystem.mapper.FieldDefineMapper;
import com.example.datamanagesystem.service.FieldDefineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/field")
public class FieldController {

	// 1. 注入 Service（业务层）
	@Autowired
	private FieldDefineService fieldDefineService;

	// 2. 注入 Mapper 实例（关键！必须注入，不能用类名调用）
	@Autowired
	private FieldDefineMapper fieldDefineMapper; // 实例名：fieldDefineMapper（小写开头）
	@Autowired
	private FieldDataMapper fieldDataMapper;     // 实例名：fieldDataMapper（小写开头）

	/**
	 * 添加字段接口
	 */
	@PostMapping("/add")
	public Result<?> addField(@RequestBody FieldDefine fieldDefine) {
		try {
			if (fieldDefine == null || fieldDefine.getTableCode() == null || fieldDefine.getFieldCode() == null) {
				return Result.error("参数不能为空（tableCode/fieldCode为必填）");
			}
			boolean success = fieldDefineService.addField(fieldDefine);
			return success ? Result.success("字段添加成功") : Result.error("字段编码已存在，添加失败");
		} catch (Exception e) {
			log.error("添加字段失败", e);
			return Result.error("添加失败：" + e.getMessage());
		}
	}

	/**
	 * 查询字段列表接口
	 */
	@GetMapping("/list")
	public Result<?> getFieldList(@RequestParam String tableCode) {
		try {
			List<FieldDefine> fieldList = fieldDefineService.getFieldList(tableCode);
			return Result.success("查询成功", fieldList);
		} catch (Exception e) {
			log.error("查询字段列表失败", e);
			return Result.error("查询失败：" + e.getMessage());
		}
	}

	/**
	 * 删除字段接口（核心修复：全部用实例调用，无静态调用）
	 */
	@DeleteMapping("/delete/{fieldId}")
	public Result<?> deleteField(@PathVariable Long fieldId) {
		// 校验ID
		if (fieldId == null || fieldId <= 0) {
			return Result.error("请传入有效的字段ID（如/api/field/delete/1）");
		}

		try {
			// 修正1：用注入的 fieldDataMapper 实例调用（不是 FieldDataMapper 类名）
			fieldDataMapper.deleteByFieldId(fieldId);

			// 修正2：用注入的 fieldDefineMapper 实例调用（不是 FieldDefineMapper 类名）
			int affectedRows = fieldDefineMapper.deleteById(fieldId);

			if (affectedRows == 0) {
				return Result.error("字段不存在，删除失败");
			}
			return Result.success("字段删除成功");
		} catch (Exception e) {
			log.error("删除字段失败", e);
			return Result.error("删除字段失败：" + e.getMessage());
		}
	}
}