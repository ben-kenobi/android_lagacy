package com.icanit.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.icanit.app.adapter.MyMerchandiseLvAdapter;
import com.icanit.app.adapter.MyPagerAdapter;
import com.icanit.app.common.IConstants;
import com.icanit.app.entity.AppGoods;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.ImageUtil;
import com.icanit.app.util.ListenerUtil;
import com.icanit.app.util.ListenerUtil.MyOnScrollListener;

public class MerchandiseListActivity extends Activity {
	private TextView storeName, minCost, phoneNum, location;
//	private ImageView storeSnap;
	private RatingBar rating;
	private ImageButton toggle,backButton,forwardToPay;
	private ListView lv;
	private ViewPager viewPager;
	private FrameLayout container;
	private MyPagerAdapter pagerAdapter;
	private MyMerchandiseLvAdapter lvAdapter;
	private AppMerchant storeInfo;
	private Handler handler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				lvAdapter.notifyDataSetChanged();
				pagerAdapter.notifyDataSetChanged();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchandise_list);
		try {
			init();
			initlv();
			bindListeners();
			findMerchandiseList();
			fillStoreInfo();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		AppMerchant merchant = (AppMerchant) intent
				.getSerializableExtra(IConstants.STORE_INFO);
		if (storeInfo != null && storeInfo.id == merchant.id)
			return;
		storeInfo = merchant;
		findMerchandiseList();
		try {
			fillStoreInfo();
		} catch (AppException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void findMerchandiseList() {
		// if(storeInfo!=null&&storeInfo.id==merchant.id) return;
		// this.storeInfo=merchant;

		DataService.THREAD_POOL.submit(new Runnable() {
			public void run() {
				List<AppGoods> list = null;
				try {
					list = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(
									MerchandiseListActivity.this)
							.getProductsInfoByStoreId(storeInfo.id);
				} catch (AppException e) {
					e.printStackTrace();
				}
				lvAdapter.setGoodsList(list);
				pagerAdapter.setGoodsList(list);
				handler.sendEmptyMessage(1);
			}
		});

	}

	// +++++++++++++++++++++++++++++++++++++++++++++++++
	private void fillStoreInfo() throws AppException {
		Log.d("errorTag", "storeInfo=" + storeInfo
				+ " @home_bottomtab02  fillStoreInfo()");
		if (storeInfo != null) {
//			ImageUtil.asyncDownloadImageAndShow(storeSnap, storeInfo.pic, this,
//					false);
			Log.d("errorTag", "storeNameView=" + storeName
					+ "  @home_bottomtab02Fragment");
			storeName.setText(storeInfo.merName);
			minCost.setText("ÆðËÍ:£¤" + storeInfo.minCost);
			phoneNum.setText(storeInfo.phone);
			location.setText(storeInfo.location);
			rating.setRating(3.8f);
		}
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				container.removeAllViews();
				lv.setEnabled(false);
				viewPager.setEnabled(true);
				pagerAdapter.updateView(viewPager,position);
				viewPager.setCurrentItem(position, true);
				container.addView(viewPager,LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				toggle.setImageResource(android.R.drawable.stat_notify_sync_noanim);
				
			}
		});
		/**
		 * toggle button is temporary removed;
		 */
		toggle.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				container.removeAllViews();
				if (viewPager.isEnabled()) {
					viewPager.setEnabled(false);
					lv.setEnabled(true);
					container.addView(lv, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					toggle.setImageResource(android.R.drawable.ic_menu_view);
				} else {
					viewPager.setEnabled(true);
					lv.setEnabled(false);
					container.addView(viewPager, LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);
					toggle.setImageResource(android.R.drawable.stat_notify_sync_noanim);
					pagerAdapter.updateView(viewPager,-1);
				}
			}
		});
		
		
		
		forwardToPay.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MerchandiseListActivity.this,HomeActivity.class).
						setAction(IConstants.FORWARD_TO_PAY).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
	}

	private void initlv() throws AppException {
		lv.setAdapter(lvAdapter = new MyMerchandiseLvAdapter(this));
		lv.setOnScrollListener(new MyOnScrollListener(lvAdapter));
		viewPager.setAdapter(pagerAdapter = new MyPagerAdapter(this));
		viewPager.setOnPageChangeListener(new ListenerUtil.MyOnPageChangeListener(
						pagerAdapter));
		viewPager.setEnabled(false);
	}

	private void init() throws AppException {
		lv = (ListView) findViewById(R.id.listView1);
		forwardToPay=(ImageButton)findViewById(R.id.imageButton1);
		backButton=(ImageButton)findViewById(R.id.imageButton3);
		toggle = (ImageButton) findViewById(R.id.imageButton2);
//		storeSnap = (ImageView) findViewById(R.id.imageView1);
		rating = (RatingBar) findViewById(R.id.ratingBar1);
		storeName = (TextView) findViewById(R.id.textView1);
		minCost = (TextView) findViewById(R.id.textView2);
		phoneNum = (TextView) findViewById(R.id.textView3);
		location = (TextView) findViewById(R.id.textView4);
		container = (FrameLayout) findViewById(R.id.frameLayout1);
		viewPager = (ViewPager) LayoutInflater.from(this).inflate(
				R.layout.viewpager_withstrip, container, false);
		PagerTabStrip pagerTab = (PagerTabStrip) viewPager
				.findViewById(R.id.pagertab);
		pagerTab.setTabIndicatorColor(Color.rgb(0x87,0xce, 0xfa));
		pagerTab.setDrawFullUnderline(true);
		pagerTab.setBackgroundColor(getResources().getColor(R.color.azure));
		pagerTab.setTextSpacing(10);
		storeInfo = (AppMerchant) getIntent().getSerializableExtra(
				IConstants.STORE_INFO);
	}
}
