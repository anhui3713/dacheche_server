package com.imooc.dacheche.net.proccesors;

import java.sql.SQLException;
import java.util.List;

import com.imooc.dacheche.bean.ClientMessage;
import com.imooc.dacheche.bean.Log;
import com.imooc.dacheche.bean.RequestMessage;
import com.imooc.dacheche.bean.ServerMessage;
import com.imooc.dacheche.bean.User;
import com.imooc.dacheche.common.OutUtils;
import com.imooc.dacheche.dao.LogDao;
import com.imooc.dacheche.net.MessagePoccesor;
import com.imooc.dacheche.net.MessageTransfer;
import com.imooc.dacheche.net.Server;

public class DriverProccesor implements MessagePoccesor {

	private MessageTransfer mt;
	private LogDao logDao = new LogDao();
	
	public DriverProccesor(MessageTransfer mt) {
		super();
		this.mt = mt;
	}

	@Override
	public void proccess(ClientMessage msg) {
		switch(msg.getCommand()) {
			 // 司机请求载客
			 case ClientMessage.GET_ORDER: getOrder(msg); break;
			 // 司机获取历史消息
			 case ClientMessage.GET_HISTORY: requestHistory(msg); break;
			 // 司机请求搭载指定乘客
			 case ClientMessage.REQUEST_ORDER: requestPassenger(msg); break;
			 // 司机请求确认是否到达终点
			 case ClientMessage.REQUEST_DONE: confirmDone(msg); break;
		}
	}

	/**
	 * 司机请求查看载客历史记录
	 * @param msg
	 */
	private void requestHistory(ClientMessage msg) {
		
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.HISTORY);
		try {
			List<Log> logs = logDao.listLogs(mt.getUser().getName());
			sm.setObjMsg(logs);
		} catch (SQLException e) {
			sm.setState(ServerMessage.HISTORY_EXCEPTION);
			OutUtils.outMsg("查询失败:" + e.getMessage());
		}
		
		mt.sendMessage(sm);
	}

	/**
	 * 司机确认已经到达终点
	 * @param msg
	 */
	private void confirmDone(ClientMessage msg) {
		User passenger = msg.getReceiver();
		MessageTransfer tmt = Server.getServer().getMt(passenger.getId());
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.REQUEST_DONE);
		sm.setDriver(mt.getUser());
		
		tmt.sendMessage(sm);
	}
	/**
	 * 司机请求搭乘指定乘客
	 * @param msg
	 */
	private void requestPassenger(ClientMessage msg) {
		// 根据消息id找到对应的用户和消息处理对象
		String id = msg.getMessage();
		RequestMessage rm = Server.getServer().getReqMsg(id);
		if(rm != null) {
			MessageTransfer mp = Server.getServer().getMt(rm.getUser().getId());
			
			// 构造消息通过消息处理对象发送到对应的乘客
			ServerMessage sm = new ServerMessage();
			sm.setDriver(mt.getUser());
			sm.setState(ServerMessage.DRIVER_REQUEST);
			
			mp.sendMessage(sm);
		} else {
			// 返回没找到当前消息
			ServerMessage sm = new ServerMessage();
			sm.setDriver(mt.getUser());
			sm.setState(ServerMessage.ORDER_NOT_FOUND);
			
			mt.sendMessage(sm);
		}
	}

	/**
	 * 司机请求载客
	 * @param msg
	 */
	private void getOrder(ClientMessage msg) {
		RequestMessage[] rms = Server.getServer().listReqMsgs();
		ServerMessage sm = new ServerMessage();
		sm.setObjMsg(rms);
		sm.setState(2201);
		mt.sendMessage(sm);
	}

}
