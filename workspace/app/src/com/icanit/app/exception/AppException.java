package com.icanit.app.exception;

public class AppException extends Exception {

	public AppException() {
		super();
	}

	public AppException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AppException(String detailMessage) {
		super(detailMessage);
	}

	public AppException(Throwable throwable) {
		super(throwable);
	}
	
}
