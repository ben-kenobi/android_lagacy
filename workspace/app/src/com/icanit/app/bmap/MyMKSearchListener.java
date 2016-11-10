package com.icanit.app.bmap;

import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.icanit.app.R;

/**public class MyMKSearchListener implements MKSearchListener,OnClickListener {
	private MapView map;
	private MapController controller;
	private BMapActivity activity;
	private MKSearch search;
	private PoiOverlay overlay;
	public CommunityBoundOverlay cbo;
	public View popView;
	public TextView merName,addr,phoneNo;
	private String uid;
	public MyMKSearchListener(MapView map,BMapActivity activity,MKSearch search,CommunityBoundOverlay cbo){
		this.map=map;controller=map.getController();
		this.activity=activity;this.search=search;
//		this.cbo=cbo;popView=LayoutInflater.from(activity).inflate(R.layout.popview_bmap, map,false);
		map.addView(popView);popView.setVisibility(View.GONE);
		merName=(TextView)popView.findViewById(R.id.textView1);
		addr=(TextView)popView.findViewById(R.id.textView2);
		phoneNo=(TextView)popView.findViewById(R.id.textView3);
		merName.setOnClickListener(this);
	}
	public void onGetAddrResult(MKAddrInfo info, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetAddrResult,\n" +
				"errorCode:"+error+",info:"+info);
		if(info==null||error!=0)return;
		controller.setCenter(info.geoPt);
		activity.centerPoint=info.geoPt;
		controller.setZoom(16);
		map.getOverlays().add(cbo);
		cbo.setDistance(1000);
		cbo.setGp(info.geoPt);
		map.invalidate();
	}

	public void onGetBusDetailResult(MKBusLineResult result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetBusDetailResult,\n" +
				"errorCode:"+error+",result:"+result);
	}

	public void onGetDrivingRouteResult(MKDrivingRouteResult result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetDrivingRouteResult,\n" +
				"errorCode:"+error+",result:"+result);
	}
	
	public void onGetPoiDetailSearchResult(int type, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetPoiDetailSearchResult,\n" +
				"errorCode:"+error+",type:"+type);
		
	}

	public void onGetPoiResult(MKPoiResult result, int type, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetPoiResult,\n" +
				"errorCode:"+error+",result:"+result+",type:"+type);
		Log.w("mapInfo","@itemizedMapActivity  onGetPoiResult,"+type+","+error+","+result);
		if(result==null||error!=0)return;
		final ArrayList<MKPoiInfo> pois=result.getAllPoi();
		map.getOverlays().remove(overlay);
//		 overlay = new PoiOverlay(activity, map){
//			protected boolean onTap(int arg0) {
//				MKPoiInfo info = pois.get(arg0);
//				uid=info.uid;
//				map.updateViewLayout(popView, new MapView.LayoutParams(LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT,info.pt,MapView.LayoutParams.BOTTOM_CENTER));
//				popView.setVisibility(View.VISIBLE);
//				merName.setText(info.name);
//				addr.setText("µÿ÷∑£∫"+info.address);
//				addr.setText("µÁª∞£∫"+info.phoneNum);
//				 return true;
//			}
//			public boolean onTap(GeoPoint arg0, MapView arg1) {
//				Log.d("mapInfo","@MYMKSearchListener  onGeoPoiResul onTap");
//				popView.setVisibility(View.GONE);
//				return super.onTap(arg0, arg1);
//			}
//		};
		overlay.setData(pois);
		map.getOverlays().add(overlay);
		map.invalidate();
	}

	public void onGetRGCShareUrlResult(String result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetRGCShareUrlResult,\n" +
				"errorCode:"+error+",result:"+result);
	}

	public void onGetSuggestionResult(MKSuggestionResult result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetSuggestionResult,\n" +
				"errorCode:"+error+",result:"+result);
	}

	public void onGetTransitRouteResult(MKTransitRouteResult result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetTransitRouteResult,\n" +
				"errorCode:"+error+",result:"+result);
	}

	public void onGetWalkingRouteResult(MKWalkingRouteResult result, int error) {
		Log.w("MKSearchInfo","@MyMKSearchListener  onGetWalkingRouteResult,\n" +
				"errorCode:"+error+",result:"+result);
	}
	@Override
	public void onClick(View v) {
		search.poiDetailSearch(uid);
	}

}
*/