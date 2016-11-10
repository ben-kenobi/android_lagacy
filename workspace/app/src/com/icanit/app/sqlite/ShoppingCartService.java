package com.icanit.app.sqlite;

import android.widget.BaseAdapter;
import android.widget.Button;

import com.icanit.app.entity.AppGoods;
import com.icanit.app.entity.CartItem;

public interface ShoppingCartService {
	void addItem(CartItem item);
	void removeItem(CartItem item);
	void clearAllItems();
	CartItem cartItemConvertor(AppGoods goods);
	void updateAddNminusButton(Button add,Button minus,int prodId);
	void addCartItem(Button add,Button minus,AppGoods goods);
	void minusCartItem(Button add,Button minus,int prodId);
}
