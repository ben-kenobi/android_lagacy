package com.what.yunbao.person;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.address.AddressManageActivity;
import com.what.yunbao.collection.CollectionActivity;
import com.what.yunbao.history.HistoryActivity;
import com.what.yunbao.order.OrderActivity;
import com.what.yunbao.setting.SettingTabActivity;
import com.what.yunbao.test.TestActivity;
import com.what.yunbao.test.TestAddress;

public class PersonalCenterActivity extends Activity {
	private Bitmap bmp_1 = null;//背景 background
	private Bitmap bmp_2 = null;//图片 src
	private LinearLayout bc_1;
	private ImageView bc_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.personal_center);
		bc_1 = (LinearLayout) findViewById(R.id.ll_recycle_bmp);
		bc_2 = (ImageView) findViewById(R.id.iv_recycle_bmp);
//		InputStream is = getResources().openRawResource(R.id.action_settings);
//		BitmapDrawable  bd = new BitmapDrawable(is);
//		bc_2.setBackgroundDrawable(bd);
		System.out.println("=========oncreate");

		bmp_2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_gt);
		bc_1.setBackgroundResource(R.drawable.bg_g);
		bc_2.setImageBitmap(bmp_2);
		
		bmp_1 = ((BitmapDrawable)bc_1.getBackground()).getBitmap();

		GridView gridView = (GridView) findViewById(R.id.gv_person_option);
		final String [] gridViewTextArr = getResources().getStringArray(R.array.pcgridview_text);
		Integer [] gridViewImgArr = new Integer [] {android.R.drawable.ic_menu_today,android.R.drawable.ic_dialog_info,
													android.R.drawable.ic_menu_save,android.R.drawable.btn_star_big_off,
													android.R.drawable.ic_dialog_dialer,android.R.drawable.ic_menu_manage};
		PCGridViewAdapter gridViewAdapter =new PCGridViewAdapter(this, gridViewTextArr, gridViewImgArr);
		gridView.setAdapter(gridViewAdapter);
	
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				String text = gridViewTextArr[arg2];
				Intent intent = new Intent();
				
				boolean logged  = true;//未登录
				if(text.equals("我的订单")){
					if(!logged){
						Toast.makeText(PersonalCenterActivity.this, "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(PersonalCenterActivity.this,TestActivity.class));
						return;
					};
					Toast.makeText(getBaseContext(), text, 2000).show();
					intent.setClass(getBaseContext(), OrderActivity.class);					
				}
				else if(text.equals("我的帐户")){
					if(!logged){
						Toast.makeText(PersonalCenterActivity.this, "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(PersonalCenterActivity.this,TestActivity.class));
						return;
					};
					Toast.makeText(getBaseContext(), text, 2000).show();
//					intent.setClass(getBaseContext(), AccountActivity.class);			
				}
				else if(text.equals("收货地址")){					
					Toast.makeText(getBaseContext(), text, 2000).show();
					intent.setClass(getBaseContext(), AddressManageActivity.class);
				}
				else if(text.equals("我的收藏")){		
					if(!logged){
						Toast.makeText(PersonalCenterActivity.this, "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(PersonalCenterActivity.this,TestActivity.class));
						return;
					};
					Toast.makeText(getBaseContext(), text, 2000).show();
					intent.setClass(getBaseContext(), CollectionActivity.class);
				}
				else if(text.equals("浏览记录")){
					Toast.makeText(getBaseContext(), text, 2000).show();
					intent.setClass(getBaseContext(), HistoryActivity.class);
				}
				else{
					Toast.makeText(getBaseContext(), text, 2000).show();
					intent.setClass(getBaseContext(), SettingTabActivity.class);
				}
				startActivity(intent);
				overridePendingTransition(R.anim.anim_fromright_toup6, R.anim.anim_down_toleft6);
			}
		});
		
		((TextView)findViewById(R.id.textView1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(PersonalCenterActivity.this,TestActivity.class));
				
			}
		});
		((TextView)findViewById(R.id.textView2)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				startActivity(new Intent(PersonalCenterActivity.this,LocationActivity.class));
			
			}
		});
		((TextView)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(PersonalCenterActivity.this,TestAddress.class));
			
			}
		});
		
	}
	@Override
	protected void onStart() {
		super.onStart();
//		((TextView)findViewById(R.id.textView3)).setText(CommonUtil.formatFileSize(Runtime.getRuntime().totalMemory()));
	}
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("=========onresume");
		if(bmp_2 == null){
			System.out.println("=====bmp_2 == null====onresume");
			
			bmp_2 = BitmapFactory.decodeResource(getResources(), R.drawable.bg_gt);
			bc_2.setImageBitmap(bmp_2);
			System.out.println("==1==mybitmap2=="+bmp_2);
		}
		if(bmp_1 == null){
			System.out.println("=====bmp_1 == null====onresume");
			bc_1.setBackgroundResource(R.drawable.bg_g);			
			bmp_1 = ((BitmapDrawable)bc_1.getBackground()).getBitmap();
			System.out.println("==1==mybitmap1=="+bmp_1);
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("=========onpause");
		if(bmp_1!=null){
			bc_1.setBackgroundDrawable(null);
			bmp_1.recycle();
			bmp_1 = null;
			System.out.println("==2==mybitmap1=="+bmp_1);
		}
		if(bmp_2!=null){
			bmp_2.recycle();
			bmp_2 = null;
			System.out.println("==2==mybitmap2=="+bmp_2);
		}
		//注意一个问题 该图片被回收了 1.但是activity跳转后再跳回还是显示的 2.其他地方activity若引用该   同名图片    的话会报Cannot draw recycled bitmaps
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
