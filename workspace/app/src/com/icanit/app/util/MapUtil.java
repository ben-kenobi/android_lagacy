package com.icanit.app.util;

import android.content.Context;
import android.location.LocationListener;
import android.util.Log;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MyLocationOverlay;


public class MapUtil {
	private static MapUtil maputil;
	private  BMapManager mapManager=null;
	private static final String MAP_KEY = "5740715164CD9A5C151EB580D63CCC61D9AC138F";
	private boolean initSuccess = false;
	private Context context;
	private MapUtil(Context context){
		this.context=context;
	}
	public static MapUtil getInstance(Context context){
		if(maputil==null)
			synchronized (MapUtil.class){ 
				if(maputil==null)
					maputil=new MapUtil(context);
			}
		return maputil;
	}
	public BMapManager getMapManager(){
		if(mapManager==null){
			mapManager=new BMapManager(context.getApplicationContext());
			initSuccess=mapManager.init(MAP_KEY, new MKGeneralListener() {
				public void onGetPermissionState(int error) {
					Log.w("mapInfo","@mapApplication onGetPermissionState "+error);
					if(error==MKEvent.ERROR_PERMISSION_DENIED)
						Log.w("mapInfo","@mapApplication onGetPermissionState  MAP_KEY is not valid  "+error);
				}
				public void onGetNetworkState(int error) {
					Log.w("mapInfo","@mapApplication  onGetNetworState "+error);
					Log.w("mapInfo","@mapApplication  onGetNetworState  NETWORKException  "+error);
				}
			});
			if(initSuccess){
//				mapManager.getLocationManager().setNotifyInternal(10,5);
			}else{
			}
		}
		return mapManager;
	}
//	public void startMap(MyLocationOverlay overlay,MKLocationManager locationManager,
//			LocationListener listener,BMapManager mapManager){
//		if(overlay!=null){
//			overlay.enableCompass();overlay.enableMyLocation();
//			}
//		if(locationManager!=null){
//			locationManager.requestLocationUpdates(listener);
//			locationManager.enableProvider(MKLocationManager.MK_GPS_PROVIDER);
//		}
//		if(mapManager!=null)
//		mapManager.start();
//	}
//	public void stopMap(MyLocationOverlay overlay,MKLocationManager locationManager,
//			LocationListener listener,BMapManager mapManager){
//		if(overlay!=null){
//			overlay.disableCompass();overlay.disableMyLocation();
//			}
//		if(locationManager!=null){
//			locationManager.removeUpdates(listener);
//			locationManager.disableProvider(MKLocationManager.MK_GPS_PROVIDER);
//		}
//		if(mapManager!=null)
//		mapManager.stop();
//	}
	public  void finalize() throws Throwable {
		if(mapManager!=null)
			mapManager.destroy();
		mapManager=null;
		super.finalize();
	}
	
}
