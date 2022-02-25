package com.desaysv.edi.infrastructure.system;

import org.springframework.data.domain.Page;

import java.util.HashMap;

/**
 * 操作消息提醒
 */
public class R extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 状态码
	 */
	public static final String CODE_TAG = "code";
	/**
	 * 返回内容
	 */
	public static final String MESSAGE_TAG = "message";
	/**
	 * 数据对象
	 */
	public static final String DATA_TAG = "data";
	
	public static final int SUCCESS = 200,
			ERROR = 500;
	
	/**
	 * 初始化一个新创建的 R 对象，使其表示一个空消息。
	 */
	public R() {
	}
	
	/**
	 * 初始化一个新创建的 R 对象
	 *
	 * @param code 状态码
	 * @param message  返回内容
	 */
	public R(int code, String message) {
		super.put(CODE_TAG, code);
		super.put(MESSAGE_TAG, message);
	}
	
	/**
	 * 初始化一个新创建的 R 对象
	 *
	 * @param code 状态码
	 * @param message  返回内容
	 * @param data 数据对象
	 */
	public R(int code, String message, Object data) {
		super.put(CODE_TAG, code);
		super.put(MESSAGE_TAG, message);
		if (data != null) {
			super.put(DATA_TAG, data);
		}
	}
	
	/**
	 * 返回成功消息
	 *
	 * @return 成功消息
	 */
	public static R success() {
		return R.success("操作成功");
	}
	
	/**
	 * 返回成功数据
	 *
	 * @return 成功消息
	 */
	public static R success(Object data) {
		return R.success("操作成功", data);
	}
	
	/**
	 * 返回成功消息
	 *
	 * @param message 返回内容
	 * @return 成功消息
	 */
	public static R success(String message) {
		return R.success(message, null);
	}
	
	/**
	 * 返回成功消息
	 *
	 * @param message  返回内容
	 * @param data 数据对象
	 * @return 成功消息
	 */
	public static R success(String message, Object data) {
		return new R(SUCCESS, message, data);
	}
	
	/**
	 * 返回错误消息
	 *
	 * @return
	 */
	public static R error() {
		return R.error("操作失败");
	}
	
	/**
	 * 返回错误消息
	 *
	 * @param message 返回内容
	 * @return 警告消息
	 */
	public static R error(String message) {
		return R.error(message, null);
	}
	
	/**
	 * 返回错误消息
	 *
	 * @param message  返回内容
	 * @param data 数据对象
	 * @return 警告消息
	 */
	public static R error(String message, Object data) {
		return new R(ERROR, message, data);
	}
	
	/**
	 * 返回错误消息
	 *
	 * @param code 状态码
	 * @param message  返回内容
	 * @return 警告消息
	 */
	public static R error(int code, String message) {
		return new R(code, message, null);
	}
	
	/**
	 * 方便链式调用
	 *
	 * @param page  分页对象
	 * @return 数据对象
	 */
	public static R page(Page page) {
		return success().put("rows", page.getContent()).put("total", page.getTotalElements());
	}
	
	/**
	 * 方便链式调用
	 *
	 * @param key   键
	 * @param value 值
	 * @return 数据对象
	 */
	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
