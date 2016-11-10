package com.icanit.app_v2.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.icanit.app_v2.activity.AccountActivity;
import com.icanit.app_v2.activity.MainActivity;
import com.icanit.app_v2.activity.OrderListActivity;
import com.icanit.app_v2.util.AppUtil;
import com.what.yunbao.R;
import com.what.yunbao.address.AddressManageActivity;
import com.what.yunbao.collection.CollectionActivity;
import com.what.yunbao.history.HistoryActivity;
import com.what.yunbao.person.PCGridViewAdapter;
import com.what.yunbao.test.TestActivity;


public class Main_usercentral_Fragment extends AbstractRadioBindFragment {
	private LayoutInflater inflater;
	private int resId = R.layout.personal_center;
	private View self;
	private GridView gv;
	private PCGridViewAdapter gvAdapter;
	//add  later
//	private Bitmap bmp_1 = null;//背景 background
//	private Bitmap bmp_2 = null;//图片 src
	private LinearLayout bc_1;
	private ImageView bc_2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initGv();
		bindListeners();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup vg = (ViewGroup) self.getParent();
		if (vg != null)
			vg.removeAllViews();
		return self;
	}
	public void onResume() {
		super.onResume();
		if(AppUtil.getLoginUser()==null){
			((MainActivity)getActivity()).setHome_bottomtab03Fragment(this);
		}
	}
	private void init() {
		self = (inflater = LayoutInflater.from(getActivity())).inflate(resId,
				null, false);
		bc_1 = (LinearLayout) self.findViewById(R.id.ll_recycle_bmp);
		bc_2 = (ImageView) self.findViewById(R.id.iv_recycle_bmp);
		gv= (GridView) self.findViewById(R.id.gv_person_option);
		
	}

	private void initGv() {
		final String [] gridViewTextArr = getResources().getStringArray(R.array.pcgridview_text);
		Integer [] gridViewImgArr = new Integer [] {android.R.drawable.ic_menu_today,android.R.drawable.ic_dialog_info,
													android.R.drawable.ic_menu_save,android.R.drawable.btn_star_big_off,
													android.R.drawable.ic_dialog_dialer,android.R.drawable.ic_menu_manage};
		gvAdapter =new PCGridViewAdapter(getActivity(), gridViewTextArr, gridViewImgArr);
		gv.setAdapter(gvAdapter);
	
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				String text = gridViewTextArr[arg2];
				Intent intent = new Intent();
				
				boolean logged  = true;//未登录
				if(text.equals("我的订单")){
					if(!logged){
						Toast.makeText(getActivity(), "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(getActivity(),TestActivity.class));
						return;
					};
					intent.setClass(getActivity().getBaseContext(), OrderListActivity.class);					
				}
				else if(text.equals("我的帐户")){
					if(!logged){
						Toast.makeText(getActivity().getBaseContext(), "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(getActivity().getBaseContext(),TestActivity.class));
						return;
					};
					intent.setClass(getActivity().getBaseContext(), AccountActivity.class);			
				}
				else if(text.equals("收货地址")){					
					intent.setClass(getActivity().getBaseContext(), AddressManageActivity.class);
				}
				else if(text.equals("我的收藏")){		
					if(!logged){
						Toast.makeText(getActivity(), "未登录，跳转到登录界面", 2000).show();
						startActivity(new Intent(getActivity(),TestActivity.class));
						return;
					};
					intent.setClass(getActivity().getBaseContext(), CollectionActivity.class);
				}
				else if(text.equals("浏览记录")){
					intent.setClass(getActivity().getBaseContext(), HistoryActivity.class);
				}
				else{
					intent=null;
//					Toast.makeText(getActivity().getBaseContext(), text, 2000).show();
//					intent.setClass(getActivity().getBaseContext(), SettingTabActivity.class);
				}
				if(intent!=null)
				startActivity(intent);
			}
		});
		
	}
	private void bindListeners(){
	}
	
	
	
	public  void onPause() {
		super.onPause();
	}

}
