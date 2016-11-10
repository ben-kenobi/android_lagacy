package com.icanit.app_v2.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.adapter.MerchandizeCateLvAdapter;
import com.icanit.app_v2.adapter.MyMerchandiseLvAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppCategory;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.AutoChangeCheckRadioGroup;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DeviceInfoUtil;

public class MerchandizeListActivity extends Activity implements
		OnClickListener, TextWatcher {
	private AppMerchant merchant;
	private ImageButton backButton, searchButton;
	private FrameLayout shoppingCart;
	private View lvHeader;
	private TextView category, search, collection, recommend, location,
			minCost, postscript, title, cartItemQuantity;
	private ListView lv;
	private MyMerchandiseLvAdapter merLvAdapter;
	private MerCateChooseDialogBuilder dialog01;
	private ImageView animateImage;
	private ViewGroup container;
	private FrameLayout adContainer;
	private ViewPager adContent;
	private AutoChangeCheckRadioGroup adIndicator;
	private Button textDisposer;
	private ViewGroup vg;
	private EditText searchField;
	private Handler handler = new Handler(Looper.getMainLooper());

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchandize_list);
		try {
			init();
			AppUtil.getServiceFactory().getUserBrowsingDaoInstance(this)
					.addToBrowsedMerchant(merchant, AppUtil.getLoginPhoneNum());
			initLv();
			bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	protected void onResume() {
		super.onResume();
		fillMerchantInfo();
		if (adIndicator != null)
			adIndicator.restartAd();
		if (merLvAdapter != null) {
			merLvAdapter.updateCartItemQuantity();
			merLvAdapter.notifyDataSetChanged();
		}
	}

	protected void onPause() {
		super.onPause();
		if (adIndicator != null)
			adIndicator.stopAd();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (dialog01 != null)
			dialog01.dismissDialog();
	}

	private void initDialog() {
		if (merLvAdapter == null || merLvAdapter.getGoodsList() == null)
			return;
		dialog01 = new MerCateChooseDialogBuilder(this);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				final List<AppCategory> cateList = new ArrayList<AppCategory>();
				// final List<AppGoods>
				// goodsList=AppUtil.getServiceFactory().getDataServiceInstance(MerchandizeListActivity.this).
				// getProductsInfoByStoreId(merchant.id);
				setCateListsCount(cateList, merLvAdapter.getGoodsList());
				handler.post(new Runnable() {
					public void run() {
						dialog01.setMerCateList(cateList);
						// merLvAdapter.setGoodsList(goodsList);
					}
				});
			}
		});
	}

	private void setCateListsCount(List<AppCategory> cateList,
			List<AppGoods> goodsList) {
		AppGoods goods;
		for (int i = 0; i < goodsList.size(); i++) {
			goods = goodsList.get(i);
			if(!cateContains(cateList, goods.cateId)){
				cateList.add(new AppCategory(goods.cateId,goods.cateName,0,0,1));
			}
		}
	}
	
	private boolean cateContains(List<AppCategory> cateList,int cateid){
		for(int i=0;i<cateList.size();i++){
			if(cateList.get(i).id==cateid){
				cateList.get(i).count++;
				return true;
			}
		}
		return false;
	}

	private void fillMerchantInfo() {
		title.setText(merchant.merName);
		location.setText(merchant.location);
		minCost.setText("起送价格："+AppUtil.formatMoney(merchant.deliverPrice));
		postscript.setText(merchant.detail);
		collection.setSelected(AppUtil.hadCollected(merchant.id));
		if (AppUtil.hadRecommended(merchant.id)) {
			 recommend.setText(merchant.recommend + 1 + "");
			recommend.setSelected(true);
		} else {
			 recommend.setText(merchant.recommend + "");
			recommend.setSelected(false);
		}
	}

	private void init() throws AppException {
		lvHeader = LayoutInflater.from(this).inflate(
				R.layout.header4lv_merchandizelist, null, false);
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		shoppingCart = (FrameLayout) findViewById(R.id.frameLayout1);
		category = (TextView) lvHeader.findViewById(R.id.textView1);
		search = (TextView) lvHeader.findViewById(R.id.textView2);
		collection = (TextView) lvHeader.findViewById(R.id.textView3);
		recommend = (TextView) lvHeader.findViewById(R.id.textView4);
		location = (TextView) lvHeader.findViewById(R.id.textView5);
		minCost = (TextView) lvHeader.findViewById(R.id.textView6);
		postscript = (TextView) lvHeader.findViewById(R.id.textView7);
		searchButton = (ImageButton) lvHeader.findViewById(R.id.imageButton1);
		searchField = (EditText) lvHeader.findViewById(R.id.editText1);
		textDisposer = (Button) lvHeader.findViewById(R.id.button1);
		vg = (ViewGroup) lvHeader.findViewById(R.id.relativeLayout1);
		title = (TextView) findViewById(R.id.textView9);
		cartItemQuantity = (TextView) findViewById(R.id.textView10);
		lv = (ListView) findViewById(R.id.listView1);
		animateImage = (ImageView) findViewById(R.id.imageView1);
		container = (ViewGroup) findViewById(R.id.relativeLayout2);
		adContainer = (FrameLayout) lvHeader.findViewById(R.id.frameLayout3);
		merchant = (AppMerchant) getIntent().getSerializableExtra(
				IConstants.MERCHANT_KEY);
	}

	private void findAdGoodsList(List<AppGoods> goodsList) throws AppException {
		adContainer.removeAllViews();
		if (goodsList == null)
			return;
		final List<AppGoods> adGoodsList = new ArrayList<AppGoods>();
		for (int i = 0; i < goodsList.size(); i++) {
			AppGoods goods = goodsList.get(i);
			// TODO advertisement
			// if (goods.advertStatus == 1)
			// adGoodsList.add(goods);
		}
		if (adGoodsList == null || adGoodsList.isEmpty()) {
			AppUtil.setNoAdImage(adContainer, this);
		} else {
			adContent = AppUtil.setMerchandizeAdPagers(adContainer, adContent,
					adGoodsList, MerchandizeListActivity.this);
			Object obj = adContent.getTag();
			if (obj != null)
				adIndicator = (AutoChangeCheckRadioGroup) obj;
		}
	}

	private void initLv() throws AppException {
		lv.addHeaderView(lvHeader, null, false);
		lv.setAdapter(merLvAdapter = new MyMerchandiseLvAdapter(this,
				cartItemQuantity, container, animateImage, merchant));
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				try {
					final List<AppGoods> goodsList = AppUtil
							.getServiceFactory()
							.getDataServiceInstance(
									MerchandizeListActivity.this)
							.getProductsInfoByStoreId(merchant.id);
					handler.post(new Runnable() {
						public void run() {
							try {
								findAdGoodsList(goodsList);
							} catch (AppException e) {
								e.printStackTrace();
							}
							merLvAdapter.setGoodsList(goodsList);
						}
					});
				} catch (final AppException e) {
					e.printStackTrace();
				}
			}
		});
		/*
		 * lv.setOnItemClickListener(new OnItemClickListener() { public void
		 * onItemClick(AdapterView<?> parent, View v, int position, long id) {
		 * shoppingCartService
		 * .addCartItem(merLvAdapter.getGoodsListByCate().get(
		 * position),merchant); ImageView imageView =
		 * (ImageView)v.findViewById(R.id.imageView1); try {
		 * AnimationUtil.startBuyingAnimation
		 * (animateImage,rlContainer,imageView,
		 * shoppingCart,MerchandizeListActivity.this); } catch (Exception e) {
		 * e.printStackTrace(); }
		 * 
		 * } });
		 */
		AppUtil.getListenerUtilInstance().setOnScrollListener(lv, merLvAdapter);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		shoppingCart.setOnClickListener(this);
		category.setOnClickListener(this);
		recommend.setOnClickListener(this);
		collection.setOnClickListener(this);
		textDisposer.setOnClickListener(this);
		searchField.addTextChangedListener(this);
		AppUtil.bindSearchEditTextTrigger(search, searchButton, vg);
	}

	// private List<Map<String,Object>> getCateData(){
	// List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
	// String[] sary = new String[]{"全部商品","未分类","饮料","干调","方便面",
	// "生活用品","小食品","巧克力","瓜子","饼干","烟酒","国产烟"};
	// for(int i =0;i<sary.length;i++){
	// Map<String,Object> map = new HashMap<String,Object>();
	// map.put("cateName", sary[i]);map.put("count", (int)(Math.random()*1000));
	// data.add(map);
	// }
	// return data;
	// }

	public void showMerchandizeDetail(AppGoods goods) {
		Intent intent = new Intent()
				.setClassName(getPackageName(),
						MerchandizeDetailActivity.class.getCanonicalName())
				.putExtra(IConstants.GOODS_KEY, goods)
				.putExtra(IConstants.MERCHANT_KEY, merchant);
		startActivity(intent);
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */

	class MerCateChooseDialogBuilder {
		private Dialog merCateChooseDialog;
		private MerchandizeCateLvAdapter adapter;

		public MerCateChooseDialogBuilder(Context context) {
			merCateChooseDialog = new Dialog(context, R.style.dialogStyle);
			merCateChooseDialog.setCanceledOnTouchOutside(true);
			View contentView = LayoutInflater.from(context).inflate(
					R.layout.dialog_merchandize_cates, null, false);
			merCateChooseDialog.setContentView(contentView, new LayoutParams(
					(int) (DeviceInfoUtil.getScreenWidth() * 0.9),
					(int) (DeviceInfoUtil.getScreenHeight() * 0.8)));
			ListView lv = (ListView) contentView.findViewById(R.id.listView1);
			lv.setAdapter(adapter = new MerchandizeCateLvAdapter(context));
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Toast.makeText(MerchandizeListActivity.this,
							adapter.getMerCateList().get(position).cateName,
							Toast.LENGTH_LONG).show();
					merCateChooseDialog.dismiss();
					merLvAdapter.classifyGoodsListByCateId(adapter
							.getMerCateList().get(position));
				}
			});
		}

		public void setMerCateList(List<AppCategory> merCateList) {
			System.out.println(merCateList + "  @merchantdizeListActivity");
			adapter.setMerCateList(merCateList);
		}

		public void showDialog() {
			merCateChooseDialog.show();
		}

		public void dismissDialog() {
			merCateChooseDialog.dismiss();
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if (after == 0) {
			if (textDisposer != null)
				textDisposer.setVisibility(View.GONE);
		} else {
			if (textDisposer != null)
				textDisposer.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTextChanged(CharSequence content, int start, int before,
			int count) {
		merLvAdapter.searchGoods(content);
	}

	@Override
	public void onClick(View v) {
		if (v == shoppingCart) {
			Intent intent = new Intent(MerchandizeListActivity.this,
					ShoppingCartSubstituteActivity.class);
			startActivity(intent);
		} else if (v == category) {
			if (dialog01 == null) {
				initDialog();
			}
			if (dialog01 != null)
				dialog01.showDialog();
		} else if (v == recommend) {
			AppUtil.recommend(merchant, recommend, MerchandizeListActivity.this);
		} else if (v == collection) {
			try {
				AppUtil.handlerCollectionEvent(merchant, collection,
						MerchandizeListActivity.this);
			} catch (AppException e) {
				e.printStackTrace();
			}
		} else if (v == textDisposer) {
			searchField.setText("");
		}

	}

}
