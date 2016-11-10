package com.icanit.app_v2.exception;

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.util.AppUtil;


public class AppException extends Exception {

	public AppException() {
		super();
	}

	public AppException(final String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AppException(final String detailMessage) {
		super(detailMessage);
	}

	public AppException(Throwable throwable) {
		super(throwable);
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		IConstants.MAIN_HANDLER.post(new Runnable(){
			public void run(){
				AppUtil.toast("发生异常。。。，请检查网络");
			}
		});
	}
	
	
}
