package com.icanit.app_v2.entity;
// default package

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * AppMerchant entity. @author MyEclipse Persistence Tools
 */

public class AppMerchant implements java.io.Serializable {

	// Fields

	public int id,type,recommend;
	public double deliverPrice;
	public String merName,location,phone,tel,detail,logo;
	public Date regTime;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result +id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppMerchant other = (AppMerchant) obj;
		if (id == 0) {
			if (other.id != 0)
				return false;
		} else if (id!=other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AppMerchant [id=" + id + ", merName=" + merName + "]";
	}

}