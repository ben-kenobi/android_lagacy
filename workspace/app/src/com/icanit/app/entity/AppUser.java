package com.icanit.app.entity;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


public class AppUser implements java.io.Serializable {

	// Fields

	public Integer id;
	public AppCommunity appCommunity;
	public String userName,phone,password,address,veriCode,veriCodeTemp;
	public Integer permission=0;
	public Date regTime=new Date();
	public boolean isverified=false;//
	public Set<AppOrder> appOrders = new HashSet<AppOrder>();

	// Constructors

	
}