package com.icanit.app_v2.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.action.BasePopAction;
import com.icanit.app_v2.activity.MainActivity;
import com.icanit.app_v2.activity.MerchandizeListActivity;
import com.icanit.app_v2.activity.RegisterActivity;
import com.icanit.app_v2.adapter.AlphabetLvAdapter;
import com.icanit.app_v2.adapter.CommunityDistrictLvAdapter;
import com.icanit.app_v2.adapter.CommunityLvAdapter;
import com.icanit.app_v2.adapter.MyMerchantLvAdapter;
import com.icanit.app_v2.adapter.MySimpleAdapter;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppArea;
import com.icanit.app_v2.entity.AppCommunity;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.EntityMapFactory;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.service.DataService;
import com.icanit.app_v2.ui.AutoChangeCheckRadioGroup;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.PinyinUtil;
import com.icanit.app_v2.util.SortUtil.ComparatorParamPair;
import com.icanit.bdmapversion2.activity.LocationActivity;

public class Main_community_Fragment extends AbstractRadioBindFragment implements OnClickListener,TextWatcher{
	private DataService dataService;
	private AppCommunity appCommunity;
	private LayoutInflater inflater;
	private TextView section,province,community;
	private ListView lv;
	private View self;
	private EditText searchField;
	private int resId=R.layout.fragment4main_community; 
	private ImageButton mapButton,searchButton;
	private Button screenButton,textDisposer;
	private MyMerchantLvAdapter merchantLvAdapter;
	private ItemClickCallBack callback;
	private BasePopAction pop1;
	private ScreenMerchantDialogBuilder dialog;
	private FrameLayout adContainer,lvContainer,guideContainer;
	private ViewPager adContent;
	private AutoChangeCheckRadioGroup adIndicator;
	private ViewGroup vg;
	private View register,login,chooseComm,mapLocation;
	
	
	private static Handler handler =IConstants.MAIN_HANDLER; 
	public Main_community_Fragment(){};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			init();initLv();	bindListeners();
			AppCommunity comm=AppUtil.getSharedPreferencesUtilInstance().
			getReservedCommunityInfo();
			if(comm==null){
				guideContainer.setVisibility(View.VISIBLE);
				lvContainer.setVisibility(View.GONE);
				adContainer.setVisibility(View.GONE);
				setupGuide();
			}else{
				setAppCommunity(comm);
			}
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof ItemClickCallBack)
		callback=(ItemClickCallBack)activity;
	}
	
	public void onResume() {
		super.onResume();
		if(adIndicator!=null)
		adIndicator.restartAd();
	}
	@Override
	public void onPause() {
		super.onPause();
		if(adIndicator!=null)
		adIndicator.stopAd();
	}
	private void setAppCommunity(AppCommunity community){
		if(community==null||(this.appCommunity!=null&&this.appCommunity.id==community.id))return;
		guideContainer.setVisibility(View.GONE);
		lvContainer.setVisibility(View.VISIBLE);
		adContainer.setVisibility(View.VISIBLE);
		this.appCommunity=community;
		onAppCommunityBeSet();
	}
	private void onAppCommunityBeSet(){
		loadStoresInfo(appCommunity.id);
		community.setText("My社区\n"+appCommunity.commName);
	}
	private void findAdvertMerchant(List<AppMerchant> merchantList) throws AppException{
		adContainer.removeAllViews();
		if(merchantList==null) return;
		List<AppMerchant> advertMerchantList=new ArrayList<AppMerchant>();
		for(int i=0;i<3;i++){
//			AppMerchant merchant=merchantList.get(i);
			//TODO advertisement
				advertMerchantList.add(new AppMerchant());
		}
		if(advertMerchantList!=null&&!advertMerchantList.isEmpty()){
			adContent=AppUtil.setMerchantAdPagers(adContainer, adContent, advertMerchantList, getActivity());
			Object obj = adContent.getTag();
			adIndicator=(AutoChangeCheckRadioGroup)obj;
		}else{
			AppUtil.setNoAdImage(adContainer, getActivity());
		}
	}
	
	private void loadStoresInfo(final String commId){
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				try {
					//TODO obtain  dynamic  commId
					final List<AppMerchant> merchantList=(dataService.getStoresInfoByCommunityId(commId));
					handler.post(new Runnable(){
						public void run(){
							merchantLvAdapter.setMerchantList(merchantList);
							try {
								findAdvertMerchant(merchantList);
								merchantLvAdapter.sortBy(new ComparatorParamPair("id", true),true);
//								pop2.resetTrigger();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
		});
	}
	private void bindListeners(){
		mapButton.setOnClickListener(this);
		screenButton.setOnClickListener(this);
		AppUtil.bindSearchEditTextTrigger(searchButton,null,vg);
		textDisposer.setOnClickListener(this);
		searchField.addTextChangedListener(this);
	}
	
	private void setupGuide(){
		register=self.findViewById(R.id.view1);
		login=self.findViewById(R.id.view2);
		chooseComm=self.findViewById(R.id.view3);
		mapLocation=self.findViewById(R.id.view4);
		mapLocation.setOnClickListener(this);
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {
				if(v==chooseComm){
					pop1.showAsDropDown();
				}else if(v==register){
					Intent intent = new Intent(getActivity(),
							RegisterActivity.class);
					getActivity().startActivity(intent);
					AppUtil.putIntoApplication(
							IConstants.DESTINATION_AFTER_REGISTER,
							MainActivity.class);
				}else if(v==login){
					((MainActivity)getActivity()).toHome_bottomtab03Fragment(Main_community_Fragment.this);
				}
			}
		};
		chooseComm.setOnClickListener(listener);
		register.setOnClickListener(listener);
		login.setOnClickListener(listener);
	}
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup vg=(ViewGroup)self.getParent();
		if(vg!=null)
		vg.removeAllViews();
		return self;
	}
	private void init() throws AppException{
		self = (inflater=getActivity().getLayoutInflater()).inflate(resId,null ,false);
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(getActivity());
		lv=(ListView)self.findViewById(R.id.listView1);
		section=(TextView)self.findViewById(R.id.textView1);
		province=(TextView)self.findViewById(R.id.textView2);
		community=(TextView)self.findViewById(R.id.textView3);
//		chooseAllButton=(TextView)self.findViewById(R.id.textView4);
//		chooseCommunityButton=(TextView)self.findViewById(R.id.textView5);
//		chooseSortType=(TextView)self.findViewById(R.id.textView6);
		mapButton=(ImageButton)self.findViewById(R.id.imageButton1);
		searchButton=(ImageButton)self.findViewById(R.id.imageButton2);
		searchField=(EditText)self.findViewById(R.id.editText1);
		screenButton=(Button)self.findViewById(R.id.button1);
		textDisposer=(Button)self.findViewById(R.id.button2);
		adContainer = (FrameLayout)self.findViewById(R.id.frameLayout1);
		lvContainer = (FrameLayout)self.findViewById(R.id.frameLayout2);
		guideContainer = (FrameLayout)self.findViewById(R.id.frameLayout3);
		vg=(ViewGroup)self.findViewById(R.id.relativeLayout1);
//		pop0=new Pop0Action(inflater,chooseAllButton);
		pop1=new Pop1Action(getActivity(),community);
//		pop2=new Pop2Action(inflater,chooseSortType);
		
	}
	private void initLv() throws AppException{
		lv.setAdapter(merchantLvAdapter=new MyMerchantLvAdapter(getActivity()));
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self, int position,
					long id) {
				Intent intent=new Intent(getActivity(),MerchandizeListActivity.class).putExtra(IConstants.MERCHANT_KEY, 
						merchantLvAdapter.getScreenedMerchantList().get(position));
				getActivity().startActivity(intent);
			}
		});
		AppUtil.getListenerUtilInstance().setOnScrollListener(lv,merchantLvAdapter);
	}
	
	public static interface ItemClickCallBack{
		void onItemClick(AppMerchant store);
	}
	
	
	/**
	 * 
	 * @author innerclass dialog
	 *
	 */
	class ScreenMerchantDialogBuilder implements OnClickListener{
		private Dialog screenMerchantDialog;
		private ImageButton deliveryOnly,reservationOnly;
		private Button confirmation;
		private int resId=R.layout.dialog_merchant_screening;
		public ScreenMerchantDialogBuilder(Context context){
			screenMerchantDialog=new Dialog(context,R.style.dialogStyle);
			screenMerchantDialog.setCanceledOnTouchOutside(true);
			View contentView=LayoutInflater.from(context).inflate(resId, null,false);
			screenMerchantDialog.setContentView(contentView);
			deliveryOnly=(ImageButton)contentView.findViewById(R.id.imageButton1);
			reservationOnly=(ImageButton)contentView.findViewById(R.id.imageButton2);
			confirmation=(Button)contentView.findViewById(R.id.button1);
			AppUtil.setOnClickListenerForSelectionView(deliveryOnly);
			AppUtil.setOnClickListenerForSelectionView(reservationOnly);
			confirmation.setOnClickListener(this);
		}
		
		public void showDialog(){
			screenMerchantDialog.show();
		}

		public void onClick(View v) {
			if(merchantLvAdapter==null) return;
			merchantLvAdapter.screenMerchantList(deliveryOnly.isSelected(),reservationOnly.isSelected());
			screenMerchantDialog.dismiss();
		}
		
	}
	
	
	
	/**
	 * define inner class for popupWindows  as
	 * command pattern 
	 */
