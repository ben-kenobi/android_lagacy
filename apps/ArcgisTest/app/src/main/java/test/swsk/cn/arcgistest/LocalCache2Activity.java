package test.swsk.cn.arcgistest;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.android.map.ogc.kml.KmlLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.symbol.PictureMarkerSymbol;

import java.util.ArrayList;

/**
 * Created by apple on 16/6/3.
 */
public class LocalCache2Activity extends Activity {

    // Map elements
    MapView mMapView;
    ArcGISLocalTiledLayer localTiledLayer;

    // action bar menu items
    MenuItem selectLevels;
    MenuItem download;
    MenuItem switchMaps;

    boolean isLocalLayerVisible = false;

    // The generated tile cache will be a compact cache
    boolean createAsTilePackage = true;

    double[] levels;

    final CharSequence[] items = {"Level ID:0", "Level ID:1", "Level ID:2",
            "Level ID:3", "Level ID:4", "Level ID:5", "Level ID:6",
            "Level ID:7", "Level ID:8", "Level ID:9",};

    double[] mapResolution = {156543.03392800014, 78271.51696399994,
            39135.75848200009, 19567.87924099992, 9783.93962049996,
            4891.96981024998, 2445.98490512499, 1222.992452562495,
            611.4962262813797, 305.74811314055756};


    boolean[] itemsChecked = new boolean[items.length];
    ArrayList<Double> levelsArraylist = new ArrayList<Double>();
    // path to persist data to disk
    static String DEFAULT_BASEMAP_PATH;
    // tile package url
    String tileURL;

    private static String defaultPath = null;
    final static double SEARCH_RADIUS = 5;


    private GeodatabaseFeatureTable geodatabaseFeatureTable;
    private FeatureLayer mFeatureLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);

        DEFAULT_BASEMAP_PATH = getResources().getString(R.string.offline_dir);
        defaultPath = Environment.getExternalStorageDirectory().getPath()
                + DEFAULT_BASEMAP_PATH;

        // Initialize MapView and extent
        mMapView = (MapView) findViewById(R.id.mapView);

        // Add Tile layer to the MapView

        localTiledLayer = new ArcGISLocalTiledLayer("/sdcard/fjs.tpk");
        mMapView.addLayer(localTiledLayer);

        mMapView.enableWrapAround(true);


        Geodatabase geodatabase = null;
        try {
            geodatabase = new Geodatabase("/sdcard/fjs1geodb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        KmlLayer kml=new KmlLayer("http://192.168.1.202:8189/geoserver/rgyx/wms/kml?layers=rgyx:city_light");
        mMapView.addLayer(kml);
      String  wmsURL = "http://192.168.1.202:8189/geoserver/rgyx/wms";
        WMSLayer wmsLayer = new WMSLayer(wmsURL);
        wmsLayer.setImageFormat("image/png");
        // available layers
        String[] visibleLayers = {"rgyx:airport"};
        wmsLayer.setVisibleLayer(visibleLayers);
        wmsLayer.setTransparent(true);
        // add layer to map
        mMapView.addLayer(wmsLayer);

//get the geodatabase feature table
        geodatabaseFeatureTable = geodatabase.getGeodatabaseFeatureTableByLayerId(0);

//create a feature layer
        mFeatureLayer = new FeatureLayer(geodatabaseFeatureTable);
//        mMapView.addLayer(mFeatureLayer);
        mFeatureLayer.setSelectionColor(Color.BLUE);
        mFeatureLayer.setOpacity(1);

        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float v, float v1) {
                Log.e("itag", v + "------" + v1);
                Log.e("itag", mMapView.toMapPoint(v, v1) + "===========");
            }
        });
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {

            private static final long serialVersionUID = 1L;
            LocationDisplayManager lDisplayManager;

            @Override
            public void onStatusChanged(Object source, STATUS status) {
                if (source == mMapView && status == STATUS.INITIALIZED) {
//                    mMapView.centerAndZoom(119.266354, 26.003557, 13);
//                    mMapView.zoomToScale(mMapView.getCenter(), 1);
//                    Log.e("itag", mMapView.getCenter()
//                            + "===========");
                    Log.e("itag", mMapView.getScale() + "===========");

                    lDisplayManager = mMapView.getLocationDisplayManager();
                    lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                    lDisplayManager.setShowLocation(true);
                    lDisplayManager.setAllowNetworkLocation(true);
                    try{
                    lDisplayManager.setDefaultSymbol(new PictureMarkerSymbol(getResources().getDrawable(R.drawable.ic_download)));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.e("itag", lDisplayManager.getLocation() + "--------------");

                    lDisplayManager.setLocationListener(new LocationListener() {

                        boolean locationChanged = false;

                        // Zooms to the current location when first GPS fix arrives.
                        @Override
                        public void onLocationChanged(Location loc) {
                            if (!locationChanged) {
                                Log.e("itag", loc + "=================");
                                locationChanged = true;
                                double locy = loc.getLatitude();
                                double locx = loc.getLongitude();
                                Point wgspoint = new Point(locx, locy);
                                Point mapPoint = (Point) GeometryEngine
                                        .project(wgspoint,
                                                SpatialReference.create(4326),
                                                mMapView.getSpatialReference());

                                Unit mapUnit = mMapView.getSpatialReference()
                                        .getUnit();
                                double zoomWidth = Unit.convertUnits(
                                        SEARCH_RADIUS,
                                        Unit.create(LinearUnit.Code.MILE_US),
                                        mapUnit);
                                Envelope zoomExtent = new Envelope(mapPoint,
                                        zoomWidth, zoomWidth);
                                mMapView.setExtent(zoomExtent);

                            }
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {
                            Log.e("itag", lDisplayManager.getLocation() + "0--------------");
                        }

                        @Override
                        public void onProviderEnabled(String provider) {
                            Log.e("itag", lDisplayManager.getLocation() + "1--------------");
                        }

                        @Override
                        public void onProviderDisabled(String provider) {
                            Log.e("itag", lDisplayManager.getLocation() + "2--------------");
                        }
                    });
                }
            }
        });


    }
}
