package com.imooc.dacheche.common;

/**
 * ���������
 * @author Huang Shan
 *
 */
public class OutUtils {
	
	/**
	 * �����Ϣ������̨
	 * @param msg
	 */
	public static synchronized void outMsg(String msg) {
		System.out.println();
		System.out.println(msg);
	}
}
