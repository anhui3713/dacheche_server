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
 * 服务器端网络管理类, 用于启动和停止服务器对客户端的连接监听
 * @author Huang Shan
 *
 */
public class Server {
	
	/**
	 * 单例模式
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
	 * 服务器端接收对象
	 */
	private ServerSocket ss;
	/**
	 * 服务器运行状态
	 * true: 运行
	 * false: 停止
	 * 默认为停止状态
	 */
	private boolean running = false;
	/**
	 * 接收请求线程,启动服务器后不停接收客户端请求
	 */
	private ServerReceiverThread receiverThread;
	/**
	 * 当前服务器中正在运行处理消息的线程列表
	 */
	private Map<String,MessageTransfer> mts = new HashMap<String,MessageTransfer>();
	/**
	 * 当前服务器上的打车请求列表
	 */
	private List<RequestMessage> rms = new ArrayList<RequestMessage>();
	/**
	 * 添加打车消息
	 * @param rm
	 */
	public synchronized void addReqMsg(RequestMessage rm) {
		rms.add(rm);
	}
	
	/**
	 * 删除打车消息
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
	 * 删除指定乘客发送的打车消息
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
	 * 检查指定消息是否还在消息列表中
	 * @param rm
	 * @return
	 */
	public synchronized boolean containsReqMsg(RequestMessage rm) {
		return rms.contains(rm);
	}
	
	/**
	 * 获取打车请求列表中的某条消息
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
	 * 返回当前打车消息列表
	 * @return
	 */
	public synchronized RequestMessage[] listReqMsgs() {
		return rms.toArray(new RequestMessage[rms.size()]);
	}
	/**
	 * 根据id获取对应客户端对象,可以实现消息发送
	 * @param id
	 * @return
	 */
	public MessageTransfer getMt(String id) {
		return mts.get(id);
	}
	
	/**
	 * 启动服务器端
	 * @throws ServerStartException 启动服务器出错异常
	 */
	public void start() throws ServerStartException {
		try {
			OutUtils.outMsg("正在启动服务器...");
			// 创建服务器接收对象,监听9527端口
			ss = new ServerSocket(9527);
			
			// 设置服务器端运行状态为运行状态
			running = true;
			
			// 启动接收线程,用于监听接收客户端消息
			receiverThread = new ServerReceiverThread();
			receiverThread.start();
			OutUtils.outMsg("启动完成!");
		} catch (IOException e) {
			throw new ServerStartException("启动服务器出错", e);
		}
	}
	
	/**
	 * 停止服务器端
	 * @throws ServerStopException  停止服务器时发生异常
	 */
	public void stop() throws ServerStopException {
		// 设置运行状态为停止
		running = false;
		// 停止后立即抢占socket连接资源,防止再有客户端连接进来,抢占后立即关闭
		try {
			Socket es = new Socket("localhost", 9527);
			es.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 停止当前正在处理消息的所有线程
		if(mts != null && mts.size() > 0) {
			for (MessageTransfer mp : mts.values()) {
				mp.stop();
			}
			
			// 清空所有线程对象
			mts.clear();
		}
		
		// 关闭服务器端接收对象
		try {
			ss.close();
		} catch (IOException e) {
			throw new ServerStopException();
		}
		ss = null;
		
		// 将当前正在运行的接收线程设置为null
		receiverThread = null;
	}
	
	/**
	 * 从线程列表中删除指定线程
	 * 当客户主动下线或者网络连接断开时
	 * 该线程对象将停止
	 * 需要从线程列表中删除该对象
	 */
	protected void removeThread(MessageTransfer mt) {
		mts.remove(mt);
	}
	
	/**
	 * 专门用于接收连接消息的子线程
	 * @author Administrator
	 *
	 */
	class ServerReceiverThread extends Thread {
		
		public void run() {
			if(ss != null && !ss.isClosed()) {
				try {
					// 设置服务器端接收超时时长为5秒
					ss.setSoTimeout(5000);
					while(running) {
						// 创建线程处理针对该客户端的消息
						MessageTransfer mt = null;
						try {
							// 接收到客户端连接
							Socket s = ss.accept();
							// 生成一个id用作唯一区分每个客户端的线程对象
							String proId = Utils.getId();
							// 创建线程处理针对该客户端的消息
							mt = new MessageTransfer(proId, s);
							// 添加到线程集合中,用作关闭停止线程
							mts.put(proId, mt);
							// 启动线程
							new Thread(mt).start();
							
						} catch(SocketTimeoutException  e) {
							// 当连接超时发生异常,忽略异常,直接进行下一次接收
						} catch (IOException e) {
							e.printStackTrace();
							// 连接时发生其他异常
						} catch (ReceiveMessageException e) {
							 e.printStackTrace();
							 /* 
							  * 针对本次客户端套接字创建接收对象时发生异常
							  * 将当前对象从线程列表中删除
							  */
							 mts.remove(mt);
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
					// 发生异常,停止接收
					running = false;
				}
			}
		}
	}
}
