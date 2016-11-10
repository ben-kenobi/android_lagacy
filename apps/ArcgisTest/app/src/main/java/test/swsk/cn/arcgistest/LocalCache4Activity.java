package test.swsk.cn.arcgistest;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
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

/**
 * Created by apple on 16/6/3.
 */
public class LocalCache4Activity extends Activity {

    // Map elements
    MapView mMapView;
    ArcGISLocalTiledLayer localTiledLayer;


    private GeodatabaseFeatureTable geodatabaseFeatureTable;
    private FeatureLayer mFeatureLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main);


        mMapView = (MapView) findViewById(R.id.mapView);
        WMSLayer wmslayer = new WMSLayer("http://3dspace.xicp.net:20081/geoserver/fjseism/wms", SpatialReference.create(4326));
//         Add Tile layer to the MapView

        localTiledLayer = new ArcGISLocalTiledLayer("/sdcard/fjs.tpk");
//        mMapView.addLayer(localTiledLayer);

        mMapView.enableWrapAround(true);


        Geodatabase geodatabase = null;
        try {
            geodatabase = new Geodatabase("/sdcard/fjs1geodb");

//get the geodatabase feature table
            geodatabaseFeatureTable = geodatabase.getGeodatabaseFeatureTableByLayerId(0);

//create a feature layer
            mFeatureLayer = new FeatureLayer(geodatabaseFeatureTable);
        mMapView.addLayer(mFeatureLayer);

        } catch (Exception e) {
            e.printStackTrace();
        }





    KmlLayer kml2=new KmlLayer("/sdcard/county.kml");
    mMapView.addLayer(kml2);







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
                                        5,
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
