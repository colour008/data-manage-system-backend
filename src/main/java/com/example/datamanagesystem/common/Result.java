package com.example.datamanagesystem.common;

import lombok.Data;

/**
 * 统一接口返回结果类
 */
@Data
public class Result<T> {
	// 响应状态：true=成功，false=失败
	private boolean success;
	// 响应消息
	private String msg;
	// 响应数据
	private T data;

	// 成功响应（无数据）
	public static Result<?> success(String msg) {
		Result<?> result = new Result<>();
		result.setSuccess(true);
		result.setMsg(msg);
		return result;
	}

	// 成功响应（带数据）
	public static <T> Result<T> success(String msg, T data) {
		Result<T> result = new Result<>();
		result.setSuccess(true);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

	// 失败响应
	public static Result<?> error(String msg) {
		Result<?> result = new Result<>();
		result.setSuccess(false);
		result.setMsg(msg);
		return result;
	}
}