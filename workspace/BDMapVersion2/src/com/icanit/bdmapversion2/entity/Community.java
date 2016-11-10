package com.icanit.bdmapversion2.entity;

import java.util.List;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class Community {
	private long id;
	private String communityName;
	private GeoPoint communityCenterPoint;
	private List<Business> communityBusiness;//
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public GeoPoint getCommunityCenterPoint() {
		return communityCenterPoint;
	}
	public void setCommunityCenterPoint(GeoPoint communityCenterPoint) {
		this.communityCenterPoint = communityCenterPoint;
	}
	public List<Business> getCommunityBusiness() {
		return communityBusiness;
	}
	public void setCommunityBusiness(List<Business> communityBusiness) {
		this.communityBusiness = communityBusiness;
	}
	
	
}
