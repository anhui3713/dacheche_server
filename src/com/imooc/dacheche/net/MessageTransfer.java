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
 * 服务器消息处理线程
 * @author Huang Shan
 *
 */
public class MessageTransfer implements Runnable {

	/**
	 * 服务器为每个线程对象分配的id,方便对每个对象进行管理
	 */
	private String id;
	/**
	 * 线程是否正在处理
	 */
	private boolean running = false;
	/**
	 * 本线程处理的消息套接字对象
	 */
	private Socket socket;
	/**
	 * 针对客户端套接字的对象输入流,用于读取对方发送的消息
	 */
	private ObjectInputStream ois;
	/**
	 * 针对客户端套接字的输出流,用于向对方发送消息
	 */
	private ObjectOutputStream oos;
	
	/**
	 * 当前登陆用户的信息
	 */
	private User user;
	/**
	 * 对应的消息处理器
	 */
	private MessagePoccesor mp;
	
	/**
	 * 唯一构造方法,必须传入本线程处理的套接字对象
	 * @param socket
	 * @throws ReceiveMessageException 
	 */
	public MessageTransfer(String id,Socket socket) throws ReceiveMessageException {
		this.id = id;
		this.socket = socket;
		try {
			/*
			 * 服务器端先创建针对客户端的对象输出流
			 * 并马上发送一个空对象,用于打通对象流通道
			 * 防止构造对象流时接收不到header信息而发生阻塞
			 */
			this.oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(null);
			oos.flush();
			// 构造针对当前客户端的对象输入流
			this.ois = new ObjectInputStream(socket.getInputStream());
			// 设置为运行状态
			running = true;
		} catch (IOException e) {
			// 先设置当前线程为停止状态
			stop();
			throw new ReceiveMessageException("构造通信对象出错", e);
		}
	}

	/**
	 * 发送消息
	 * @param sm
	 * @throws IOException
	 */
	public void sendMessage(ServerMessage sm) {
		try {
			sm.setSendTime(new Date());
			oos.writeObject(sm);
			oos.flush();
		} catch (IOException e) {
			OutUtils.outMsg("发送消息失败.");
//			e.printStackTrace();
		}
	}
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	public User getUser() {
		return this.user;
	}
	
	/**
	 * 线程执行的方法,用于接收客户端连接请求
	 */
	public void run() {
		while(running) {
			try {
				// 接收客户端发送过来的 消息
				ClientMessage msg = (ClientMessage) ois.readObject();
				
				// 获取客户端消息,并进行分发
				switch(msg.getCommand()) {
					 // 登陆
					 case ClientMessage.LOGIN: login(msg); break;
				     // 退出
					 case ClientMessage.EXIT: logout(msg); break;
					 // 其他 交由对应的消息处理器来处理
					 default: mp.proccess(msg);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				// 接收到无法识别的消息
			} catch (IOException e) {
				/*
				 *  网络连接异常(通常为消息非正常断开)
				 *  立即停止针对该客户端消息的接收处理
				 */
				stop();
			}
		}
	}
	
	/**
	 * 退出
	 * @param msg
	 */
	private void logout(ClientMessage msg) {
		Server.getServer().removeReqByPassenger(user);
		Server.getServer().removeThread(this);
		OutUtils.outMsg( (user.getType() == 1 ? "乘客" : "司机") + "[" + user.getName() + "]下线.");
	}

	/**
	 * 登陆
	 * @param msg
	 */
	private void login(ClientMessage msg) {
		// 获取登陆者信息
		User user = msg.getSender();
		user.setId(id);
		
		ServerMessage sm = new ServerMessage();
		sm.setState(ServerMessage.LOGIN_OK);
		sm.setMessage(id);
		
		// 创建对应类型的消息处理器
		if(user.getType() == 1) {
			mp = new PassengerProccessor(this);
		} else {
			mp = new DriverProccesor(this);
		}
		
		OutUtils.outMsg( (user.getType() == 1 ?"乘客" : "司机") + "[" + user.getName() + "]上线.");
		
		// 记住当前用户信息
		this.user = user;
		
		// 发送登陆完成消息到客户端
		sendMessage(sm);
	}

	/**
	 * 停止消息处理
	 */
	public void stop() {
		// 设置为停止状态
		running = false;
		try {
			// 关闭客户端套接字对象
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
