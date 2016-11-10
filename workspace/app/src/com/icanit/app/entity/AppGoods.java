package com.icanit.app.entity;
// default package

import java.util.Date;

/**
 * AppGoods entity. @author MyEclipse Persistence Tools
 */

public class AppGoods implements java.io.Serializable {

	// Fields

	public Integer id,amount,hot;
	public AppMerchant appMerchant;
	public AppCategory appCategory;
	public String goodName,detail,pic;
	public double curPrice,duePrice;
	public Date addTime;


}