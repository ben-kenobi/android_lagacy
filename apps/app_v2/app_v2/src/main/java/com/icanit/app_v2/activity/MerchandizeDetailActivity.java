package com.icanit.app_v2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.sqlite.ShoppingCartService;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.ImageUtil;

public class MerchandizeDetailActivity extends Activity{
	private AppGoods goods;
	private AppMerchant merchant;
	private ImageButton backButton;
	private TextView detail,goodsName,price,amount,scope,quantity;
	private ImageView pic;
	private Button buyNow,addToCart,plus,minus;
	private ShoppingCartService shoppingCartService;
	private int count;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchandize_detail);
		try {
			init();bindListeners();fillPageInfos();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		updateCount();
		updateQuantity();
	}
	private void updateCount(){
		CartItem item=AppUtil.appContext.shoppingCartMap.get(goods.id);
		if(item==null) count=1;else count=item.quantity;
	}
	private void init() throws AppException{
		shoppingCartService=AppUtil.getServiceFactory().getShoppingCartServiceInstance(this);
		goods=(AppGoods)getIntent().getSerializableExtra(IConstants.GOODS_KEY);
		merchant=(AppMerchant)getIntent().getSerializableExtra(IConstants.MERCHANT_KEY);
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		detail=(TextView)findViewById(R.id.textView1);
		goodsName=(TextView)findViewById(R.id.textView2);
		price=(TextView)findViewById(R.id.textView3);
		amount=(TextView)findViewById(R.id.textView4);
		scope=(TextView)findViewById(R.id.textView5);
		quantity=(TextView)findViewById(R.id.textView6);
		pic=(ImageView)findViewById(R.id.imageView1);
		buyNow=(Button)findViewById(R.id.button1);
		addToCart=(Button)findViewById(R.id.button2);
		plus=(Button)findViewById(R.id.button3);
		minus=(Button)findViewById(R.id.button4);
	}
	private void updateQuantity(){
		if(count>0){
			minus.setEnabled(true);quantity.setText(count+"");
		}else{
			minus.setEnabled(false);quantity.setText("0");
		}
	}
	
	private void bindListeners(){
		AppUtil.bindBackListener(backButton);
		buyNow.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(addToCart()){
					Intent intent = new Intent(MerchandizeDetailActivity.this,ShoppingCartSubstituteActivity.class);
					startActivity(intent);
				}
			}
		});
		addToCart.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				addToCart();
			}
		});
		plus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				count++;
				updateQuantity();
			}
		});
		minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(count>0)
				count--;
				updateQuantity();
			}
		});
	}
	
	private boolean  addToCart(){
//		String quantityTx=quantity.getText().toString().trim();
//		boolean b = AppUtil.isNumber(quantityTx);
		if(!AppUtil.appContext.shoppingCartMap.containsKey(goods.id)&&count==0)	return false;
		shoppingCartService.addCartItem(goods, merchant,count);
		if(count<=0)
			Toast.makeText(this, "移除成功", Toast.LENGTH_SHORT).show();
		else 
			Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
//		updateQuantity();
		return true;
	}
	
	
	private void fillPageInfos(){
		detail.setText(goods.detail);
		goodsName.setText("品名："+goods.goodName);
		price.setText("价格：￥"+String.format("%.2f",goods.curPrice));
		amount.setText("库存："+goods.amount);
		scope.setText("配送范围：XXXX");
		ImageUtil.asyncDownloadImageAndShow(pic, goods.pic, this, false);
	}
}
