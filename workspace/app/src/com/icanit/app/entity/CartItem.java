package com.icanit.app.entity;

import java.io.Serializable;

public class CartItem implements Serializable {
	public int _id,quantity=1,prod_id,status=1;
	public long add_time;
	public double present_price,former_price;
	public String phone,prod_name,prod_desc,snap;
}
