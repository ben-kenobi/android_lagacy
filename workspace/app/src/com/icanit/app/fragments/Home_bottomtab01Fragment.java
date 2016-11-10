package com.icanit.app.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icanit.app.ChangeCommunityActivity;
import com.icanit.app.HomePageActivity;
import com.icanit.app.R;
import com.icanit.app.adapter.MyMerchantLvAdapter;
import com.icanit.app.entity.AppCommunity;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.ListenerUtil;
import com.icanit.bdmapversion2.update.LoadingActivity;

public class Home_bottomtab01Fragment extends AbstractRadioBindFragment{
	private DataService dataService;
	private AppCommunity appCommunity;
	private TextView tv ;
	private ListView lv;
	private View self;
	private int resId=R.layout.fragment4home_01_merchant;
	private EditText editText;
	private ImageButton textDisposer,backButton,mapButton;
	private MyMerchantLvAdapter merchantLvAdapter;
	private ItemClickCallBack callback;
	public static final int NOTIFY_LV_ADAPTER=1;
	private  Handler handler = new Handler(Looper.getMainLooper()){
		public void handleMessage(Message msg) {
			if(msg.what==NOTIFY_LV_ADAPTER)
				merchantLvAdapter.notifyDataSetChanged();
		}
	};
	public Home_bottomtab01Fragment(){};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			init();
			initLv();
			bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof ItemClickCallBack)
		callback=(ItemClickCallBack)activity;
	}
	
	public void updateAfterCommunityChanged(){
		
	}
	public void onResume() {
		super.onResume();
			AppCommunity appCommunityTemp=AppUtil.getSharedPreferencesUtilInstance().
					getReservedCommunityInfo();
			if(appCommunity!=null&&appCommunity.id==appCommunityTemp.id) return ;
			appCommunity=appCommunityTemp;
			tv.setText(appCommunity.commName);
			DataService.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						merchantLvAdapter.setMerchantList(dataService.getStoresInfoByCommunityId(appCommunity.id));
						handler.sendEmptyMessage(1);
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
	}
	private void bindListeners(){
		backButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getActivity().startActivity(getActivity().getIntent().setClass(getActivity(), 
						HomePageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
		tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				getActivity().startActivity(getActivity().getIntent().setClass(getActivity().getApplicationContext(), 
						ChangeCommunityActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			}
		});
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);
		
		mapButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
//				Intent intent = new Intent(getActivity(),BMapActivity.class).putExtra
//						(IConstants.COMMUNITY_INFO,appCommunity);
				Intent intent = new Intent(getActivity(),LoadingActivity.class);
				getActivity().startActivity(intent);
			}
		});
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return self;
	}
	private void init() throws AppException{
		self = getActivity().getLayoutInflater().inflate(resId,null ,false);
		tv=(TextView)self.findViewById(R.id.textView2);
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(getActivity());
		lv=(ListView)self.findViewById(R.id.listView1);
		editText=(EditText)self.findViewById(R.id.editText1);
		textDisposer=(ImageButton)self.findViewById(R.id.imageButton1);
		backButton=(ImageButton)self.findViewById(R.id.imageButton2);
		mapButton=(ImageButton)self.findViewById(R.id.imageButton3);
	}
	private void initLv() throws AppException{
		lv.setAdapter(merchantLvAdapter=new MyMerchantLvAdapter(getActivity()));
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self, int position,
					long id) {
				callback.onItemClick(merchantLvAdapter.getMerchantList().get(position));
			}
		});
		lv.setOnScrollListener(new ListenerUtil.MyOnScrollListener(merchantLvAdapter));
	}
	
	public static interface ItemClickCallBack{
		void onItemClick(AppMerchant store);
	}
	
}
