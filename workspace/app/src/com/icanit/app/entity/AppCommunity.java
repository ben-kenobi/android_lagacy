package com.icanit.app.entity;
// default package

import java.util.HashSet;
import java.util.Set;


public class AppCommunity implements java.io.Serializable {

	// Fields

	public int id,longitudeE6,latitudeE6;
	public String commName,location,cityName;
	public Set<AppMerchant> appMerchants = new HashSet<AppMerchant>(0);



}