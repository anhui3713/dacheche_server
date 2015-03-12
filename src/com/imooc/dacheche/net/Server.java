package com.imooc.dacheche.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.imooc.dacheche.bean.RequestMessage;
import com.imooc.dacheche.bean.User;
import com.imooc.dacheche.common.OutUtils;
import com.imooc.dacheche.common.Utils;
import com.imooc.dacheche.exception.ReceiveMessageException;
import com.imooc.dacheche.exception.ServerStartException;
import com.imooc.dacheche.exception.ServerStopException;

/**
 * �����������������, ����������ֹͣ�������Կͻ��˵����Ӽ���
 * @author Huang Shan
 *
 */
public class Server {
	
	/**
	 * ����ģʽ
	 */
	private static Server server;
	private Server(){}
	public static Server getServer() {
		if(server == null) {
			server = new Server();
		}
		return server;
	}
	
	/**
	 * �������˽��ն���
	 */
	private ServerSocket ss;
	/**
	 * ����������״̬
	 * true: ����
	 * false: ֹͣ
	 * Ĭ��Ϊֹͣ״̬
	 */
	private boolean running = false;
	/**
	 * ���������߳�,������������ͣ���տͻ�������
	 */
	private ServerReceiverThread receiverThread;
	/**
	 * ��ǰ���������������д�����Ϣ���߳��б�
	 */
	private Map<String,MessageTransfer> mts = new HashMap<String,MessageTransfer>();
	/**
	 * ��ǰ�������ϵĴ������б�
	 */
	private List<RequestMessage> rms = new ArrayList<RequestMessage>();
	/**
	 * ��Ӵ���Ϣ
	 * @param rm
	 */
	public synchronized void addReqMsg(RequestMessage rm) {
		rms.add(rm);
	}
	
	/**
	 * ɾ������Ϣ
	 * @param rm
	 */
	public synchronized void removeReqMsg(String id) {
		Iterator<RequestMessage> irm = rms.iterator();
		while(irm.hasNext()) {
			RequestMessage rm = irm.next();
			if(rm.getId().equals(id)) {
				irm.remove();
			}
		}
	}
	
	/**
	 * ɾ��ָ���˿ͷ��͵Ĵ���Ϣ
	 * @param passenger
	 */
	public synchronized void removeReqByPassenger(User passenger) {
		Iterator<RequestMessage> irm = rms.iterator();
		while(irm.hasNext()) {
			RequestMessage rm = irm.next();
			if(rm.getUser().equals(passenger)) {
				irm.remove();
			}
		}
	}
	
	/**
	 * ���ָ����Ϣ�Ƿ�����Ϣ�б���
	 * @param rm
	 * @return
	 */
	public synchronized boolean containsReqMsg(RequestMessage rm) {
		return rms.contains(rm);
	}
	
	/**
	 * ��ȡ�������б��е�ĳ����Ϣ
	 * @param id
	 * @return
	 */
	public RequestMessage getReqMsg(String id) {
		for (RequestMessage rm : rms) {
			if(rm.getId().equals(id)) {
				return rm;
			}
		}
		
		return null;
	}
	
	/**
	 * ���ص�ǰ����Ϣ�б�
	 * @return
	 */
	public synchronized RequestMessage[] listReqMsgs() {
		return rms.toArray(new RequestMessage[rms.size()]);
	}
	/**
	 * ����id��ȡ��Ӧ�ͻ��˶���,����ʵ����Ϣ����
	 * @param id
	 * @return
	 */
	public MessageTransfer getMt(String id) {
		return mts.get(id);
	}
	
	/**
	 * ������������
	 * @throws ServerStartException ���������������쳣
	 */
	public void start() throws ServerStartException {
		try {
			OutUtils.outMsg("��������������...");
			// �������������ն���,����9527�˿�
			ss = new ServerSocket(9527);
			
			// ���÷�����������״̬Ϊ����״̬
			running = true;
			
			// ���������߳�,���ڼ������տͻ�����Ϣ
			receiverThread = new ServerReceiverThread();
			receiverThread.start();
			OutUtils.outMsg("�������!");
		} catch (IOException e) {
			throw new ServerStartException("��������������", e);
		}
	}
	
	/**
	 * ֹͣ��������
	 * @throws ServerStopException  ֹͣ������ʱ�����쳣
	 */
	public void stop() throws ServerStopException {
		// ��������״̬Ϊֹͣ
		running = false;
		// ֹͣ��������ռsocket������Դ,��ֹ���пͻ������ӽ���,��ռ�������ر�
		try {
			Socket es = new Socket("localhost", 9527);
			es.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ֹͣ��ǰ���ڴ�����Ϣ�������߳�
		if(mts != null && mts.size() > 0) {
			for (MessageTransfer mp : mts.values()) {
				mp.stop();
			}
			
			// ��������̶߳���
			mts.clear();
		}
		
		// �رշ������˽��ն���
		try {
			ss.close();
		} catch (IOException e) {
			throw new ServerStopException();
		}
		ss = null;
		
		// ����ǰ�������еĽ����߳�����Ϊnull
		receiverThread = null;
	}
	
	/**
	 * ���߳��б���ɾ��ָ���߳�
	 * ���ͻ��������߻����������ӶϿ�ʱ
	 * ���̶߳���ֹͣ
	 * ��Ҫ���߳��б���ɾ���ö���
	 */
	protected void removeThread(MessageTransfer mt) {
		mts.remove(mt);
	}
	
	/**
	 * ר�����ڽ���������Ϣ�����߳�
	 * @author Administrator
	 *
	 */
	class ServerReceiverThread extends Thread {
		
		public void run() {
			if(ss != null && !ss.isClosed()) {
				try {
					// ���÷������˽��ճ�ʱʱ��Ϊ5��
					ss.setSoTimeout(5000);
					while(running) {
						// �����̴߳�����Ըÿͻ��˵���Ϣ
						MessageTransfer mt = null;
						try {
							// ���յ��ͻ�������
							Socket s = ss.accept();
							// ����һ��id����Ψһ����ÿ���ͻ��˵��̶߳���
							String proId = Utils.getId();
							// �����̴߳�����Ըÿͻ��˵���Ϣ
							mt = new MessageTransfer(proId, s);
							// ��ӵ��̼߳�����,�����ر�ֹͣ�߳�
							mts.put(proId, mt);
							// �����߳�
							new Thread(mt).start();
							
						} catch(SocketTimeoutException  e) {
							// �����ӳ�ʱ�����쳣,�����쳣,ֱ�ӽ�����һ�ν���
						} catch (IOException e) {
							e.printStackTrace();
							// ����ʱ���������쳣
						} catch (ReceiveMessageException e) {
							 e.printStackTrace();
							 /* 
							  * ��Ա��οͻ����׽��ִ������ն���ʱ�����쳣
							  * ����ǰ������߳��б���ɾ��
							  */
							 mts.remove(mt);
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
					// �����쳣,ֹͣ����
					running = false;
				}
			}
		}
	}
}
