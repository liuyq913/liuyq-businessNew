package com.liuyq.common.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 工具类-数字处理
 * @author yinLiang
 * @version 1.0
 * @date 2015年11月17日 下午5:33:26
 * Copyright 杭州融都科技股份有限公司 统一资金接入系统 UFX  All Rights Reserved
 * 官方网站：www.erongdu.com
 * 研发中心：rdc@erongdu.com
 * 未经授权不得进行修改、复制、出售及商业使用
 */
public class RdNumberUtil {
	
	public static double format(double d,String format){
		DecimalFormat df = new DecimalFormat(format); 
		String ds=df.format(d);
		return Double.parseDouble(ds);
	}

	public static double format2(double d){
		return BigDecimalUtil.decimal(d, 2);
	}
	
	public static String format2Str(double d){
		DecimalFormat df = new DecimalFormat("#####0.00");
		return df.format(BigDecimalUtil.decimal(d, 2));
	}
	
	public static String format3Str(double d){
		DecimalFormat df = new DecimalFormat("#,##0.00");
		return df.format(BigDecimalUtil.decimal(d, 2));
	}
	
	public static String format5Str(double d){
		DecimalFormat df = new DecimalFormat("0.00000");
		return df.format(BigDecimalUtil.decimal(d, 5));
	}
	
	public static double format4(double d){
		return BigDecimalUtil.decimal(d, 4);
	}
	
	
	public static double format6(double d){
		return BigDecimalUtil.decimal(d, 6);
	}
	
	public static int compare(double x,double y){
		BigDecimal val1=new BigDecimal(x);
		BigDecimal val2=new BigDecimal(y);
		return val1.compareTo(val2);
	}
	
	/**
	 * @param d
	 * @param len
	 * @return
	 */
	public static double ceil(double d,int len){
		String str=Double.toString(d);
		int a=str.indexOf(".");
		if(a+3>str.length()){
			a=str.length();
		}else{
			a=a+3;
		}
		str=str.substring(0, a);
		return Double.parseDouble(str);
	}
	
	public static double ceil(double d){
		return ceil(d,2);
	}
	
	/**
	 * 去除数字的科学计数法
	 * @param d
	 * @return
	 */
	public static String format(double d) {
		if (d < 10000000) {
			return d + "";
		}
		java.text.NumberFormat nf = java.text.NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		return nf.format(d);
	}
	
	/**
	 * String → long
	 * @param str
	 * @return
	 */
	public static long getLong(String str) {
		if (StringValidUtil.isBlank(str)) {
			return 0L;
		}
		long ret = 0;
		try {
			ret = Long.parseLong(str);
		} catch (NumberFormatException e) {
			ret = 0;
		}
		return ret;
	}
	
	/**
	 * String → int
	 * @param str
	 * @return
	 */
	public static int getInt(String str) {
		if (StringValidUtil.isBlank(str)) {
			return 0;
		}
		int ret = 0;
		try {
			ret = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			ret = 0;
		}
		return ret;
	}
	
	/**
	 * String → double
	 * @param str
	 * @return
	 */
	public static double getDouble(String str) {
		if (StringValidUtil.isBlank(str)) {
			return 0.0;
		}
		double ret = 0.0;
		try {
			ret = Double.parseDouble(str);
		} catch (Exception e) {
			ret = 0.0;
		}
		return ret;
	}
}
