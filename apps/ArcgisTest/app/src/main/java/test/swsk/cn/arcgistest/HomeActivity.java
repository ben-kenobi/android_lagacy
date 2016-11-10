package test.swsk.cn.arcgistest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by apple on 16/5/25.
 *
 */
public class HomeActivity extends AppCompatActivity {
    MapView mMapView=null;
    GraphicsLayer mGraphicsLayer;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        mMapView = (MapView) findViewById(R.id.map);
//        mGraphicsLayer = new GraphicsLayer();
//        mMapView.addLayer(mGraphicsLayer);
////        GeodatabaseFeatureServiceTable tb=new GeodatabaseFeatureServiceTable("",100);
////        FeatureLayer layer =new FeatureLayer(tb);
////        mMapView.addLayer(layer);
//
//
//        // instantiates a class break renderer
//        ClassBreaksRenderer cityPopRenderer = new ClassBreaksRenderer();
//// set the attribute field used by renderer to match values
//        cityPopRenderer.setField("POP1990");
//// set the minimum population value = MIN_POP
//        cityPopRenderer.setMinValue(0);
//
//// create a class break representing the first
//// low class break
//        ClassBreak lowClassBreak = new ClassBreak();
//// set label as Low
//        lowClassBreak.setLabel("Low");
//// set class break max value
//        lowClassBreak.setClassMaxValue(50000);
//// set a pre-defined simple marker symbol
//        lowClassBreak.setSymbol( new SimpleMarkerSymbol(0xff00dd55, 5, SimpleMarkerSymbol.STYLE.CIRCLE)
//        );
//
//// create two more class breaks representing the
//// mid and max class breaks
//        ClassBreak midClassBreak = new ClassBreak();
//        midClassBreak.setLabel("Middle");
//        midClassBreak.setClassMaxValue(250000);
//        midClassBreak.setSymbol(new SimpleMarkerSymbol(0xff0055dd, 5, SimpleMarkerSymbol.STYLE.CIRCLE));
//        ClassBreak highClassBreak = new ClassBreak();
//        highClassBreak.setLabel("High");
//        highClassBreak.setClassMaxValue(2500000);
//        highClassBreak.setSymbol(new SimpleMarkerSymbol(0xffdd5555, 5, SimpleMarkerSymbol.STYLE.CIRCLE));
//
//// add class breaks to the class break renderer
//        cityPopRenderer.addClassBreak(lowClassBreak);
//        cityPopRenderer.addClassBreak(midClassBreak);
//        cityPopRenderer.addClassBreak(highClassBreak);
//
//// add the class break renderer to the graphics layer
//        mGraphicsLayer.setRenderer(cityPopRenderer);
//
//
//
//    }
    private GeodatabaseFeatureTable geodatabaseFeatureTable;
    private FeatureLayer featureLayer;
    public File convertIS2F(InputStream is){
        File f = null;
        try{
            f=File.createTempFile("mytempmap","tmp");
            FileOutputStream fos = new FileOutputStream(f);
            byte buf[]=new byte[1024*5];
            int count=0;
            while((count = is.read(buf))>0){
                fos.write(buf,0,count);
            }
            fos.flush();
            fos.close();
            is.close();
        }catch (Exception e){}
        return f;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        Geodatabase geodatabase = null;
        try {
//            File f = convertIS2F(getAssets().open("xian.shp"));
//            Log.e("itag",f.getAbsolutePath());
//            Log.e("itag",f+"");
//            File file = new File("/sdcard/fjs");
//            Log.e("itag",file.length()+ "|||"+f.length());
            geodatabase = new Geodatabase("/sdcard/fjs1geodb");
        }catch (Exception e){
            e.printStackTrace();
        }

//get the geodatabase feature table
        geodatabaseFeatureTable = geodatabase.getGeodatabaseFeatureTableByLayerId(0);
//create a feature layer
        featureLayer = new FeatureLayer(geodatabaseFeatureTable);

//        mMapView.addLayer(featureLayer);

    }

        @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("itag", "" + mMapView.getSpatialReference());
        Log.e("itag", "" + mMapView.getCenter());

//        Log.e("itag", "" + mGraphicsLayer.getSpatialReference());
        return super.onTouchEvent(event);
    }
}
