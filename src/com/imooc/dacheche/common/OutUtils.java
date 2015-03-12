package com.imooc.dacheche.common;

/**
 * 输出工具类
 * @author Huang Shan
 *
 */
public class OutUtils {
	
	/**
	 * 输出消息到控制台
	 * @param msg
	 */
	public static synchronized void outMsg(String msg) {
		System.out.println();
		System.out.println(msg);
	}
}
