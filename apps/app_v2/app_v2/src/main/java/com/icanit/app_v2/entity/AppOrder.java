package com.icanit.app_v2.entity;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AppOrder entity. @author MyEclipse Persistence Tools
 */

public class AppOrder implements java.io.Serializable {
	// Fields

	public int id,userId,status=0,count,payway,type=0;
	public String orderNumber,userName,contactPhone,address,userPhone,
					cupsQid,settleDate,cupsTraceNum,cupsTraceTime,setlAmt,cancelOrderNum,cancelOrderTime;
	public Date orderTime;
	public double sum;
	public boolean deleted;
	public Set<AppOrderItems> appOrderItemses = new HashSet<AppOrderItems>(0);
	@Override
	public String toString() {
		return "id:"+id+",status:"+status;
	}
	// Constructors

}