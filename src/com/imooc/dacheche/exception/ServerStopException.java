package com.imooc.dacheche.exception;

/**
 * 关闭服务器时发生异常
 * @author HuangShan
 *
 */
public class ServerStopException extends Exception {
	private static final long serialVersionUID = -7592249763086945790L;

	public ServerStopException() {
		super();
	}

	public ServerStopException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ServerStopException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerStopException(String message) {
		super(message);
	}

	public ServerStopException(Throwable cause) {
		super(cause);
	}

}
