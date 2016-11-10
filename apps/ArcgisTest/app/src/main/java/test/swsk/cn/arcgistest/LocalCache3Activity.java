package test.swsk.cn.arcgistest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.runtime.ArcGISRuntime;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.Iterator;

/**
 * Created by apple on 16/6/3.
 */
public class LocalCache3Activity extends Activity {

    // Map elements
    ArcGISLocalTiledLayer localTiledLayer;


    private LocationListener locationListener = new LocationListener() {
        /**
         * 位置信息变化时触发
         */
        public void onLocationChanged(Location location) {
            markLocation(location);
        }

        /**
         * 状态改变时调用
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                //GPS状态为可见时
                case LocationProvider.AVAILABLE:
                    showToast("当前GPS状态为可见状态");
                    Log.i("TAG", "当前GPS状态为可见状态");
                    break;
                //GPS状态为服务区外时
                case LocationProvider.OUT_OF_SERVICE:
                    showToast("当前GPS状态为服务区外状态");
                    Log.i("TAG", "当前GPS状态为服务区外状态");
                    break;
                //GPS状态为暂停服务时
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    showToast("当前GPS状态为暂停服务状态");
                    Log.i("TAG", "当前GPS状态为暂停服务状态");
                    break;
            }
        }

        /**
         * GPS开启时触发
         */
        public void onProviderEnabled(String provider) {
            showToast("GPS打开");
            if (ActivityCompat.checkSelfPermission(LocalCache3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocalCache3Activity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locMag.getLastKnownLocation(provider);
            markLocation(location);
        }

        /**
         * GPS禁用时触发
         */
        public void onProviderDisabled(String provider) {
            showToast("GPS已关闭");
        }
    };
    MapView mapview;
    GraphicsLayer gLayerGps;
    Toast toast;
    LocationManager locMag;
    Location loc;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapview = (MapView) findViewById(R.id.mapView);
        localTiledLayer = new ArcGISLocalTiledLayer("/sdcard/fjs.tpk");
        mapview.addLayer(localTiledLayer);

        mapview.enableWrapAround(true);

        //去除水印
        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        //要定位在地图中的位置，需要知道当前位置，而当前位置有Location对象决定，
        //但是，Location对象又需要LocationManager对象来创建。
        //创建LocationManager的唯一方法
        locMag = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        gLayerGps = new GraphicsLayer();
        mapview.addLayer(gLayerGps);

      mapview.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;
            LocationDisplayManager lDisplayManager;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mapview && status == STATUS.INITIALIZED) {
                    if (!locMag.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        showToast("请开启GPS导航...");
                        //返回开启GPS导航设置界面
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 0);
                        return;
                    }
                    Location location = locMag.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    markLocation(location);
                    locMag.addGpsStatusListener(listener);
                    locMag.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

                }
            }
        });
    }

    //状态监听
    GpsStatus.Listener listener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            switch (event) {
                //第一次定位
                case GpsStatus.GPS_EVENT_FIRST_FIX:
                    Log.i("TAG", "第一次定位");
                    break;
                //卫星状态改变
                case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                    Log.i("TAG", "卫星状态改变");
                    //获取当前状态
                    GpsStatus gpsStatus = locMag.getGpsStatus(null);
                    //获取卫星颗数的默认最大值
                    int maxSatellites = gpsStatus.getMaxSatellites();
                    //创建一个迭代器保存所有卫星
                    Iterator<GpsSatellite> iters = gpsStatus.getSatellites().iterator();
                    int count = 0;
                    while (iters.hasNext() && count <= maxSatellites) {
                        GpsSatellite s = iters.next();
                        count++;
                    }
                    System.out.println("搜索到：" + count + "颗卫星");
                    break;
                //定位启动
                case GpsStatus.GPS_EVENT_STARTED:
                    Log.i("TAG", "定位启动");
                    break;
                //定位结束
                case GpsStatus.GPS_EVENT_STOPPED:
                    Log.i("TAG", "定位结束");
                    break;
            }
        }

        ;
    };

    private void markLocation(Location location) {
        if (location != null) {
            Log.i("TAG", "时间：" + location.getTime());
            Log.i("TAG", "经度：" + location.getLongitude());
            Log.i("TAG", "纬度：" + location.getLatitude());
            Log.i("TAG", "海拔：" + location.getAltitude());
            double locx = location.getLongitude();
            double locy = location.getLatitude();
            ShowPointOnMap(locx, locy);
        }
    }

    public void ShowPointOnMap(double lon, double lat) {
        //清空定位图层
        gLayerGps.removeAll();
        //接收到的GPS的信号X(lat),Y(lon)
        double locx = lon;
        double locy = lat;
        Point wgspoint = new Point(locx, locy);
        Point mapPoint = (Point) GeometryEngine.project(wgspoint, SpatialReference.create(4326), mapview.getSpatialReference());
        //图层的创建
//		Graphic graphic = new Graphic(mapPoint,new SimpleMarkerSymbol(Color.RED,18,STYLE.CIRCLE));
        PictureMarkerSymbol pms = new PictureMarkerSymbol(this.getResources().getDrawable(
                R.drawable.ic_download));
        Graphic graphic = new Graphic(mapPoint, pms);
        gLayerGps.addGraphic(graphic);
    }


    private void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapview.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.unpause();
    }
}

