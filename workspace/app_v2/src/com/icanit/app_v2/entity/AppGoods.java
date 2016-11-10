package com.icanit.app_v2.entity;
// default package

import java.util.Date;

/**
 * AppGoods entity. @author MyEclipse Persistence Tools
 */

public class AppGoods implements java.io.Serializable {

	// Fields
	public Integer merId,cateId,amount;
	public String id,merName,cateName,goodName,shortName,detail,pic;
	public double curPrice,duePrice;
	public Date addTime;

}