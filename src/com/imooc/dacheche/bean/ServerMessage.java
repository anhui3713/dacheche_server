package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

public class ServerMessage implements Serializable {
	private static final long serialVersionUID = 3346092612678984119L;
	
	/**-----------��ͨ��Ϣ-----------*/
	/**
	 * 2000: ��½�ɹ�
	 */
	public static final int LOGIN_OK = 2000;
	/**
	 * 2001: ��½ʧ��
	 */
	public static final int LOGIN_FAIL = 2000;
	
	/**
	 * 2101: ƥ�䵽һ�����⳵˾��
	 */
	public static final int DRIVER_REQUEST = 2101;
	/**
	 * 2102: ������ʱ
	 */
	public static final int CALL_TIMEOUT = 2102;
	/**
	 * 2103: ˾������ȷ�ϵ���Ŀ�ĵ�
	 */
	public static final int REQUEST_DONE = 2103;
	
	/**
	 * 2201: �˿��б�
	 */
	public static final int ORDER_LIST = 2201;
	/**
	 * 2211: �ؿͼ�¼
	 */
	public static final int HISTORY = 2211;
	/**
	 * 2212: �ؿͼ�¼��ѯʧ��
	 */
	public static final int HISTORY_EXCEPTION = 2212;
	/**
	 * 2202: �˿�ͬ����
	 */
	public static final int PASSENGER_ACCEPT = 2202;
	/**
	 * 2203: �˿;ܾ����
	 */
	public static final int PASSENGER_REJECT = 2203;
	/**
	 * 2204: û�ҵ��ô����Ϣ
	 */
	public static final int ORDER_NOT_FOUND = 2204;
	/**
	 * 2205: �˿�ͬ�⵽���յ� 
	 */
	public static final int ACCEPT_DONE = 2205;
	/**
	 *  2206: �˿;ܾ������յ�
	 */
	public static final int REJECT_DONE = 2206;
	
	/**
	 * ������������Ϣ״̬ 
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
