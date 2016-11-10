package com.icanit.app_v2.entity;
// default package


/**
 * AppOrderItems entity. @author MyEclipse Persistence Tools
 */

public class AppOrderItems implements java.io.Serializable {

	// Fields

	public int id,orderId,goodsId,quantity,status,merId;
	public double sum,curPrice,duePrice;
	public String goodName, postscript;
	public boolean delivery;

	// Constructors

}