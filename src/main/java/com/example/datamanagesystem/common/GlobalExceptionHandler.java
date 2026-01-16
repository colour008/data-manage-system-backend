package com.example.datamanagesystem.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 处理参数类型转换异常（如delete/null、delete/abc）
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("msg", "删除失败：ID必须为数字，当前传入值：" + e.getValue());
		return result;
	}

	// 处理路径不存在异常（如delete/、delete）
	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String, Object> handleNoResourceFoundException(NoResourceFoundException e) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("msg", "删除失败：请传入有效的数据ID（如/api/data/delete/1）");
		return result;
	}

	// 通用异常兜底
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Map<String, Object> handleCommonException(Exception e) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", false);
		result.put("msg", "操作失败：" + e.getMessage());
		e.printStackTrace();
		return result;
	}
}