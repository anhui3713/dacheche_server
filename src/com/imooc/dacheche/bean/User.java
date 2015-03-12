package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
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
	 * 用户逐渐信息
	 */
	private String id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 性别
	 */
	private String gender;
	/**
	 * 用户类型 1: 乘客 2: 司机
	 */
	private int type;
	/**
	 * 登陆时间
	 */
	private Date loginTime;
	/**
	 * 个人介绍
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
