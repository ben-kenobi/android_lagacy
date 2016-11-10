package test.swsk.cn.tianditutst;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.geostar.android.map.MapView;
import com.geostar.android.map.ogc.WMTSLayer;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;//地图对象

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mapView = new MapView(this, "");//实例化地图对象
            mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

            String vectorUrl = "http://t0.tianditu.cn/vec_c/wmts?LAYER=vec&TILEMATRIXSET=c";
            WMTSLayer vectorTiledLayer = new WMTSLayer(vectorUrl);
//            vectorTiledLayer.setBufferEffect(true);
            String labelUrl = "http://t0.tianditu.cn/cva_c/wmts?LAYER=cva&TILEMATRIXSET=c";
            WMTSLayer labelTiledLayer = new WMTSLayer(labelUrl);
            mapView.addLayer(vectorTiledLayer);
            mapView.addLayer(labelTiledLayer);
            mapView.setLevel(5);
            //设置地图初始化中心点
            mapView.setCenter(114.555,30.515);
            setContentView(mapView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
