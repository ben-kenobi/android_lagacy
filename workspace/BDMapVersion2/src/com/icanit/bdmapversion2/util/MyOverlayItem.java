package com.icanit.bdmapversion2.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.icanit.bdmapversion2.R;
import com.icanit.bdmapversion2.activity.IntentActivity;
import com.icanit.bdmapversion2.activity.LocationActivity;

public class MyOverlayItem extends ItemizedOverlay<OverlayItem>{
	private Context mContext = null;
    static PopupOverlay pop = null;
    public static GeoPoint selectedPoint;
    private Toast mToast=null;
    private List<OverlayItem> mList;
    
    public MyOverlayItem(Drawable marker,Context context, MapView mapView,List<OverlayItem> list){
		super(marker,mapView);
		this.mContext = context;
		this.mList=list;
	}
    
	protected boolean onTap(int index) {
		//item点击事件 			
		//用textView充当转成BitMap
	    View popview = LayoutInflater.from(mContext).inflate(R.layout.infowindow, null);// 获取要转换的View资源  
	    TextView tv_business_name = (TextView)popview.findViewById(R.id.bmap_business_name);
	    TextView tv_business_addr=(TextView)popview.findViewById(R.id.bmap_business_addr);
	    tv_business_name.setText(mList.get(index).getTitle());//将每个点的Title在弹窗中以文本形式显示出来        	
	    tv_business_addr.setText(mList.get(index).getSnippet());
	    tv_business_name.setBackgroundColor(android.graphics.Color.WHITE);
	    tv_business_addr.setBackgroundColor(android.graphics.Color.WHITE);     
	    Bitmap popbitmap = convertViewToBitmap(popview);
	    
		final int mIndex=index;  
		selectedPoint=getItem(mIndex).getPoint();
		 pop = new PopupOverlay(mMapView,new PopupClickListener() {
				
				@Override
				public void onClickedPopup(int index) { 					
					Log.e("=============", "被点击的"+index);
					// 这是个bug 根本不能用这个回调  index
				    Toast.makeText(mContext, "正在为您加载 "+getItem(mIndex).getTitle(), Toast.LENGTH_SHORT).show();
				    Intent intent=new Intent(mContext,IntentActivity.class);
					intent.putExtra("title", getItem(mIndex).getTitle());
					mContext.startActivity(intent);				    
				}
			});
			
	    Bitmap[] bmps = new Bitmap[1];   
	    try {
	    	bmps[0]= popbitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} 
	 
	    //直接到社区 刚才我的locData没数值 所以包括 要先定位
	    if(LocationActivity.mLocData.latitude==0.0){
	    	showToast("请先进行定位");
	    }else{
	    	GeoPoint mLoc = new GeoPoint((int)(LocationActivity.mLocData.latitude* 1E6),(int)(LocationActivity.mLocData.longitude* 1E6));
	    	double distance = DistanceUtil.getDistance(mLoc, getItem(index).getPoint());
	 	    BigDecimal  b= new BigDecimal(distance);   
	 	    double  mDistance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(); 	 	    
	 	    pop.showPopup(bmps, getItem(index).getPoint(), 32); 
	 	    showToast(getItem(index).getTitle()+"\n距离我: "+mDistance+"米");
	 		super.onTap(index);
	    }
		return true;
	}

	public boolean onTap(GeoPoint pt, MapView mapView){
		// 在此处理MapView的点击事件，当返回 true时
		if (pop != null){
			pop.hidePop();
		}
		super.onTap(pt,mapView);
		return false;
	}
	
	public void showToast(String text) {  
        if(mToast == null) {  
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT); 
            mToast.setGravity(Gravity.CENTER, 0, 186);
        } else {        
            mToast.setGravity(Gravity.CENTER, 0, 186);
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();  
    }  
	
	public static Bitmap convertViewToBitmap(View view) {		
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        view.buildDrawingCache();  
        Bitmap bitmap = view.getDrawingCache();  
	    return bitmap;  
	}
}
