package com.imooc.dacheche.exception;

/**
 * 开启服务器时发生异常
 * @author Huang Shan
 *
 */
public class ServerStartException extends Exception {
	private static final long serialVersionUID = -3105291155384001453L;

	public ServerStartException() {
		super();
	}

	public ServerStartException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServerStartException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerStartException(String message) {
		super(message);
	}

	public ServerStartException(Throwable cause) {
		super(cause);
	}
	
}
