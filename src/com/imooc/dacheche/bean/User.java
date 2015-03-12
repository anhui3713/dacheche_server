package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * �û���Ϣ
 * 
 * @author Huang Shan
 *
 */
/**
 * @author Huang Shan
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = 7608975268016405681L;
	
	/**
	 * �û�����Ϣ
	 */
	private String id;
	/**
	 * ����
	 */
	private String name;
	/**
	 * �Ա�
	 */
	private String gender;
	/**
	 * �û����� 1: �˿� 2: ˾��
	 */
	private int type;
	/**
	 * ��½ʱ��
	 */
	private Date loginTime;
	/**
	 * ���˽���
	 */
	private String remark;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getGender() {
		return gender;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
