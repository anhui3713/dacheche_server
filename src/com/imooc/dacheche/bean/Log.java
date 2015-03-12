package com.imooc.dacheche.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ����־��Ϣ  <br />
 * DDL: <br />
 * <pre>
 * create table t_log( 
 * 	id int primary key auto_increment, 
 * 	driver varchar(20), 
 * 	passenger varchar(20), 
 * 	startTime datetime, 
 * 	remark varchar(200)
 * );
 * </pre>
 * @author Huang Shan
 *
 */
public class Log implements Serializable {
	private static final long serialVersionUID = 3967618199657912401L;
	
	/**
	 * ����
	 */
	private Integer id;
	/**
	 * ˾��
	 */
	private String driver;
	/**
	 * �˿�
	 */
	private String passenger;
	/**
	 * ���ʱ��
	 */
	private Date endTime;
	/**
	 * ����
	 */
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
