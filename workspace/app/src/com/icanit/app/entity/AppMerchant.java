package com.icanit.app.entity;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AppMerchant entity. @author MyEclipse Persistence Tools
 */

public class AppMerchant implements java.io.Serializable {

	// Fields

	public Integer id;
	public AppMerchantType appMerchantType;
	public String merName,password,location,map,pic,phone,detail;
	public double minCost;
	public Date regTime;
	public Set<AppGoods> appGoodses = new HashSet<AppGoods>(0);
	public Set<AppCommunity> appCommunities = new HashSet<AppCommunity>(0);


}