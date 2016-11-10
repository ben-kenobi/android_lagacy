package com.icanit.app_v2.sqlite;

import android.widget.Button;

import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;

public interface ShoppingCartService {
	void addItem(CartItem item);
	void removeItem(CartItem item,MerchantCartItems mc);
	void clearAllItems();
	CartItem cartItemConvertor(AppGoods goods,AppMerchant merchant);
	void updateAddNminusButton(Button add,Button minus,String prodId);
	void addCartItem(Button add,Button minus,AppGoods goods,AppMerchant merchant);
	void addCartItem(AppGoods goods,AppMerchant merchant);
	void addCartItem(AppGoods goods,AppMerchant merchant,int quantity);
	void increaseCartItem(String prodId);
	void decreaseCartItem(Button add,Button minus,String prodId);
	void decreaseCartItem(String prodId);
	int getItemsCount();
}
