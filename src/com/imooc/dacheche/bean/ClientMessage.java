package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户端传输消息对象
 * @author Huang Shan
 *
 */

public class ClientMessage implements Serializable {
	private static final long serialVersionUID = -4900192510934028841L;
	
	/**----------------乘客------------------*/
	/**
	 * 1000:  登陆
	 */
	public static final int LOGIN = 1000;
	/**
	 * 1001: 退出程序
	 */
	public static final int EXIT = 1001;
	/**
	 * 1101: 叫车, 呼叫出租车
	 */
	public static final int CALL_TAXI = 1101;
	/**
	 * 1102: 接受, 接受系统匹配的出租车司机
	 */
	public static final int ACCEPT = 1102;
	/**
	 * 1103: 拒绝, 拒绝系统匹配的出租车司机
	 */
	public static final int REJECT = 1103;
	/**
	 * 1104: 确认到达, 确认目的地已经到达
	 */
	public static final int ACCEPT_DONE = 1104;
	/**
	 * 1105: 未到达, 拒绝司机的到达请求
	 */
	public static final int REJECT_DONE = 1105;
	
	/**-------------------司机---------------*/
	/**
	 * 1201: 上班, 获取乘客叫车列表
	 */
	public static final int GET_ORDER = 1101;
	/**
	 * 1211: 查询记录, 查看待载客历史记录
	 */
	public static final int GET_HISTORY = 1211;
	/**
	 * 1202: 请求接单, 请求接受某位乘客发起的叫车服务
	 */
	public static final int REQUEST_ORDER = 1202;
	/**
	 * 1203: 到达, 发送到达目的地给乘客确认
	 */
	public static final int REQUEST_DONE = 1203;
	
	/**
	 * 消息命令类型
	 */
	private int command;
	/**
	 * 发送者
	 */
	private User sender;
	/**
	 * 接受者
	 */
	private User receiver;
	/**
	 * 具体消息
	 */
	private String message;
	/**
	 * 发送时间
	 */
	private Date sendTime;
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public User getSender() {
		return sender;
	}
	public void setSender(User sender) {
		this.sender = sender;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public User getReceiver() {
		return receiver;
	}
	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
}
