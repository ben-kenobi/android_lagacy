package com.icanit.bdmapversion2.entity;

import java.io.Serializable;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class Business implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3595862400411677393L;
	private int businessId;
	private int CommunityId;
	private double businessDistance;
	private GeoPoint businessGeoPoint;
	private String businessName;
	private String businessValuation;
	private String businessAddress;
	private String businessDescription;
	private String businessPicName;
	private String businessPicDir;
	private double businessPrice;
	
	public int getBusinessId() {
		return businessId;
	}
	public void setBusinessId(int businessId) {
		this.businessId = businessId;
	}
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	public String getBusinessValuation() {
		return businessValuation;
	}
	public void setBusinessValuation(String businessValuation) {
		this.businessValuation = businessValuation;
	}
	public String getBusinessAddress() {
		return businessAddress;
	}
	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}
	public String getBusinessDescription() {
		return businessDescription;
	}
	public void setBusinessDescription(String businessDescription) {
		this.businessDescription = businessDescription;
	}
	public String getBusinessPicDir() {
		return businessPicDir;
	}
	public void setBusinessPicDir(String businessPicDir) {
		this.businessPicDir = businessPicDir;
	}
	public String getBusinessPicName() {
		return businessPicName;
	}
	public void setBusinessPicName(String businessPicName) {
		this.businessPicName = businessPicName;
	}
	public double getBusinessPrice() {
		return businessPrice;
	}
	public void setBusinessPrice(double businessPrice) {
		this.businessPrice = businessPrice;
	}
	public GeoPoint getBusinessGeoPoint() {
		return businessGeoPoint;
	}
	public void setBusinessGeoPoint(GeoPoint businessGeoPoint) {
		this.businessGeoPoint = businessGeoPoint;
	}
	public void setBusinessDistance(double businessDistance) {
		this.businessDistance = businessDistance;
	}
	public double getBusinessDistance() {
		return businessDistance;
	}
	public int getCommunityId() {
		return CommunityId;
	}
	public void setCommunityId(int communityId) {
		CommunityId = communityId;
	}
	
	
}
