package com.icanit.app_v2.entity;

import java.io.Serializable;

public class CartItem implements Serializable {
	public int _id,quantity=1,status=1,mer_id,cate_id;
	public long add_time;
	public double present_price,former_price;
	public String phone,prod_name,prod_desc,snap,mer_name,postscript="",mer_location,prod_id;
	private boolean delivery=true;
	public boolean isDelivery() {
		return delivery;
	}
	public void setDelivery(boolean delivery) {
		this.delivery = delivery;
	}
	
}
