package com.imooc.dacheche.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.imooc.dacheche.bean.Log;

/**
 * 打车信息数据访问类
 * @author HuangShan
 *
 */
public class LogDao {
	
	/**
	 * 添加日志到数据库
	 * @param log 被添加的日志
	 * @throws SQLException 数据库异常
	 */
	public void addLog(Log log) throws SQLException {
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dacheche", "root", "");
			conn.setAutoCommit(false);
			pst = conn.prepareStatement("insert into t_log(driver,passenger,end_time,remark) values(?, ?, ?, ?)");
			pst.setString(1, log.getDriver());
			pst.setString(2, log.getPassenger());
			pst.setDate(3, new java.sql.Date(log.getEndTime().getTime()));
			pst.setString(4, log.getRemark());
			
			pst.executeUpdate();
			
			conn.commit();
		} catch (ClassNotFoundException e) {
			throw new SQLException("未找到数据库驱动", e);
		} finally {
			if(pst != null) {
				pst.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
	
	/**
	 * 查询指定司机所有的载客记录
	 * @param driver 将要查询的司机
	 * @return 查询到的结果
	 * @throws SQLException 数据库异常
	 */
	public List<Log> listLogs(String driver) throws SQLException {
		
		List<Log> logs = new ArrayList<Log>();
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet res = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dacheche", "root", "");
			pst = conn.prepareStatement("select * from t_log where driver=?");
			
			pst.setString(1, driver);
			res = pst.executeQuery();
			
			while(res.next()) {
				Log log = new Log();
				log.setId(res.getInt("id"));
				log.setDriver(res.getString("driver"));
				log.setPassenger(res.getString("passenger"));
				log.setRemark(res.getString("remark"));
				log.setEndTime(res.getDate("end_time"));
				
				logs.add(log);
			}
			
		} catch (ClassNotFoundException e) {
			throw new SQLException("未找到数据库驱动", e);
		} finally {
			if(pst != null) {
				pst.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		
		return logs;
	}

}