/*	private class Pop0Action extends BasePopAction{
		private String[] sary;
		private MySimpleAdapter msa;
		private ListView sublv;
		
		public Pop0Action(LayoutInflater inflater, TextView trig) throws AppException {
			super(R.layout.pop0_community, inflater,trig);
			execute();
		}
		public void execute() throws AppException {
			ListView lv = (ListView)contentView.findViewById(R.id.listView1);
			sublv = (ListView)contentView.findViewById(R.id.listView2);
			lv.setAdapter(msa=new MySimpleAdapter(getActivity(), getPop0ContentList(), 
					R.layout.item4lv_community_pop0,new String[]{"pic","title","num","arrow"}, 
					new int[]{R.id.imageView1,R.id.textView1,R.id.textView2,R.id.imageView2}));
			lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					msa.setSelected(position);
					msa.notifyDataSetChanged();
					LayoutParams lps=sublv.getLayoutParams();
					lps.width=LayoutParams.MATCH_PARENT;
					sublv.setLayoutParams(lps);
					setSubLvAdapter(sublv,position);
				}
			});
		}
		
		private void setSubLvAdapter(ListView lv,int position){
			final String[] sary;
			lv.setAdapter(new ArrayAdapter(getActivity(),R.layout.simple_list_item,
					sary=new String[]{"热门","今日新单","零食饮料","美食","生活用品","粮油生鲜"}));
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					pop.dismiss();
					trigger.setText(sary[position]);
				}
			});
		}
		private  List<Map<String,Object>>  getPop0ContentList(){
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			sary=new String[]{"全部","热门","今日新单","零食饮料","美食","生活用品","粮油生鲜"};
			for(int i=0;i<sary.length;i++){
				list.add(EntityMapFactory.generateEntity(android.R.drawable.ic_btn_speak_now, 
						sary[i], 1788,R.drawable.ic_arrow));
			}
			return list;
		}

		public void resetTrigger() {
		}
	}
	
	*/
	
	private class Pop1Action extends BasePopAction{
		private CommunityDistrictLvAdapter adapter,subAdapter;
		private CommunityLvAdapter sub2Adapter;
		private ListView lv,sublv,sub2Lv,alphabetLv;
		private AlphabetLvAdapter alphabetLvAdapter;
		private Context context;
		public Pop1Action(Context context, TextView trig) throws AppException {
			super(R.layout.pop1_community, LayoutInflater.from(context), trig);
			this.context =context;
			execute();
		}
		public void execute() throws AppException {
			lv=(ListView)contentView.findViewById(R.id.listView1);
			sublv=(ListView)contentView.findViewById(R.id.listView2);
			sub2Lv=(ListView)contentView.findViewById(R.id.listView3);
			alphabetLv=(ListView)contentView.findViewById(R.id.listView4);
			lv.setAdapter(adapter=new CommunityDistrictLvAdapter(context,CommunityDistrictLvAdapter.TYPE_AREA));
			sublv.setAdapter(subAdapter=new CommunityDistrictLvAdapter(context,CommunityDistrictLvAdapter.TYPE_COMM));
			subAdapter.setSelectedColor(Color.rgb(0xdd, 0xdd, 0xdd));
			sub2Lv.setAdapter(sub2Adapter=new CommunityLvAdapter(context));
			alphabetLv.setAdapter(alphabetLvAdapter=new AlphabetLvAdapter(context));
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppArea> areaList=AppUtil.getServiceFactory().getDataServiceInstance(context).
								listAreasByParentId(IConstants.ROOTAREAID);
						handler.post(new Runnable(){
							public void run(){
								adapter.setAreaList(areaList);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
			
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if(adapter.selected==position) return;
					adapter.setSelected(position);
					refreshSubLvAdapter(position);
				}
			});
			sublv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if(subAdapter.selected==position) return;
					subAdapter.setSelected(position);
					refreshSub2LvAdapter(position);
				}
			});
			sub2Lv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					pop.dismiss();
					setAppCommunity(sub2Adapter.getCommunities().get(position));
					AppUtil.getSharedPreferencesUtilInstance().reserveCommunityInfo(sub2Adapter.getCommunities().get(position));
				}
			});
			alphabetLv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					String alpha=alphabetLvAdapter.getAlpahList().get(position);
					sub2Lv.setSelection(sub2Adapter.getPositionByAlpha(alpha));
				}
			});
			
		}
		private void refreshSubLvAdapter(final int position){
			subAdapter.setCommList(null);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppCommunity> commList=AppUtil.getServiceFactory().getDataServiceInstance(context)
								.findCommunitiesByAreaId(adapter.getAreaList().get(position).id);
						handler.post(new Runnable(){
							public void run(){
								subAdapter.setSelected(-1);
								subAdapter.setCommList(commList);
								sub2Adapter.setCommunities(null);
								alphabetLvAdapter.setAlphaList(null);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
		}
		private void refreshSub2LvAdapter(final int position){
			sub2Adapter.setCommunities(null);
			alphabetLvAdapter.setAlphaList(null);
			IConstants.THREAD_POOL.submit(new Runnable(){
				public void run(){
					try {
						final List<AppCommunity> communities=AppUtil.getServiceFactory().getDataServiceInstance(context)
								.findCommunitiesByAreaId(subAdapter.getSelectedAppComm().id);
						Collections.sort(communities, new Comparator<AppCommunity>() {
							public int compare(AppCommunity comm0, AppCommunity comm1) {
								return PinyinUtil.toPinyin(comm0.commName.charAt(0)).
										compareTo(PinyinUtil.toPinyin(comm1.commName.charAt(0)));
							}
						});
						final List<String> alphaList=new ArrayList<String>();
						for(int i=0;i<communities.size();i++){
							AppCommunity comm = communities.get(i);
							String alpha=PinyinUtil.toPinyin(comm.commName.charAt(0)).substring(0, 1);
							if(!alphaList.contains(alpha)) alphaList.add(alpha);
						}
						handler.post(new Runnable(){
							public void run(){
								alphabetLvAdapter.setAlphaList(alphaList);
								sub2Adapter.setCommunities(communities);
							}
						});
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
		}
		public void resetTrigger() {
		}
	}



	
/*	private class Pop2Action extends BasePopAction{
		private String[] sary ;
		private List<ComparatorParamPair> cpps=new ArrayList<ComparatorParamPair>();
		{
			cpps.add(new ComparatorParamPair("id",true));
			cpps.add(new ComparatorParamPair("minCost",true));
			cpps.add(new ComparatorParamPair("merName",true));
			cpps.add(new ComparatorParamPair("merName",false));
			cpps.add(new ComparatorParamPair("type",true));
		}
		public Pop2Action(LayoutInflater inflater, TextView trig) throws AppException {
			super(R.layout.pop2_community, inflater, trig);
			execute();
		}
		public void execute() throws AppException {
			ListView lv=(ListView)contentView.findViewById(R.id.listView1);
			 sary = getActivity().getResources().getStringArray(R.array.merchant_order); 
			lv.setAdapter(new ArrayAdapter<String>(getActivity(),R.layout.simple_list_item,sary));
			lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					pop.dismiss();
					trigger.setText(sary[position]);
					try {
						merchantLvAdapter.sortBy(cpps.get(position),false);
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					}
				}
			});
		}
		public void resetTrigger() {
			trigger.setText(sary[0]);
		}
	}*/

	@Override
	public void onClick(View v) {
		if(v==mapButton||v==mapLocation){
			Intent intent = new Intent(getActivity(),LocationActivity.class);
			getActivity().startActivity(intent);
		}else if(v==screenButton){
			if(dialog==null){
				dialog=new ScreenMerchantDialogBuilder(getActivity());
			}
			dialog.showDialog();
		}else if(v==textDisposer){
			searchField.setText("");
		}
		
	}
	@Override
	public void afterTextChanged(Editable s) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		if(after==0){
			if(textDisposer!=null)
			textDisposer.setVisibility(View.GONE);
		}else{
			if(textDisposer!=null)
			textDisposer.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void onTextChanged(CharSequence content, int start, int before, int count) {
		if(before==count) return;
		merchantLvAdapter.searchMerchantList(content);
	}
	
}
