package com.icanit.app_v2.entity;

import java.util.ArrayList;
import java.util.List;

public class MerchantCartItems {
	public AppMerchant merchant=new AppMerchant();
	public List<CartItem> items=new ArrayList<CartItem>();
	public boolean delivery=true; 
	public String postscript="";
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if(obj.getClass()!=Integer.class&&obj.getClass()!=int.class)
			return false;
		int merId=(Integer)obj;
		System.out.println("obj="+obj+",merchantId="+merchant.id+"   equals  @merchantCartItems");
		return merchant.id==merId;
	}
	
	
}
