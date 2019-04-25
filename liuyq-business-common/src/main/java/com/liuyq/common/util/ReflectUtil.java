package com.liuyq.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工具类-反射帮助类
 * @author yinLiang
 * @version 1.0
 * @date 2015年11月17日 下午5:39:54
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class ReflectUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectUtil.class);

	public static List<?> PRIMITIVE_TYPES = Arrays.asList(new Class[] {
			char.class, short.class, byte.class, int.class, long.class,
			float.class, double.class, boolean.class, Short.class, Byte.class,
			Integer.class, Long.class, Float.class, Double.class,
			Boolean.class, String.class, Date.class });

	public static boolean isPrimitive(Class<?> type) {
		return PRIMITIVE_TYPES.contains(type);
	}

	public static Object invokeGetMethod(Class<?> claszz, Object o, String name) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("get" + RdStringUtil.firstCharUpperCase(name));
			ret = method.invoke(o);
		} catch (Exception e) {
			//LOGGER.error(e.toString(), e);
			//LOGGER.error("claszz:" + claszz + ",name:" + name);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o, String name, Class<?>[] argTypes, Object[] args) {
		Object ret = null;
		try {
			// 非 常量 进行反射
			if (!checkModifiers(claszz, name)) {
				Method method = claszz.getMethod("set" + RdStringUtil.firstCharUpperCase(name), argTypes);
				ret = method.invoke(o, args);
			}
		} catch (Exception e) {
			//LOGGER.error(e.toString(), e);
			LOGGER.error("claszz:" + claszz + ",name:" + name + ",argType:"
					+ argTypes + ",args:" + args);
		}
		return ret;
	}

	public static Object invokeSetMethod(Class<?> claszz, Object o, String name, Class<?> argType, Object args) {
		Object ret = null;
		try {
			// 非 常量 进行反射
			if (!checkModifiers(claszz, name)) {
				Method method = claszz.getMethod("set" + RdStringUtil.firstCharUpperCase(name), new Class[] { argType });
				ret = method.invoke(o, new Object[] { args });
			}
		} catch (Exception e) {
			//LOGGER.error(e.toString(), e);
			LOGGER.error("claszz:" + claszz + ",name:" + name + ",argType:"
					+ argType + ",args:" + args);
		}
		return ret;
	}
	
	/**
	 * 校验参数类型 
	 * 目前只校验是否为 常量
	 * @param claszz
	 * @param name
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean checkModifiers(Class<?> claszz, String name) {
		try {
			Field field = claszz.getField(name);
			if (isConstant(field.getModifiers())) {
				return true;
			}
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		} 
		return false;
	}

	/**
	 * 是否为常量
	 * @param modifiers
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean isConstant(int modifiers) {
		// static 和 final修饰
		if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
			return true;
		} 
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public static Class<?> getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		if ((index >= params.length) || (index < 0)) {
			throw new RuntimeException("你输入的索引" + ((index < 0) ? "不能小于0" : "超出了参数的总数"));
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}

	@SuppressWarnings("rawtypes")
	public static Class getSuperClassGenricType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		Class<?> clazz = object.getClass();
		while (clazz != Object.class) {
			try {
				method = clazz.getDeclaredMethod(methodName, parameterTypes);
			} catch (Exception e) {

			}
			if (method != null) {
				break;
			}
			clazz = clazz.getSuperclass();
		}
		return method;
	}

	public static Map<String, Field> getClassField(Class<?> clazz) {
		Field[] declaredFields = clazz.getDeclaredFields();
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Map<String, Field> superFieldMap = new HashMap<String, Field>();
		for (Field field : declaredFields) {
			fieldMap.put(field.getName(), field);
		}
		if (clazz.getSuperclass() != null) {
			superFieldMap = getClassField(clazz.getSuperclass());
		}
		fieldMap.putAll(superFieldMap);
		return fieldMap;
	}
	
	/**
	 * object 属性名称及属性值组装为 Map，再用Map转Json字符串。
	 * 组装规则：
	 * 只组装String类型，且不为常量的字段，
	 * 组装时若属性值为空或为null，则不加入Json
	 * @param object
	 * @return
	 */
	public static String fieldValueToJson(Object object) {
		Class<?> clazz = object.getClass();
		Field[] fss = new Field[0];
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fs = clazz.getDeclaredFields();
				fss = RdArrayUtil.concat(fss, fs);
			} catch (Exception e) {
				// 这里异常不能抛出去。
				// 如果这里的异常打印或者往外抛，就不会执行clazz = clazz.getSuperclass(),
				// 最后就不会进入到父类中了
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		for (Field f : fss) {
			// 反射对象中String类型，且不为常量的字段
			if (String.class.equals(f.getType()) && !isConstant(f.getModifiers())) {
				String fieldName = f.getName();
				Object o = ReflectUtil.invokeGetMethod(f.getDeclaringClass(), object, fieldName);
				String value = RdStringUtil.isNull(o);
				if (value == "") {
					continue;
				}
				map.put(fieldName, value);
			}
		}
		String str = JSONObject.toJSONString(map);
		return str;
	}
	
	
	/**
	 * object 属性名称及属性值组装为String字符串。
	 * 组装规则：
	 * field.name1=field.value1&field.name2=field.value2 ...
	 * @param object
	 * @return
	 *//*
	public static String fieldValueToString(Object object) {
		Class<?> clazz = object.getClass();
		Field[] fss = new Field[0];
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fs = clazz.getDeclaredFields();
				fss = RdArrayUtil.concat(fss, fs);
			} catch (Exception e) {
				// 这里异常不能抛出去。
				// 如果这里的异常打印或者往外抛，就不会执行clazz = clazz.getSuperclass(),
				// 最后就不会进入到父类中了
			}
		}
		StringBuffer sb = new StringBuffer(50);
		for (Field f : fss) {
			// 反射对象中String类型，且不为常量的字段
			if (String.class.equals(f.getType()) && !isConstant(f.getModifiers())) {
				String fieldName = f.getName();
				Object o = ReflectUtil.invokeGetMethod(f.getDeclaringClass(), object, fieldName);
				String value = RdStringUtil.isNull(o);
				if (value == "") {
					continue;
				}
				sb.append(fieldName + "=" + value + "&");
			}
		}
		LOGGER.info("请求TPP参数："+sb.toString());
		return sb.toString();
	}*/
}
