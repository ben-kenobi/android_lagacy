package com.icanit.bdmapversion2.entity;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class Merchant  implements java.io.Serializable {


    // Fields    

     private long id;
     private String merName;
     private Integer typeId;
     private double minCost;
     private String detail;
     
     private GeoPoint geoPoint;
     private long commuId;
    // Constructors

    /** default constructor */
    public Merchant() {
    	
    }

    // Property accessors
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMerName() {
		return merName;
	}

	public void setMerName(String merName) {
		this.merName = merName;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public double getMinCost() {
		return minCost;
	}

	public void setMinCost(double minCost) {
		this.minCost = minCost;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public long getCommuId() {
		return commuId;
	}

	public void setCommuId(long commuId) {
		this.commuId = commuId;
	}
    
}