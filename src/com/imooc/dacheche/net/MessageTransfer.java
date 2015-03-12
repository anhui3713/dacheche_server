package com.imooc.dacheche.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

import com.imooc.dacheche.bean.ClientMessage;
import com.imooc.dacheche.bean.ServerMessage;
import com.imooc.dacheche.bean.User;
import com.imooc.dacheche.common.OutUtils;
import com.imooc.dacheche.exception.ReceiveMessageException;
import com.imooc.dacheche.net.proccesors.DriverProccesor;
import com.imooc.dacheche.net.proccesors.PassengerProccessor;
/**
 * ��������Ϣ�����߳�
 * @author Huang Shan
 *
 */
public class MessageTransfer implements Runnable {

	/**
	 * ������Ϊÿ���̶߳�������id,�����ÿ��������й���
	 */
	private String id;
	/**
	 * �߳��Ƿ����ڴ���
	 */
	private boolean running = false;
	/**
	 * ���̴߳������Ϣ�׽��ֶ���
	 */
	private Socket socket;
	/**
	 * ��Կͻ����׽��ֵĶ���������,���ڶ�ȡ�Է����͵���Ϣ
	 */
	private ObjectInputStream ois;
	/**
	 * ��Կͻ����׽��ֵ������,������Է�������Ϣ
	 */
	private ObjectOutputStream oos;
	
	/**
	 * ��ǰ��½�û�����Ϣ
	 */
	private User user;
	/**
	 * ��Ӧ����Ϣ������
	 */
	private MessagePoccesor mp;
	
	/**
	 * Ψһ���췽��,���봫�뱾�̴߳�����׽��ֶ���
	 * @param socket
	 * @throws ReceiveMessageException 
	 */
	public MessageTransfer(String id,Socket socket) throws ReceiveMessageException {
		this.id = id;
		this.socket = socket;
		try {
			/*
			 * ���������ȴ�����Կͻ��˵Ķ��������
			 * �����Ϸ���һ���ն���,���ڴ�ͨ������ͨ��
			 * ��ֹ���������ʱ���ղ���header��Ϣ����������
			 */
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(null);
			oos.flush();
			// ������Ե�ǰ�ͻ��˵Ķ���������
			this.ois = new ObjectInputStream(socket.getInputStream());
			// ����Ϊ����״̬
			running = true;
		} catch (IOException e) {
			// �����õ�ǰ�߳�Ϊֹͣ״̬
			stop();
			throw new ReceiveMessageException("����ͨ�Ŷ������", e);
		}
	}

	/**
	 * ������Ϣ
	 * @param sm
	 * @throws IOException
	 */
	public void sendMessage(ServerMessage sm) {
		try {
			sm.setSendTime(new Date());
			oos.writeObject(sm);
			oos.flush();
		} catch (IOException e) {
			OutUtils.outMsg("������Ϣʧ��.");
//			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ��ǰ�û���Ϣ
	 * @return
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * �߳�ִ�еķ���,���ڽ��տͻ�����������
	 */
	public void run() {
		while(running) {
			try {
				// ���տͻ��˷��͹����� ��Ϣ
				ClientMessage msg = (ClientMessage) ois.readObject();
				
				// ��ȡ�ͻ�����Ϣ,�����зַ�
				switch(msg.getCommand()) {
					 // ��½
					 case ClientMessage.LOGIN: login(msg); break;
				     // �˳�
					 case ClientMessage.EXIT: logout(msg); break;
					 // ���� ���ɶ�Ӧ����Ϣ������������
					 default: mp.proccess(msg);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				// ���յ��޷�ʶ�����Ϣ
			} catch (IOException e) {
				/*
				 *  ���������쳣(ͨ��Ϊ��Ϣ�������Ͽ�)
				 *  ����ֹͣ��Ըÿͻ�����Ϣ�Ľ��մ���
				 */
				stop();
			}
		}
	}
	
	/**
	 * �˳�
	 * @param msg
	 */
	private void logout(ClientMessage msg) {
		Server.getServer().removeReqByPassenger(user);
		Server.getServer().removeThread(this);
		OutUtils.outMsg( (user.getType() == 1 ? "�˿�" : "˾��") + "[" + user.getName() + "]����.");
	}

	/**
	 * ��½
	 * @param msg
	 */
	private void login(ClientMessage msg) {
		// ��ȡ��½����Ϣ
		User user = msg.getSender();
		user.setId(id);
		
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.LOGIN_OK);
		sm.setMessage(id);
		
		// ������Ӧ���͵���Ϣ������
		if(user.getType() == 1) {
			mp = new PassengerProccessor(this);
		} else {
			mp = new DriverProccesor(this);
		}
		
		OutUtils.outMsg( (user.getType() == 1 ?"�˿�" : "˾��") + "[" + user.getName() + "]����.");
		
		// ��ס��ǰ�û���Ϣ
		this.user = user;
		
		// ���͵�½�����Ϣ���ͻ���
		sendMessage(sm);
	}

	/**
	 * ֹͣ��Ϣ����
	 */
	public void stop() {
		// ����Ϊֹͣ״̬
		running = false;
		try {
			// �رտͻ����׽��ֶ���
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
