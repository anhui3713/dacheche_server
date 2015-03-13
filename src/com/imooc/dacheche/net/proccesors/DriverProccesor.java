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
			 // ˾�������ؿ�
			 case ClientMessage.GET_ORDER: getOrder(msg); break;
			 // ˾����ȡ��ʷ��Ϣ
			 case ClientMessage.GET_HISTORY: requestHistory(msg); break;
			 // ˾���������ָ���˿�
			 case ClientMessage.REQUEST_ORDER: requestPassenger(msg); break;
			 // ˾������ȷ���Ƿ񵽴��յ�
			 case ClientMessage.REQUEST_DONE: confirmDone(msg); break;
		}
	}

	/**
	 * ˾������鿴�ؿ���ʷ��¼
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
			OutUtils.outMsg("��ѯʧ��:" + e.getMessage());
		}
		
		mt.sendMessage(sm);
	}

	/**
	 * ˾��ȷ���Ѿ������յ�
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
	 * ˾��������ָ���˿�
	 * @param msg
	 */
	private void requestPassenger(ClientMessage msg) {
		// ������Ϣid�ҵ���Ӧ���û�����Ϣ�������
		String id = msg.getMessage();
		RequestMessage rm = Server.getServer().getReqMsg(id);
		if(rm != null) {
			MessageTransfer mp = Server.getServer().getMt(rm.getUser().getId());
			
			// ������Ϣͨ����Ϣ��������͵���Ӧ�ĳ˿�
			ServerMessage sm = new ServerMessage();
			sm.setDriver(mt.getUser());
			sm.setState(ServerMessage.DRIVER_REQUEST);
			
			mp.sendMessage(sm);
		} else {
			// ����û�ҵ���ǰ��Ϣ
			ServerMessage sm = new ServerMessage();
			sm.setDriver(mt.getUser());
			sm.setState(ServerMessage.ORDER_NOT_FOUND);
			
			mt.sendMessage(sm);
		}
	}

	/**
	 * ˾�������ؿ�
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
