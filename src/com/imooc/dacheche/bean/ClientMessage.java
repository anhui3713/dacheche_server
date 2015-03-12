package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * �ͻ��˴�����Ϣ����
 * @author Huang Shan
 *
 */

public class ClientMessage implements Serializable {
	private static final long serialVersionUID = -4900192510934028841L;
	
	/**----------------�˿�------------------*/
	/**
	 * 1000:  ��½
	 */
	public static final int LOGIN = 1000;
	/**
	 * 1001: �˳�����
	 */
	public static final int EXIT = 1001;
	/**
	 * 1101: �г�, ���г��⳵
	 */
	public static final int CALL_TAXI = 1101;
	/**
	 * 1102: ����, ����ϵͳƥ��ĳ��⳵˾��
	 */
	public static final int ACCEPT = 1102;
	/**
	 * 1103: �ܾ�, �ܾ�ϵͳƥ��ĳ��⳵˾��
	 */
	public static final int REJECT = 1103;
	/**
	 * 1104: ȷ�ϵ���, ȷ��Ŀ�ĵ��Ѿ�����
	 */
	public static final int ACCEPT_DONE = 1104;
	/**
	 * 1105: δ����, �ܾ�˾���ĵ�������
	 */
	public static final int REJECT_DONE = 1105;
	
	/**-------------------˾��---------------*/
	/**
	 * 1201: �ϰ�, ��ȡ�˿ͽг��б�
	 */
	public static final int GET_ORDER = 1101;
	/**
	 * 1211: ��ѯ��¼, �鿴���ؿ���ʷ��¼
	 */
	public static final int GET_HISTORY = 1211;
	/**
	 * 1202: ����ӵ�, �������ĳλ�˿ͷ���Ľг�����
	 */
	public static final int REQUEST_ORDER = 1202;
	/**
	 * 1203: ����, ���͵���Ŀ�ĵظ��˿�ȷ��
	 */
	public static final int REQUEST_DONE = 1203;
	
	/**
	 * ��Ϣ��������
	 */
	private int command;
	/**
	 * ������
	 */
	private User sender;
	/**
	 * ������
	 */
	private User receiver;
	/**
	 * ������Ϣ
	 */
	private String message;
	/**
	 * ����ʱ��
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
