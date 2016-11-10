package com.icanit.app.bmap;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.Projection;
import com.icanit.app.R;
import com.icanit.app.util.AppUtil;

public class CommunityBoundOverlay  extends Overlay{
	private GeoPoint gp;
	private int distance;
	private static Drawable marker = AppUtil.appContext.getResources().getDrawable(R.drawable.icon_map_position);
	public CommunityBoundOverlay(){
	}
	
	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	

	public GeoPoint getGp() {
		return gp;
	}

	public void setGp(GeoPoint gp) {
		this.gp = gp;
	}

//	@Override
//	public void draw(Canvas canvas, MapView map, boolean shadow) {
//		Paint paint = new Paint();
//		Projection projection = map.getProjection();
//		Point p=projection.toPixels(gp, null);
//		int height = marker.getIntrinsicHeight();
//		int width = marker.getIntrinsicWidth();
//		canvas.drawBitmap(BitmapFactory.decodeResource(AppUtil.appContext.getResources(),R.drawable.icon_map_position)
//				,new Rect(0, 0, width,height), new Rect(p.x-width/2,p.y-height/2,p.x+width/2,p.y+height/2), paint);
//		Point subp=projection.toPixels(new GeoPoint(gp.getLatitudeE6()+(int)(distance/0.111),gp.getLongitudeE6()), null);
//		int radius = Math.abs(subp.y-p.y);
//		paint.setColor(Color.argb(0x88, 0x00, 0x00, 0xff));
//		paint.setStyle(Style.STROKE);
//		paint.setStrokeWidth(2);
//		canvas.drawCircle(p.x, p.y, radius, paint);
//		paint.setStyle(Style.FILL);
//		paint.setColor(Color.argb(0x33, 0x00, 0x00, 0xff));
//		canvas.drawCircle(p.x, p.y, radius, paint);
//	}

		
	
}
