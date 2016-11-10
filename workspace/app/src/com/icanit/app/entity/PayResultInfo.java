package com.icanit.app.entity;

import java.io.Serializable;

public class PayResultInfo implements Serializable{
	public String application,merchantId,merchantOrderId,merchantOrderTime,respCode,respDesc;

	@Override
	public String toString() {
		return "PayResultInfo [application=" + application + ", merchantId="
				+ merchantId + ", merchantOrderId=" + merchantOrderId
				+ ", merchantOrderTime=" + merchantOrderTime + ", respCode="
				+ respCode + ", respDesc=" + respDesc + "]";
	}
	
}
