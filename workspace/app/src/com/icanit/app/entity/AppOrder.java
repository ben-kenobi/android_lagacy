package com.icanit.app.entity;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AppOrder entity. @author MyEclipse Persistence Tools
 */

public class AppOrder implements java.io.Serializable {
	// Fields

	public Integer id,userId,status=0;
	public String orderNumber,userName,phone,address;
	public Date orderTime;
	public double sum;
	public boolean deleted;
	public Set<AppOrderItems> appOrderItemses = new HashSet<AppOrderItems>(0);

	// Constructors

}