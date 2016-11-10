package com.icanit.app.sqlite;

import java.util.List;
import java.util.Map;

import android.util.Log;
import android.widget.Button;

import com.icanit.app.entity.AppGoods;
import com.icanit.app.entity.CartItem;
import com.icanit.app.util.AppUtil;

public class ShoppingCartServiceImpl implements ShoppingCartService{
	public static List<CartItem> itemList=AppUtil.appContext.shoppingCartList;
	public static Map<Integer,CartItem> itemMap=AppUtil.appContext.shoppingCartMap;

	@Override
	public void addItem(CartItem item) {
		itemList.add(item);
		itemMap.put(item.prod_id, item);
	}

	@Override
	public void removeItem(CartItem item) {
		itemList.remove(item);
		itemMap.remove(item.prod_id);
	}

	@Override
	public void clearAllItems() {
		itemList.clear();
		itemMap.clear();
	}
	
	public CartItem cartItemConvertor(AppGoods goods){
		CartItem item = new CartItem();
		item.add_time=System.currentTimeMillis();
		item.former_price=goods.duePrice;
		item.phone="1378965478466";
		item.present_price=goods.curPrice;
		item.prod_desc=goods.detail;
		item.prod_id=goods.id;
		item.prod_name=goods.goodName;
		item.snap=goods.pic;
		return item;
	}
	
	public  void updateAddNminusButton(Button add,Button minus,int prodId){
		if(itemMap.containsKey(prodId)){
			minus.setEnabled(true);
			add.setText(itemMap.get(prodId).quantity+"");
			Log.d("errorTag","quantity="+itemMap.get(prodId).quantity+" @ShoppingCartService");
		}else{
			minus.setEnabled(false);
			add.setText("+");
		}
	}
	
	
	public void addCartItem(Button add,Button minus,AppGoods goods){
		if(itemMap.containsKey(goods.id)){
			 itemMap.get(goods.id).quantity++;
		}else{
			addItem(cartItemConvertor(goods));
		}
		updateAddNminusButton(add, minus,goods.id);
	}
	
	public void minusCartItem(Button add,Button minus,int prodId) {
		if(itemMap.containsKey(prodId)){
			CartItem item = itemMap.get(prodId);
			if(item.quantity>1) {
				item.quantity--;
			}else{
				removeItem(item);
			}
		}
		updateAddNminusButton(add, minus, prodId);
	}
	

}
