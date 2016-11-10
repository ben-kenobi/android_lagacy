package com.icanit.app_v2.sqlite;

import android.util.Log;
import android.widget.Button;

import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.util.AppUtil;

public class ShoppingCartServiceImpl implements ShoppingCartService{
	
	@Override
	public void addItem(CartItem item) {
		AppUtil.appContext.shoppingCartList.add(item);
		AppUtil.appContext.shoppingCartMap.put(item.prod_id, item);
		for(int j=0;j<AppUtil.appContext.dividedItemList.size();j++){
			MerchantCartItems mc = AppUtil.appContext.dividedItemList.get(j);
			if(mc.merchant.id==item.mer_id){
				mc.items.add(item);return;
			}
		}
		MerchantCartItems mc = new MerchantCartItems();
		mc.merchant.id=item.mer_id;mc.merchant.merName=item.mer_name;
		mc.merchant.location=item.mer_location;
		mc.postscript=item.postscript;
		mc.items.add(item);
		AppUtil.appContext.dividedItemList.add(mc);
	}

	@Override
	public void removeItem(CartItem item,MerchantCartItems merCartItem) {
		AppUtil.appContext.shoppingCartList.remove(item);
		AppUtil.appContext.shoppingCartMap.remove(item.prod_id);
		if(merCartItem==null){
			for(int i=0;i<AppUtil.appContext.dividedItemList.size();i++){
				MerchantCartItems mc = AppUtil.appContext.dividedItemList.get(i);
				if(mc.merchant.id==item.mer_id){
					merCartItem=mc;break;
				}
			}
		}
		merCartItem.items.remove(item);
		if(merCartItem.items.isEmpty()){
			AppUtil.appContext.dividedItemList.remove(merCartItem);
		}
	}

	@Override
	public void clearAllItems() {
		AppUtil.appContext.shoppingCartList.clear();
		AppUtil.appContext.shoppingCartMap.clear();
		AppUtil.appContext.dividedItemList.clear();
	}
	public int getItemsCount(){
		if(AppUtil.appContext==null||AppUtil.appContext.shoppingCartList==null) return 0;
		int count =0;
		for(int i=0;i<AppUtil.appContext.shoppingCartList.size();i++){
			count+=AppUtil.appContext.shoppingCartList.get(i).quantity;
		}
		return count;
	}
	public CartItem cartItemConvertor(AppGoods goods,AppMerchant merchant){
		CartItem item = new CartItem();
		item.add_time=System.currentTimeMillis();
		item.former_price=goods.duePrice;
		item.phone="1378965478466";
		item.present_price=goods.curPrice;
		item.prod_desc=goods.detail;
		item.prod_id=goods.id;
		item.prod_name=goods.goodName;
		item.snap=goods.pic;
		item.mer_id=goods.merId;
		item.cate_id=goods.cateId;
		if(merchant!=null){
			item.mer_name=merchant.merName;
			item.mer_location=merchant.location;
		}
		return item;
	}
	
	public  void updateAddNminusButton(Button add,Button minus,String prodId){
		if(AppUtil.appContext.shoppingCartMap.containsKey(prodId)){
			minus.setEnabled(true);
			add.setText(AppUtil.appContext.shoppingCartMap.get(prodId).quantity+"");
			Log.d("errorTag","quantity="+AppUtil.appContext.shoppingCartMap.get(prodId).quantity+" @ShoppingCartService");
		}else{
			minus.setEnabled(false);
			add.setText("+");
		}
	}
	
	
	public void addCartItem(Button add,Button minus,AppGoods goods,AppMerchant merchant){
		if(AppUtil.appContext.shoppingCartMap.containsKey(goods.id)){
			AppUtil.appContext.shoppingCartMap.get(goods.id).quantity++;
		}else{
			addItem(cartItemConvertor(goods,merchant));
		}
		updateAddNminusButton(add, minus,goods.id);
	}
	

	public void decreaseCartItem(Button add,Button minus,String prodId) {
		if(AppUtil.appContext.shoppingCartMap.containsKey(prodId)){
			CartItem item = AppUtil.appContext.shoppingCartMap.get(prodId);
			if(item.quantity>1) {
				item.quantity--;
			}else{
				removeItem(item,null);
			}
		}
		updateAddNminusButton(add, minus, prodId);
	}
	
	public void addCartItem(AppGoods goods,AppMerchant merchant){
		if(AppUtil.appContext.shoppingCartMap.containsKey(goods.id)){
			AppUtil.appContext.shoppingCartMap.get(goods.id).quantity++;
			 System.out.println("containsKey addCartItem"+goods.id+",map="+AppUtil.appContext.shoppingCartMap+
					 ",list="+AppUtil.appContext.shoppingCartList+"  @ shoppingCartService");
		}else{
			addItem(cartItemConvertor(goods,merchant));
			System.out.println("notContains addCartItem"+goods.id+"  @ shoppingCartService");
		}
	}
	public void increaseCartItem(String prodId){
		if(AppUtil.appContext.shoppingCartMap.containsKey(prodId)){
			AppUtil.appContext.shoppingCartMap.get(prodId).quantity++;
		}
	}
	public void decreaseCartItem(String prodId){
		if(AppUtil.appContext.shoppingCartMap.containsKey(prodId)){
			CartItem item = AppUtil.appContext.shoppingCartMap.get(prodId);
			if(item.quantity>1) {
				item.quantity--;
			}else{
				removeItem(item,null);
			}
		}
	}

	@Override
	public void addCartItem(AppGoods goods, AppMerchant merchant, int quantity) {
		if(goods==null||merchant==null||quantity<0)return;
		if(AppUtil.appContext.shoppingCartMap.containsKey(goods.id)){
			CartItem item = AppUtil.appContext.shoppingCartMap.get(goods.id);
			if(quantity==0){
				removeItem(item,null);
			}else{
				item.quantity=quantity;
				 System.out.println("containsKey addCartItem"+goods.id+",map="+AppUtil.appContext.shoppingCartMap+
						 ",list="+AppUtil.appContext.shoppingCartList+"  @ shoppingCartService");
			}
		}else{
			if(quantity==0) return;
			CartItem item=cartItemConvertor(goods,merchant);
			item.quantity=quantity;
			addItem(item);
			System.out.println("notContains addCartItem"+goods.id+"  @ shoppingCartService");
		}
	}
	
}
