package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

public class ServerMessage implements Serializable {
	private static final long serialVersionUID = 3346092612678984119L;
	
	/**-----------普通消息-----------*/
	/**
	 * 2000: 登陆成功
	 */
	public static final int LOGIN_OK = 2000;
	/**
	 * 2001: 登陆失败
	 */
	public static final int LOGIN_FAIL = 2000;
	
	/**
	 * 2101: 匹配到一个出租车司机
	 */
	public static final int DRIVER_REQUEST = 2101;
	/**
	 * 2102: 打车请求超时
	 */
	public static final int CALL_TIMEOUT = 2102;
	/**
	 * 2103: 司机请求确认到达目的地
	 */
	public static final int REQUEST_DONE = 2103;
	
	/**
	 * 2201: 乘客列表
	 */
	public static final int ORDER_LIST = 2201;
	/**
	 * 2211: 载客记录
	 */
	public static final int HISTORY = 2211;
	/**
	 * 2212: 载客记录查询失败
	 */
	public static final int HISTORY_EXCEPTION = 2212;
	/**
	 * 2202: 乘客同意搭乘
	 */
	public static final int PASSENGER_ACCEPT = 2202;
	/**
	 * 2203: 乘客拒绝搭乘
	 */
	public static final int PASSENGER_REJECT = 2203;
	/**
	 * 2204: 没找到该搭乘消息
	 */
	public static final int ORDER_NOT_FOUND = 2204;
	/**
	 * 2205: 乘客同意到达终点 
	 */
	public static final int ACCEPT_DONE = 2205;
	/**
	 *  2206: 乘客拒绝到达终点
	 */
	public static final int REJECT_DONE = 2206;
	
	/**
	 * 服务器返回消息状态 
	 */
	private int state;
	private String message;
	private User driver;
	private User passenger;
	private Date sendTime;
	private Object objMsg;

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getDriver() {
		return driver;
	}

	public void setDriver(User driver) {
		this.driver = driver;
	}

	public User getPassenger() {
		return passenger;
	}

	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Object getObjMsg() {
		return objMsg;
	}

	public void setObjMsg(Object objMsg) {
		this.objMsg = objMsg;
	}
}
