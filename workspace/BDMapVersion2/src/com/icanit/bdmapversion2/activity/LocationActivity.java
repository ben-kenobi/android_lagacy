package com.icanit.bdmapversion2.activity;

import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.icanit.bdmapversion2.MapApplication;
import com.icanit.bdmapversion2.R;
import com.icanit.bdmapversion2.entity.Community;
import com.icanit.bdmapversion2.entity.Merchant;
import com.icanit.bdmapversion2.entity.MyMapView;
import com.icanit.bdmapversion2.sync.http.AsyncHttpClient;
import com.icanit.bdmapversion2.sync.http.AsyncHttpResponseHandler;
import com.icanit.bdmapversion2.util.CommonUtil;
import com.icanit.bdmapversion2.util.CommunityJSONParser;
import com.icanit.bdmapversion2.util.CommunityMarkJSONParser;
import com.icanit.bdmapversion2.util.ConfigCache;
import com.icanit.bdmapversion2.util.MyOverlayItem;

public class LocationActivity extends Activity {
	private static MyMapView mMapView = null;
	public static LocationData mLocData = new LocationData();
	// 定位相关
	private TextView tv_addr;
	private LocationClient mLocClient;
	
	private Button btn_loc;
	private RadioGroup rg;
	private Spinner spr_list_name;
	private Button btn_getlist;
	private ProgressBar pb_load;
	private Button btn_reposition;
	private LinearLayout btn_manager;
	private Button btn_route;
	private LinearLayout rl_commu_select;
	
	private MapController mMapController = null;
	private MyLocationOverlay myLocationOverlay = null;
	private MKMapViewListener mMapListener = null;
	private MKSearch mSearch;
	private MyOverlayItem mOv;
	private GraphicsOverlay graphicsOverlay = null; 
	private GeoPoint mSelectedPoint;
	private RouteOverlay routeOverlay;	
	private static GeoPoint cachePoint; 
	//标记
	private Drawable mark_a;
	private Drawable mark_b;
	private Drawable mark_c;
	
	private boolean isFollow = true;
    private boolean routeFlag = false;
    private boolean isNetworkAvailable = true;
    private String flag = "success";
    private static String netWorkState = "";
    private long lastTime = 0;

    private List<Community> cList = new ArrayList<Community>();     
    //用于缓存该社区对应商户信息
    private Map<Long,SoftReference<List<OverlayItem>>> markMap = 
    		Collections.synchronizedMap(new HashMap<Long,SoftReference<List<OverlayItem>>>());

	private BroadcastReceiver netWorkStateReceiver; 
	public MyLocationListenner mListener ;
	private MyOnclickListener mOnClickListener;
		
	private Application app;
	private Toast mToast = null;
	private static final String TAG = "LocationActivity";
	private BMapManager bmapManager ;

    @Override 
    public void onCreate(Bundle savedInstanceState) { 
    	super.onCreate(savedInstanceState);	 
    	Log.e("---------------1", System.currentTimeMillis()+"");//4
    	app = this.getApplication(); 
    	try {
			Field mapManagerField =app.getClass().getField("mBMapManager");
			if (mapManagerField.get(app) == null) {
				bmapManager=new BMapManager(this);
			    mapManagerField.set(app,bmapManager) ;
			   	bmapManager.init(MapApplication.strKey,new MKGeneralListener(){
					public void onGetNetworkState(int arg0) {
					}
					public void onGetPermissionState(int arg0) {
					}});
			}else{
				bmapManager=(BMapManager)mapManagerField.get(app);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "地图初始化失败", Toast.LENGTH_SHORT).show();
		} 
        Log.e("---------------2", System.currentTimeMillis()+"");//4
    	setContentView(R.layout.bmap); //2333
    	Log.e("---------------3", System.currentTimeMillis()+"");//4
    	setupView();//4
        initMapView();//9
        Log.e("---------------4", System.currentTimeMillis()+"");//4
        mLocClient = new LocationClient( this );//3
    	myLocationOverlay = new MyLocationOverlay(mMapView);
    	mListener = new MyLocationListenner();
    	mLocClient.registerLocationListener(mListener);		
    	Log.e("---------------5", System.currentTimeMillis()+"");
		//检测网络连接 
		if(!CommonUtil.checkhttp(this)){
    		isNetworkAvailable=false;
    	}else{ 	   
    		receiveDataFromWeb();
    	}
//		if(mLocData.latitude==0.0){
//    		showToast("正在获取位置信息");
//    	}
		Log.e("---------------6", System.currentTimeMillis()+"");//4
		//注册网络监听                             
    	IntentFilter filter = new IntentFilter();    
    	filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
    	netWorkStateReceiver = new BroadcastReceiver(){

	  		@Override 
	  		public void onReceive(Context context, Intent intent) {
	  			Log.e(TAG, "网络状态改变");   
	  			boolean success = false;
	  			//获得网络连接服务   	  			
	  			ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);   	  			
	  			State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState(); // 获取网络连接状态  
	  			if (State.CONNECTED == state) { // 判断是否正在使用WIFI网络   	  				
	  				success = true;  
	  				netWorkState="WIFI";
	  				if(isNetworkAvailable==false){
	  					receiveDataFromWeb();
	  				}
	  				isNetworkAvailable=true;
	  			}      
	  			state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState(); // 获取网络连接状态   
	  			if (State.CONNECTED == state) { // 判断是否正在使用GPRS网络   
	  				success = true; 
	  				netWorkState="GPRS";
	  				if(isNetworkAvailable==false){
	  					receiveDataFromWeb();
	  				}
	  				isNetworkAvailable=true;
	  			}      
	  			if (!success) {   
	  				showToast("您的网络连接已中断");  			
	  			}    
	  		}  		  
    	};
    	registerReceiver(netWorkStateReceiver, filter); 
    	Log.e("---------------7", System.currentTimeMillis()+"");  
    	
