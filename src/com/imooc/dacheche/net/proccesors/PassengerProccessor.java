package com.imooc.dacheche.net.proccesors;

import java.sql.SQLException;
import java.util.Date;

import com.imooc.dacheche.bean.ClientMessage;
import com.imooc.dacheche.bean.Log;
import com.imooc.dacheche.bean.RequestMessage;
import com.imooc.dacheche.bean.ServerMessage;
import com.imooc.dacheche.bean.User;
import com.imooc.dacheche.common.OutUtils;
import com.imooc.dacheche.common.Utils;
import com.imooc.dacheche.dao.LogDao;
import com.imooc.dacheche.net.MessagePoccesor;
import com.imooc.dacheche.net.MessageTransfer;
import com.imooc.dacheche.net.Server;

public class PassengerProccessor implements MessagePoccesor {

	private MessageTransfer mt;
	private LogDao logDao = new LogDao();
	
	public PassengerProccessor(MessageTransfer mt) {
		super();
		this.mt = mt;
	}

	@Override
	public void proccess(ClientMessage msg) {
		switch(msg.getCommand()) {
			 // 乘客请求打车
			 case ClientMessage.CALL_TAXI: getCar(msg); break;
			 
			 // 乘客接受或拒绝搭乘指定司机的车
			 case ClientMessage.ACCEPT:
			 case ClientMessage.REJECT: passengerConfig(msg); break;
			 
			 // 乘客确认到达
			 case ClientMessage.ACCEPT_DONE: done(msg);  break;
			 // 乘客否定到达终点
			 case ClientMessage.REJECT_DONE: notDone(msg); break;
		}
	}

	/**
	 * 乘客否定到达终点
	 * @param msg
	 */
	private void notDone(ClientMessage msg) {
		User driver = msg.getReceiver();
		MessageTransfer tmt = Server.getServer().getMt(driver.getId());
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.REJECT_DONE);
		sm.setPassenger(mt.getUser());
		
		tmt.sendMessage(sm);
	}
	
	/**
	 *  乘客确认到达
	 * @param msg
	 */
	private void done(ClientMessage msg) {
		User driver = msg.getReceiver();
		MessageTransfer tmt = Server.getServer().getMt(driver.getId());
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.ACCEPT_DONE);
		sm.setPassenger(mt.getUser());
		
		tmt.sendMessage(sm);
		
		Log log = new Log();
		log.setDriver(driver.getName());
		log.setPassenger(mt.getUser().getName());
		log.setEndTime(new Date());
		
		try {
			logDao.addLog(log);
		} catch (SQLException e) {
			// e.printStackTrace();
			OutUtils.outMsg("添加到数据库失败:" + e.getMessage());
		}
	}

	/**
	 * 乘客确认是否搭乘
	 * @param msg
	 */
	private void passengerConfig(ClientMessage msg) {
		User driver = msg.getReceiver();
		MessageTransfer tmt = Server.getServer().getMt(driver.getId());
		
		ServerMessage sm = new ServerMessage();
		sm.setState(msg.getCommand() == ClientMessage.ACCEPT ? 
				ServerMessage.PASSENGER_ACCEPT : ServerMessage.PASSENGER_REJECT);
		sm.setPassenger(mt.getUser());
		
		// 如果乘客接受搭乘该司机的车,则将搭乘列表中的该消息删除
		if(msg.getCommand() == ClientMessage.ACCEPT) {
			Server.getServer().removeReqMsg(mt.getUser().getId());
		}
		
		tmt.sendMessage(sm);
	}

	
	/**
	 * 乘客请求叫车
	 * @param msg
	 */
	private void getCar(ClientMessage msg) {
		// 构造一个打车消息
		final RequestMessage rm = new RequestMessage();
		rm.setId(mt.getUser().getId());
		rm.setMessage(msg.getMessage());
		rm.setUser(mt.getUser());
		rm.setRequestTime(new Date());
		
		// 添加到消息列表中
		Server.getServer().addReqMsg(rm);
		
		/*
		 * 一分钟之后检查消息是否仍在消息列表
		 * 如果还在则证明超时
		 */
		Utils.delay(new Runnable() {
			public void run() {
				if(Server.getServer().containsReqMsg(rm)) {
					ServerMessage sm = new ServerMessage();
					sm.setState(ServerMessage.CALL_TIMEOUT);
					mt.sendMessage(sm);
					Server.getServer().removeReqMsg(rm.getId());
				}
			}
		}, 60000);
	}
}
