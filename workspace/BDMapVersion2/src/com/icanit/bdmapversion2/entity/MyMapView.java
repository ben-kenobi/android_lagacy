package com.icanit.bdmapversion2.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MyMapView extends MapView{

	public MyMapView(Context arg0) {
		
		super(arg0);
		
	}
	
	public MyMapView(Context context, AttributeSet set)  
    {  
        super(context, set);  
    }  
      
    public MyMapView(Context context, AttributeSet set, int i)  
    {  
        super(context, set, i);  
    }  
      
    @Override  
    public boolean onTouchEvent(MotionEvent arg0) {  
        // TODO Auto-generated method stub  
        int x = (int)arg0.getX();  
        int y = (int)arg0.getY();  
        GeoPoint geoPoint = this.getProjection().fromPixels(x, y);  
        int xx = geoPoint.getLongitudeE6();  
        int yy = geoPoint.getLatitudeE6();  
        Log.i("纬经度", "("+Integer.toString(yy)+","+Integer.toString(xx)+")");
        Log.i("点击的坐标xxxxxxxxxxx", Integer.toString(xx));  
        Log.i("点击的坐标yyyyyyyyyyy", Integer.toString(yy));  
        return super.onTouchEvent(arg0);  
    }  

}