    	mOnClickListener = new MyOnclickListener();
    	//下拉选项
        btn_manager.setOnClickListener(mOnClickListener);	 	
		//点击定位
    	btn_loc.setOnClickListener(mOnClickListener);
			       
	    //如果是社区中心直接返回已经对应的点
		//如果是随机的点 返回计算后的4000米的点

        spr_list_name.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override			
			public void onItemSelected(AdapterView<?> view, View arg1,int position, long arg3) {
				if(cList == null){
					return;
				}
				if(position == 0){
					btn_getlist.setVisibility(View.GONE);
					return;
				} 
				else if(position == 1){
					if(flag=="fail"){
						showToast("请点击重新获取附近商户");
						return;
					}
//					mOv.pop.hidePop();
					
					btn_getlist.setVisibility(View.VISIBLE);					
					//从服务器中获取离我最近的社区的商户
					pb_load.setVisibility(View.VISIBLE);
					loadMyLocMark();					
					//添加图层/地图中心重新显示到我的位置
					nearbyArea();
				}
				
				//有些社区暂无数据 所以...
				//TODO
//				else{
////					mOv.pop.hidePop();
//					btn_getlist.setVisibility(View.VISIBLE);
//					isFollow = false;
//					setLocationOption();
//					btn_loc.setText("移动地图中..");
//					//社区ID
//					int commuId = position-1;	
//					//社区中心点
//					GeoPoint commuPoint = cList.get(position).getCommunityCenterPoint();
//					cachePoint = commuPoint;
//					//从缓存中获取社区
//					if(markMap.get(commuId)!=null){	
//						//获取该社区商户
//						getMarkFromStore(getCacheOverlayItem(commuId));
//						//画上蓝色范围圈图层
//						commArea(commuPoint);
//					}else{
//						loadCommMark(commuId);
//						commArea(commuPoint);
//					}
//				} 	
				else if(position == 2 || position ==4){
//					mOv.pop.hidePop();
					btn_getlist.setVisibility(View.VISIBLE);
					isFollow = false;
					setLocationOption();
					btn_loc.setText("移动地图中..");
					//社区ID
					int commuId = position-1;	
					//社区中心点
					GeoPoint commuPoint = cList.get(position).getCommunityCenterPoint();
					cachePoint = commuPoint;
					//从缓存中获取社区
					if(markMap.get(commuId)!=null){	
						//获取该社区商户
						getMarkFromStore(getCacheOverlayItem(commuId));
						//画上蓝色范围圈图层
						commArea(commuPoint);
					}else{
						pb_load.setVisibility(View.VISIBLE);
						loadCommMark(commuId);
						commArea(commuPoint);
					}
				}else{
					showToast("该社区暂无数据");
				}	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
						
			}
		});
              
	    //distance计算得在地图中
	    btn_getlist.setOnClickListener(mOnClickListener);  
	        
	    btn_reposition.setOnClickListener(mOnClickListener);	     
    }   
      
   // 初始化搜索模块，注册事件监听
   public void initSearch(){  	 	
  		 mSearch = new MKSearch();
         mSearch.init(bmapManager, new MKSearchListener(){

		@Override
		public void onGetAddrResult(MKAddrInfo res, int error) {
			 //非常不准位置都被偏移了
		}

		@Override
		public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
			
		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,int arg1) {
			
		}

		@Override
		public void onGetPoiDetailSearchResult(int arg0, int arg1) {

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			
		}

		@Override
		public void onGetSuggestionResult(MKSuggestionResult res,int arg1) {
	              
		}			

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult arg0,int arg1) {
			
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult res,int error) {
			if (error != 0 || res == null) {
				showToast("请点击标识选择商户");		
				return;
			}
			routeOverlay = new RouteOverlay(LocationActivity.this, mMapView);
			routeOverlay.setData(res.getPlan(0).getRoute(0));	
		    mMapView.getOverlays().add(routeOverlay);
		    mMapView.refresh();
		    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
		    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
		    mMapView.getController().animateTo(res.getStart().pt);			    
		}
      });
    }
    
    //从缓存中获取社区对应的商户(社区下拉列表点击那一块功能)
    public void getMarkFromStore(List<OverlayItem> geoList){   
    	mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏
    	//实现画两个图层 mov和routeOverlay/而cirle在画图时被清除了，得加上commArea()/location位置信息保留
    	mOv = new MyOverlayItem(null,LocationActivity.this, mMapView,geoList);
    	//移动地图时 先把原先点移除 后把moveAreaMak加载
    	mOv.removeAll();
		mOv.addItem(geoList);
//		mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏 
		mMapView.getOverlays().add(mOv);					 		
		mMapView.refresh();

    }
    
    //从网络获取数据包括（定位信息，附近商户/社区列表）
    public void receiveDataFromWeb(){
    	//自动定位
		btn_loc.setText("实时定位中.."); 
		isFollow=true;
		setLocationOption();
		mLocClient.start();					
		mLocClient.requestLocation();		
	    mMapView.getOverlays().add(myLocationOverlay); 
		myLocationOverlay.enableCompass();		
		mMapView.refresh();
		
		//当前定位点向服务器发请求获得周边点，有个位置监听获取相应坐标时间延迟的问题	
		loadMyLocMark();	
		//社区列表
		getCommuListTask();
    }
    
    private void setupView(){
    	tv_addr = (TextView) findViewById(R.id.bmapTView);
    	btn_loc = (Button) findViewById(R.id.bmap_btn_loc);
    	mMapView = (MyMapView)findViewById(R.id.bmapsView);
    	rg = (RadioGroup) findViewById(R.id.rg_views);
    	spr_list_name = (Spinner) findViewById(R.id.spr_place_name);
        btn_getlist = (Button) findViewById(R.id.bmap_btn_getlist);
    	pb_load = (ProgressBar) findViewById(R.id.loading_progressbar);   
    	btn_reposition = (Button) findViewById(R.id.bmap_btn_reposition);
    	btn_manager = (LinearLayout) findViewById(R.id.bmap_btn_manager);
    	rl_commu_select = (LinearLayout) findViewById(R.id.bmap_commu_select_rl);
    }
    
    private void initMapView(){     
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener(){	
        	
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.rb_normal) {
					mMapView.setSatellite(false);
					mMapView.setTraffic(false);
				} else if (checkedId == R.id.rb_satellite) {
					mMapView.setSatellite(true);
					mMapView.setTraffic(false);
				} else if (checkedId == R.id.rb_terrain) {
					mMapView.setTraffic(true);
					mMapView.setSatellite(false);
				}				
			}
		});
        
        mMapView.setLongClickable(true);
