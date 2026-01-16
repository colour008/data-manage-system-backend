package com.example.datamanagesystem.controller;

import com.example.datamanagesystem.common.Result;
import com.example.datamanagesystem.entity.DataDTO;
import com.example.datamanagesystem.entity.DataMain;
import com.example.datamanagesystem.mapper.DataMainMapper;
import com.example.datamanagesystem.service.DataManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据增删改查接口
 */
@RestController // 必须标注：让Spring识别为控制器并创建实例
@RequestMapping("/api/data")
@Slf4j
public class DataController {

	// 核心修复：确保@Autowired注解正确，Service被Spring管理
	@Autowired
	private DataManageService dataManageService;

	@Autowired
	private DataMainMapper dataMainMapper; // 新增：注入mapper用于前置校验

	/**
	 * 新增数据（添加异常捕获，返回结构化结果）
	 */
	@PostMapping("/add")
	public Map<String, Object> addData(@RequestBody DataDTO dataDTO) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 参数非空校验
			if (dataDTO == null || dataDTO.getTableCode() == null) {
				result.put("success", false);
				result.put("msg", "业务表标识（tableCode）不能为空");
				return result;
			}
			boolean success = dataManageService.addData(dataDTO);
			result.put("success", success);
			result.put("msg", success ? "新增成功" : "新增失败");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "新增失败：" + e.getMessage());
			e.printStackTrace(); // 测试环境打印异常栈，生产环境删除
		}
		return result;
	}

	/**
	 * 查询数据列表（添加异常捕获，返回结构化结果）
	 */
	@GetMapping("/list")
	public Map<String, Object> getDataList(@RequestParam String tableCode) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Map<String, Object>> dataList = dataManageService.getDataList(tableCode);
			result.put("success", true);
			result.put("data", dataList);
			result.put("msg", "查询成功");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "查询失败：" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 删除数据（添加异常捕获，返回结构化结果）仅保留规范路径：必须传mainId，且为数字
	 */
	@DeleteMapping("/delete/{mainId}")
	public Map<String, Object> deleteData(@PathVariable Long mainId) {
		Map<String, Object> result = new HashMap<>();
		try {
			// 1. 前置校验ID有效性
			if (mainId == null || mainId <= 0) {
				result.put("success", false);
				result.put("msg", "删除失败：ID必须为正整数");
				return result;
			}

			// 2. 校验记录是否存在
			DataMain existMain = dataMainMapper.selectById(mainId);
			if (existMain == null) {
				result.put("success", false);
				result.put("msg", "删除失败：不存在ID为" + mainId + "的记录");
				return result;
			}

			// 3. 执行删除
			boolean success = dataManageService.deleteData(mainId);
			result.put("success", success);
			result.put("msg", success ? "删除成功" : "删除失败");
		} catch (MethodArgumentTypeMismatchException e) {
			// 捕获ID非数字的情况（如delete/abc、delete/null）
			result.put("success", false);
			result.put("msg", "删除失败：ID必须为数字");
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "删除失败：" + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 编辑更新数据
	 * @param mainId 基础数据ID
	 * @param dataDTO 包含更新的字段值
	 * @return 操作结果
	 */
	@PutMapping("/update/{mainId}")
	public Result<?> updateData(
			@PathVariable Long mainId,
			@RequestBody DataDTO dataDTO
	) {
		try {
			boolean success = dataManageService.updateData(mainId, dataDTO);
			if (success) {
				return Result.success("数据修改成功 ✏️");
			} else {
				return Result.error("数据修改失败，请检查参数");
			}
		} catch (Exception e) {
			log.error("修改数据失败", e);
			return Result.error("修改失败：" + e.getMessage());
		}
	}
}