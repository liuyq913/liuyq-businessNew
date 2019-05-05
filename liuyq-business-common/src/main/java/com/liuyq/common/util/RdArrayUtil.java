package com.liuyq.common.util;

import java.util.Arrays;

/**
 * 数组操作工具类
 * @author yinliang
 * @version 1.0
 * @date 2015年12月10日 下午3:48:29
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class RdArrayUtil {

	/**
	 * 数组合并
	 * 
	 * @param first 第一个数组
	 * @param second 第二个数组
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
}