//    	mMapView.setBuiltInZoomControls(true);//启用内部的放大缩小按钮
        mMapController = mMapView.getController();
        mMapController.setZoom(16);
        mMapController.enableClick(true);
        //默认地图初始中心
        mMapController.setCenter(new GeoPoint(26081519,119301933));// 乌山路
        
        //地图移动事件监听 
        mMapListener = new MKMapViewListener() {
        	
			@Override
			public void onMapMoveFinish() {			
				long time = System.currentTimeMillis() - lastTime;	
				isFollow = false;
				setLocationOption();
				btn_loc.setText("移动地图中..");
				//注意流量问题
//				mLocData=((LocationApplication)getApplication()).mLocData;	
				if(mLocData.latitude != 0.0){
					Log.e("原始点", cachePoint+"");
					GeoPoint scrnCenterPoint = mMapView.getMapCenter();
					Log.e("中心点",scrnCenterPoint+"");
					
					double distance = DistanceUtil.getDistance(cachePoint, scrnCenterPoint);
					Log.e("移动了", distance+"");
					//所有的社区已经加载到spinner中/到List<Community> cList将所有社区坐标点与当前移动地图中心点对比/对比求距离用distanceutil工具
					if(distance > 700 && time > 1500){		
						lastTime = System.currentTimeMillis();
						cachePoint = scrnCenterPoint;
						pb_load.setVisibility(View.VISIBLE);
						HashMap<Long,GeoPoint> commuGeoMap = new HashMap<Long,GeoPoint>();
						
						//clist从第二条2开始保存社区信息的/CommunityJSONParser 第37行
						//注意map的key是long类型                       
//						for(int i=2; i<cList.size(); i++){
//							commuGeoMap.put((long)cList.get(i).getId(), cList.get(i).getCommunityCenterPoint());
//						}
//						List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
//						
//						for(int i=1; i<cList.size()-1; i++){
//							HashMap<String,Object> map=new HashMap<String,Object>();			
//							map.put("commuId", i);
//							map.put("distance", DistanceUtil.getDistance(scrnCenterPoint,commuGeoMap.get((long)i)));
//							list.add(map);
//						}
											
						//TODO 
						//做下修改有些社区暂无数据 所以只是对比3个社区够了
						
						for(int i=2; i<5; i+=2){
							System.out.println(commuGeoMap+",clist="+cList+"   @LocationActivity");
							if(cList!=null&&cList.size()>i)
							commuGeoMap.put((long)cList.get(i).getId(), cList.get(i).getCommunityCenterPoint());
						}
						List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
						
						for(int i=1; i<5-1; i+=2){
							HashMap<String,Object> map=new HashMap<String,Object>();			
							map.put("commuId", i);
							GeoPoint gp=commuGeoMap.get((long)i);
							if(gp==null)break;
							map.put("distance", DistanceUtil.getDistance(scrnCenterPoint,gp));
							list.add(map);
						}
											
						
						
						Comparator<HashMap<String,Object>> comparator = new Comparator<HashMap<String,Object>>() {

							@Override
							public int compare(HashMap<String, Object> lhs,
									HashMap<String, Object> rhs) {
								int minus=(int)Double.parseDouble(String.valueOf(lhs.get("distance")))-(int)Double.parseDouble(String.valueOf(rhs.get("distance"))); 
								return minus;
							}							
						};
						
						Collections.sort(list, comparator);
						//计算得移动后的社区id(Long类型)
						if(list.isEmpty()) return ;
						long commuId = Long.valueOf((Integer)list.get(0).get("commuId"));						
						List<OverlayItem> cacheGeoList = getCacheOverlayItem(commuId); 
						//TODO
						//缓存中存在则加载/向服务器获取
						if(cacheGeoList != null){
							
							getMarkFromStore(cacheGeoList);	
							pb_load.setVisibility(View.GONE);
							return;
						}else{
							loadCommMark(commuId);
						}
					}
				}		
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// 顺便显示下点击地图某些POI位置toast显示Title
				String title = ""; 
				if (mapPoiInfo != null){ 
					title = mapPoiInfo.strText;
					showToast(title);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {	
			}

			@Override
			public void onMapAnimationFinish() {			
			}
		};
		mMapView.regMapViewListener(bmapManager, mMapListener);	 	
    	//几何图形绘制图层：GraphicsOverlay
		graphicsOverlay =new GraphicsOverlay(mMapView);
    }
 
    //设置相关参数
  	private void setLocationOption(){
  		LocationClientOption option = new LocationClientOption();
//  		option.setOpenGps(true);				//打开gps 没效果
  		option.setCoorType("bd09ll");		//设置坐标类型
  		option.setServiceName("com.baidu.location.service_v2.9");	
  		option.setAddrType("all");
  		if(isFollow==false){
  			option.setScanSpan(640);
  		}else{
  			option.setScanSpan(10000);	//设置定位模式，小于1秒则一次定位;大于等于1秒则定时定位
  		}	
//  		option.setPriority(LocationClientOption.NetWorkFirst);
  		option.setPriority(LocationClientOption.GpsFirst);        //是gps优先
  		option.setPoiNumber(10);
  		option.setPoiDistance(160); 
  		option.disableCache(false);		
  		mLocClient.setLocOption(option);
  		
  	}
	
	//我的位置范围圈 即蓝色背景
	 public void nearbyArea(){ 	
		  graphicsOverlay.removeAll();		
		  if(mLocData.latitude==0.0){
			  showToast("请先进行定位");
		  }else{ 		 
	 		 GeoPoint mLocPoint=new GeoPoint((int)(mLocData.latitude*1E6),(int)(mLocData.longitude*1E6));
	 		//离我最近选项卡里的是距离我的坐标460米的商户
	 		 drawCircle(mLocPoint, 0);
		  }		    
		  //标记点是定位的时候就loadmark了 所以只需加上图层
	    }
	 
	 //社区位置范围圈 即蓝色背景
	 public void commArea(GeoPoint center){		 
		 graphicsOverlay.removeAll();
		//社区选项卡里的是社区中心700米的商户
		 drawCircle(center, 700);		 
	 }
	 
	 public void drawCircle(GeoPoint center,int range){
		 Symbol circleSymbol = new Symbol();
	 	 Symbol.Color circleColor = circleSymbol.new Color();
	 	 circleColor.red = 0;
	 	 circleColor.green = 191;
	 	 circleColor.blue = 255;
	 	 circleColor.alpha = 46;
	  	 circleSymbol.setSurface(circleColor,1,3);
	  	 Geometry circleGeometry = new Geometry();
	  	 circleGeometry.setCircle(center, range); 
	  	 Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);	  		
	  	 graphicsOverlay.setData(circleGraphic);
	  	 mMapView.getOverlays().add(graphicsOverlay);
	  	 mMapView.getController().setZoom(16);
	  	 mMapView.getController().setCenter(center);
	  	 mMapView.refresh(); 
	 }
	 
	 /**
	  * 通过SoftReference的get()方法得到List<OverlayItem>对象
	  */
	 private List<OverlayItem> getCacheOverlayItem(long commuId){
		 SoftReference<List<OverlayItem>> softOveItem = markMap.get(commuId);
		 if(softOveItem == null){
			 return null;
		 }
		 List<OverlayItem> oveItem = softOveItem.get();
		 return oveItem;
	 }
	 	  
	 //向服务器发请求 获取我的位置以及周围商户
	 public void loadMyLocMark() {	
		 mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏
		 GetMyLocDataTask getlocDataTask = new GetMyLocDataTask();
		 getlocDataTask.execute();				
	 }
	 	  
	 /**
	  * 获取我的位置及周围商户
	  *
	  */

	 private class GetMyLocDataTask extends AsyncTask<Void, Void,String>{
		 @Override
			protected String doInBackground(Void... params) {
				try{	
					mark_a = LocationActivity.this.getResources().getDrawable(R.drawable.map_mark_a);
					mark_b = LocationActivity.this.getResources().getDrawable(R.drawable.map_mark_b);
					mark_c = LocationActivity.this.getResources().getDrawable(R.drawable.map_mark_c);
					Thread.sleep(2500);
					//初始时我的位置坐标点是空的，获取需要时间
					Log.d("第一次取到坐标点：", mLocData.latitude+"："+mLocData.latitude);
					if(mLocData.latitude == 0.0){
						Thread.sleep(5000); 
						Log.d("第二次取到坐标点：", mLocData.latitude+"："+mLocData.latitude);
					}
					if(mLocData.latitude == 0.0){
						return flag="fail"; 
					}
					
					GeoPoint geoPoint=new GeoPoint((int)mLocData.latitude,(int)mLocData.longitude);
					loadRandomMark(geoPoint);
				}catch(Exception e){
	                
				}
				return flag="success";		
			}
			
			@Override
			protected void onPostExecute(String result) {	
				if("fail" == result){
					pb_load.setVisibility(View.GONE);
					showToast("网络延迟，加载失败");
					btn_reposition.setVisibility(View.VISIBLE);
					//TODO
				}else{
					btn_reposition.setVisibility(View.GONE);
					pb_load.setVisibility(View.GONE);
				}				
			}	
	 }
	 
	 //获得我所在坐标点周围几千米的标识点//改为向服务器获得获得离我最近社区，返回该社区内的商户点
	 public void loadRandomMark(GeoPoint center){
		 mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏
		 loadMarkersTask(center);
	 }	 
 
	 /**
	  * 获取根据我的坐标对应我最近的社区的商户...真别扭
	  *
	  */
	 private void loadMarkersTask(GeoPoint center){
		 String url = "http://192.168.137.1:8080/fc/getNearbyMercByLoc?latitudeE6="+center.getLatitudeE6()+"&longitudeE6="+center.getLongitudeE6();
		 AsyncHttpClient client = new AsyncHttpClient();
         client.get(url, new AsyncHttpResponseHandler(){

             @Override
             public void onStart() {
             }

             @Override
             public void onSuccess(String result) {
            	 List<Merchant> mercList = null;	
            	 //解析				           
 	             CommunityMarkJSONParser placeJsonParser = new CommunityMarkJSONParser();	 
 	             try{
 	            	 JSONObject jObject = new JSONObject(result);
 	                 // Getting the parsed data as a List construct
 	                 mercList = placeJsonParser.parse(jObject);
 	             }catch(Exception e){
 	                 Log.e("Exception","解析我附近的商户异常");
 	             } 	
 	             //将数据显示到地图上
 				 //第一次定位向服务器发请求 
 				 //考虑加载时间问题//后面的获取我附近商户用下边的task
 				 List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
 				
 	        	 for(int i = 0; i < mercList.size(); i++){
 	        		 OverlayItem item = new OverlayItem(mercList.get(i).getGeoPoint(), 
 	        				mercList.get(i).getMerName(), mercList.get(i).getDetail());//百度地图真搞笑 不让显示title和sin 居然还有这些值
 	        		 if(mercList.get(i).getTypeId() == 1){
 	        			 item.setMarker(mark_a);
 	        		 }
 	        		 if(mercList.get(i).getTypeId() == 2){
 	        			 item.setMarker(mark_b);
 	        		 }
 	        		 if(mercList.get(i).getTypeId() == 3){
 	        			 item.setMarker(mark_c);
 	        		 }
 	        		 mGeoList.add(item);     		
 	        	 }
 	        	 //软引用
 	        	 SoftReference<List<OverlayItem>> softMGeoList = new SoftReference<List<OverlayItem>>(mGeoList);
 	        	 //由于修改后是离我最近的社区的商户，所以map的key用直接用社区id
 	        	 markMap.put(mercList.get(0).getCommuId(), softMGeoList); 
 	        	 mOv = new MyOverlayItem(null,LocationActivity.this, mMapView,mGeoList);
 	        	 mOv.removeAll();	        	
 				 mOv.addItem(mGeoList);		
// 				 mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏
 				 mMapView.getOverlays().add(mOv);
 				 mMapView.getOverlays().add(myLocationOverlay);
 				 
 	 			 mMapView.refresh();
             }

             @Override
             public void onFailure(Throwable arg0) {
            	 showToast("加载数据错误啦！");
             }

             @Override
             public void onFinish() {
            	 pb_load.setVisibility(View.GONE);
             }
         });
	 }

	  //获得定位的坐标点周围几千米内的标识点//改成用该社区id获取商户
	  private void loadCommMark(long commuId){
		  loadCommMarkTask(commuId);
	  }
	  
	  /**
	   * 获取根据社区ID对应的商户
	   *
	   */	
	  private void loadCommMarkTask(long commuId){
		   final String url = "http://192.168.137.1:8080/fc/getMercByCommuId"+commuId;		   
		   //从缓存中获取
		   String cacheConfigString = ConfigCache.getUrlCache(url,netWorkState);
		   if(cacheConfigString != null){
			   loadCommMarkList(cacheConfigString);
			   return;
		   }
		   
		   AsyncHttpClient client = new AsyncHttpClient();
	       client.get(url, new AsyncHttpResponseHandler(){

	    	   @Override
	    	   public void onStart() {
	    	   }

	    	   @Override
	    	   public void onSuccess(String result) {
	    		   loadCommMarkList(result);
	    		   ConfigCache.setUrlCache(url, result);
	    	   }

	    	   @Override
	    	   public void onFailure(Throwable arg0) {
	    		   showToast("加载数据错误啦！");
	    	   }

	    	   @Override
	    	   public void onFinish() {
	    		   pb_load.setVisibility(View.GONE);
	    	   }
          });
	  }
	  private void loadCommMarkList(String result){
		   List<Merchant> mercList = null;	
     	   //解析				           
           CommunityMarkJSONParser placeJsonParser = new CommunityMarkJSONParser();	 
           try{
          	 JSONObject jObject = new JSONObject(result);
               // Getting the parsed data as a List construct
               mercList = placeJsonParser.parse(jObject);
           }catch(Exception e){
               Log.e("Exception","解析我附近的商户异常");
           } 	
           //将数据显示到地图上
			 //第一次定位向服务器发请求 
			 //考虑加载时间问题//后面的获取我附近商户用下边的task
			 List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
			
      	  for(int i = 0; i < mercList.size(); i++){
      		  OverlayItem item = new OverlayItem(mercList.get(i).getGeoPoint(), 
      				mercList.get(i).getMerName(), mercList.get(i).getDetail());//百度地图真搞笑 不让显示title和sin 居然还有这些值
      		  if(mercList.get(i).getTypeId() == 1){
      			  item.setMarker(mark_a);
      		  }
      		  if(mercList.get(i).getTypeId() == 2){
      			  item.setMarker(mark_b);
      		  }
      		  if(mercList.get(i).getTypeId() == 3){
      			  item.setMarker(mark_c);
      		  }
      		  mGeoList.add(item);     		
      	  }
      	  //软引用
      	  SoftReference<List<OverlayItem>> softMGeoList = new SoftReference<List<OverlayItem>>(mGeoList);
      	  //由于修改后是离我最近的社区的商户，所以map的key用直接用社区id
      	  markMap.put(mercList.get(0).getCommuId(), softMGeoList); 
      	  mOv = new MyOverlayItem(null,LocationActivity.this, mMapView,mGeoList);
      	  mOv.removeAll();	        	
		  mOv.addItem(mGeoList);		
//				 mMapView.getOverlays().clear();//得把路径、等原先的mark点等清屏
		  mMapView.getOverlays().add(mOv);
		  mMapView.getOverlays().add(myLocationOverlay);		  
		  mMapView.refresh();
	  }


	  /**
	   * 获取社区列表
	   *
	   */
	  private void getCommuListTask(){
		   final String url = "http://192.168.137.1:8080/fc/getCommuList";
		   //从缓存中获取
		   String cacheConfigString = ConfigCache.getUrlCache(url,netWorkState);
		   if(cacheConfigString != null){
			   loadCommuList(cacheConfigString);
			   return;
		   }
		   
		   AsyncHttpClient client = new AsyncHttpClient();
	       client.get(url, new AsyncHttpResponseHandler(){

	    	   @Override
	    	   public void onStart() {
	    	   }

	    	   @Override
	    	   public void onSuccess(String result) {
	    		   loadCommuList(result);
	    		   ConfigCache.setUrlCache(url, result);
	    		   ConfigCache.setUrlCache(url, result);
	    	   }

	    	   @Override
	    	   public void onFailure(Throwable arg0) {
	    		   showToast("加载数据错误啦！");
	    	   }

	    	   @Override
	    	   public void onFinish() {
            	 
	    	   }
          });

	  }
	  private void loadCommuList(String result){
		   //解析
		   CommunityJSONParser placeJsonParser = new CommunityJSONParser();	 
           try{	            	  
//          	if(jsonData[0] != null && jsonData[0].startsWith("\ufeff"))  
//          	{  
//          		jsonData[0] =  jsonData[0].substring(1);  
//          	}  	
          	//有BOM 需如下
          	JSONObject jObject = new JSONObject(result.substring(result.indexOf("{"), result.lastIndexOf("}")+1));
                cList = placeJsonParser.parse(jObject);	 
            }catch(Exception e){
            	Log.e("Exception", "解析社区列表异常");
            }
		    //将数据显示到地图上
	        String[] mPlaceName = new String[cList.size()];
	  	    for(int i = 0; i < cList.size(); i++){
	  		    mPlaceName[i] = cList.get(i).getCommunityName();
	  	   }	    		
	  	   //字体样式
	  	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(LocationActivity.this,R.layout.spinner_myitem,mPlaceName){
	  			//间隔样式
	  			@Override
	  			public View getDropDownView(int position, View convertView,
	  					ViewGroup parent) {
	  				View view = super.getDropDownView(position, convertView, parent);
	  		        if (position % 2 == 0) {
	  		            view.setBackgroundColor(0xFFFFFFFF);
	  		        } else {
	  		            view.setBackgroundColor(0xFFEAEAEA);
	  		        }
	  		        return view;	    				
	  			}
	  	   };
	  	   //下拉框样式
	  	   adapter.setDropDownViewResource(R.layout.simple_dropdown_myitem);
	  	   spr_list_name.setAdapter(adapter);
		  
	  }
   
	@Override
	public void onBackPressed() {
		 Log.e(TAG, "onBackPress()....");
		 if (mToast != null) {  
             mToast.cancel();  
         }
		super.onBackPressed();
	}
	public void showToast(String text) {  
        if(mToast == null) {  
            mToast = Toast.makeText(LocationActivity.this, text, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 186);
        } else {  
        	mToast.setGravity(Gravity.CENTER, 0, 186);
            mToast.setText(text);            
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();         
    }   	
	 
    //退出地图相应操作
    @Override
    protected void onPause() {
    	Log.e(TAG, "onPause()......");
        mMapView.onPause();
        super.onPause();
    }
    //恢复地图相应操作
    @Override
    protected void onResume() {
    	Log.e(TAG, "onResume()......");
        mMapView.onResume();  
        super.onResume();
    }
    //退出地图相应操作
    @Override
    protected void onDestroy() {
    	Log.e(TAG, "onDestory()......");
    	mLocData = null;
    	if(mOv !=null ){
    		mOv.removeAll();
    	}
    	if(mSearch != null){
        	mSearch = null;
        }
        unregisterReceiver(netWorkStateReceiver);
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	Log.e("111111111111111111111", "1111111111111111114");
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);   	
    } 
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	Log.e(TAG, "onRestoreIns()..........");
    	savedInstanceState.getString("ddd");
    	Log.e("4444444444",savedInstanceState.getString("ddd"));
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
       
    public void makePopupWindow(Context cx) {    	
    	
    	View view = LayoutInflater.from(LocationActivity.this).inflate(R.layout.map_popwindow, null);
    	final PopupWindow mPopupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
		mPopupWindow.setAnimationStyle(R.style.MapAnimationFade);		
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());//点击窗口外消失 
		mPopupWindow.setOutsideTouchable(true);//默认的
		mPopupWindow.setFocusable(true); //设置PopupWindow可获得焦点 默认不获得焦点
		mPopupWindow.setTouchable(true); //设置PopupWindow可触摸 默认的
		mPopupWindow.showAsDropDown((LinearLayout) findViewById(R.id.rl_1), 72,0); 
		
		Button btn_commu1= (Button) view.findViewById(R.id.bmap_btn_commu);
    	btn_route = (Button) view.findViewById(R.id.bmap_btn_route);
    	Button btn_views = (Button) view.findViewById(R.id.bmap_btn_views);
    	Button btn_map=(Button) view.findViewById(R.id.bmap_btn_map);
    	btn_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				rg.setVisibility(rg.getVisibility()==View.VISIBLE?View.GONE:View.GONE);
				rl_commu_select.setVisibility(rl_commu_select.getVisibility()==View.VISIBLE?View.GONE:View.GONE);
				
				btn_route.setText("获取线路");
				mMapView.getOverlays().remove(routeOverlay);					
				mMapView.getController().setZoom(16);
				mMapView.refresh();		    
			    routeFlag=false;
			}
		});
    	btn_commu1.setOnClickListener(mOnClickListener);
    	btn_route.setOnClickListener(mOnClickListener);
    	btn_views.setOnClickListener(mOnClickListener);		
    }
    
    private class MyOnclickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		
    		if (v.getId() == R.id.bmap_btn_manager) {
				makePopupWindow(LocationActivity.this);
			} else if (v.getId() == R.id.bmap_btn_loc) {
				if(isNetworkAvailable==false){
					showToast("当前网络不可用！");
					return;
				}
				btn_loc.setText("实时定位中..");
				if(mLocClient != null && mLocClient.isStarted()){
					isFollow=true;
					mLocClient.requestLocation();	
				}else{
					isFollow=true;
					setLocationOption();
					mLocClient.start();					
					mLocClient.requestLocation();	
				}
				myLocationOverlay.setData(mLocData);
//			        mMapView.getOverlays().clear();//不能清空图层 不然标记不在了		
				mMapView.getOverlays().add(myLocationOverlay);
				mMapView.refresh();
			} else if (v.getId() == R.id.bmap_btn_getlist) {
				if(cList==null){
					return;
				}
				//				List<OverlayItem> currentGeoList=new ArrayList<OverlayItem>();
//				int commuId=spr_list_name.getSelectedItemPosition();
//				if(commuId==0){
//					return;
//				}else if(commuId==1){
//					currentGeoList=markMap.get(0);
//				}else{
//					currentGeoList=markMap.get(commuId);
//				}
//				
//				List<Business> mList=new ArrayList<Business>();
//				GeoPoint mLoc = new GeoPoint((int)(mLocData.latitude* 1E6),(int)(mLocData.longitude* 1E6));
//				
//				for(int i=0;i<currentGeoList.size();i++){
//					Log.e("cdd",currentGeoList.size() +"");
//					Business marker=new Business();
//					BigDecimal  b= new BigDecimal(DistanceUtil.getDistance(mLoc, currentGeoList.get(i).getPoint()));   
//					double  mDistance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//					marker.setBusinessDistance(mDistance);
//					marker.setBusinessName(currentGeoList.get(i).getTitle());
//					mList.add(marker);
//				}				
//				//TODO
////				mOv.pop.hidePop();
				Intent intent = new Intent(LocationActivity.this,IntentListActivity.class);
//				SharedPreferences sp = LocationActivity.this.getSharedPreferences("locMsg", MODE_PRIVATE);
//				Editor editor=sp.edit();
//				editor.putString("latitude", ""+mLocData.latitude);
//				editor.putString("longitude", ""+mLocData.longitude);
//				editor.commit();
				//停止定位
//				isFollow=false;
//				setLocationOption();
//				btn_loc.setText("移动地图中..");
				//				Bundle bundle=new Bundle();
//				bundle.putSerializable(TAG, (Serializable)mList);//强转
//				intent.putExtras(bundle);
//				System.gc();
//				startActivity(intent);		
				showToast("未连接服务器，暂无数据");
			} else if (v.getId() == R.id.bmap_btn_reposition) {
				if(isNetworkAvailable==false){
					return;
				}
				loadMyLocMark();
				flag="success";
			} else if (v.getId() == R.id.bmap_btn_commu) {
				rg.setVisibility(rg.getVisibility()==View.VISIBLE?View.GONE:View.GONE);
				rl_commu_select.setVisibility(rl_commu_select.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
			} else if (v.getId() == R.id.bmap_btn_route) {
				initSearch();
				if(routeFlag==false){				
					btn_route.setText("查看线路");
//					mOv=new MyOverlayItem(mark,LocationActivity.this, mMapView);
					mSelectedPoint=MyOverlayItem.selectedPoint;					
				    GeoPoint mLoc = new GeoPoint((int)(mLocData.latitude* 1E6),(int)(mLocData.longitude* 1E6));

					MKPlanNode stNode = new MKPlanNode();
					stNode.pt=mLoc;
					MKPlanNode endNode = new MKPlanNode();
					endNode.pt=mSelectedPoint;
					mSearch.walkingSearch("福州", stNode, "福州", endNode);
					routeFlag=true;
				}else{
					btn_route.setText("获取线路");
					mMapView.getOverlays().remove(routeOverlay);					
					mMapView.getController().setZoom(16);
//				  		mMapView.getController().animateTo(new GeoPoint((int)(mLocData.latitude * 1E6),(int)(mLocData.longitude * 1E6)));
					mMapView.refresh();		    
				    routeFlag=false;
				}
			} else if (v.getId() == R.id.bmap_btn_views) {
				rl_commu_select.setVisibility(rl_commu_select.getVisibility()==View.VISIBLE?View.GONE:View.GONE);
				rg.setVisibility(rg.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
			} 
    	};
    }    
    
    //位置监听
    public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			tv_addr.setText(location.getAddrStr());
			mLocData.latitude=location.getLatitude();
			mLocData.longitude=location.getLongitude();
			mLocData.direction=location.getDerect();
			mMapView.refresh();//实时监听需在这里刷新	
			cachePoint = new GeoPoint((int)(location.getLatitude()* 1E6), (int)(location.getLongitude()* 1E6));
			//还得进行判断地址是否为空 是否显示？	
			Log.e("whatisjava", "msg");
			if(location.getAddrStr()!=null){
				Log.e("whatisjava", "msg2");
				tv_addr.setVisibility(View.VISIBLE); 
//				ToastUtils.showToast(LocationApplication.getInstance().getApplicationContext(), Gravity.BOTTOM,location.getAddrStr()+"\n"+"纬度"+location.getLatitude()+"\n"+"经度"+location.getLongitude(), Toast.LENGTH_SHORT);	
				// 获取定位精度半径，单位是米  				
				if (location.hasRadius()) {  
					mLocData.accuracy = location.getRadius(); 					
//					ToastUtils.showToast(LocationApplication.getInstance().getApplicationContext(), "范围精度"+location.getRadius(), Toast.LENGTH_SHORT);
				} 
			}			
			if (isFollow) {  
				myLocationOverlay.setData(mLocData);
	            mMapView.refresh();
				mMapController.animateTo(new GeoPoint((int)(mLocData.latitude* 1e6), (int)(mLocData.longitude *  1e6)));
            }  
		}	
		//poi信息采集 可选,耗流量
		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ; 
			} 
		}
	}
}
