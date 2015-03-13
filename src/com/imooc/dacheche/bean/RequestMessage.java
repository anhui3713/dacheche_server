package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 打车请求消息
 * @author Huang Shan
 *
 */
public class RequestMessage implements Serializable {
	private static final long serialVersionUID = -5341091045136532252L;
	
	/**
	 * 打车消息id
	 */
	private String id;
	/**
	 * 乘客信息
	 */
	private User user;
	/**
	 * 乘客打车消息
	 */
	private String message;
	/**
	 * 发起时间
	 */
	private Date requestTime;
	
	private boolean proccesing = false;
	
	public void setProccesing(boolean proccesing) {
		this.proccesing = proccesing;
	}
	
	public boolean isProccesing() {
		return proccesing;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestMessage other = (RequestMessage) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
