package com.imooc.dacheche.exception;

/**
 * 接收消息异常
 * @author Huang Shan
 *
 */
public class ReceiveMessageException extends Exception {
	private static final long serialVersionUID = -3225516799340184863L;

	public ReceiveMessageException() {
		super();
	}

	public ReceiveMessageException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReceiveMessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReceiveMessageException(String message) {
		super(message);
	}

	public ReceiveMessageException(Throwable cause) {
		super(cause);
	}
}
