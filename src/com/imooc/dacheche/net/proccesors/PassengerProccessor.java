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
			 // �˿������
			 case ClientMessage.CALL_TAXI: getCar(msg); break;
			 
			 // �˿ͽ��ܻ�ܾ����ָ��˾���ĳ�
			 case ClientMessage.ACCEPT:
			 case ClientMessage.REJECT: passengerConfig(msg); break;
			 
			 // �˿�ȷ�ϵ���
			 case ClientMessage.ACCEPT_DONE: done(msg);  break;
			 // �˿ͷ񶨵����յ�
			 case ClientMessage.REJECT_DONE: notDone(msg); break;
		}
	}

	/**
	 * �˿ͷ񶨵����յ�
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
	 *  �˿�ȷ�ϵ���
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
			OutUtils.outMsg("��ӵ����ݿ�ʧ��:" + e.getMessage());
		}
	}

	/**
	 * �˿�ȷ���Ƿ���
	 * @param msg
	 */
	private void passengerConfig(ClientMessage msg) {
		User driver = msg.getReceiver();
		MessageTransfer tmt = Server.getServer().getMt(driver.getId());
		
		ServerMessage sm = new ServerMessage();
		sm.setState(msg.getCommand() == ClientMessage.ACCEPT ? 
				ServerMessage.PASSENGER_ACCEPT : ServerMessage.PASSENGER_REJECT);
		sm.setPassenger(mt.getUser());
		
		// ����˿ͽ��ܴ�˸�˾���ĳ�,�򽫴���б��еĸ���Ϣɾ��
		if(msg.getCommand() == ClientMessage.ACCEPT) {
			Server.getServer().removeReqMsg(mt.getUser().getId());
		}
		
		tmt.sendMessage(sm);
	}

	
	/**
	 * �˿�����г�
	 * @param msg
	 */
	private void getCar(ClientMessage msg) {
		// ����һ������Ϣ
		final RequestMessage rm = new RequestMessage();
		rm.setId(mt.getUser().getId());
		rm.setMessage(msg.getMessage());
		rm.setUser(mt.getUser());
		rm.setRequestTime(new Date());
		
		// ��ӵ���Ϣ�б���
		Server.getServer().addReqMsg(rm);
		
		/*
		 * һ����֮������Ϣ�Ƿ�������Ϣ�б�
		 * ���������֤����ʱ
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
