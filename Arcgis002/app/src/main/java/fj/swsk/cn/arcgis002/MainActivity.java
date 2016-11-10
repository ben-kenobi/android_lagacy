package fj.swsk.cn.arcgis002;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.esri.android.map.Layer;
import com.esri.android.map.MapView;

public class MainActivity extends AppCompatActivity {
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initMap() {
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.enableWrapAround(true);
//        mMapView.setOnStatusChangedListener(this);
        //国家天地图
        //TianDiTuTiledMapServiceLayer t_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.VEC_C);
        //final TianDiTuTiledMapServiceLayer t_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.CVA_C);
        //福建省天地图
        final TianDiTuTiledMapServiceLayer t_fj_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_VEC_C);
        final TianDiTuTiledMapServiceLayer t_fj_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_CVA_C);
        mMapView.addLayers(new Layer[]{t_fj_vec, t_fj_cva});
    }
}
