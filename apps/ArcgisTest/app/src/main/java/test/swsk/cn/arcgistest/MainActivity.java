package test.swsk.cn.arcgistest;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.query.QueryParameters;
import com.esri.core.tasks.query.QueryTask;

import java.util.List;

public class MainActivity extends AppCompatActivity {
     MapView mMapView=null;
    // The basemap switching menu items.
    MenuItem mStreetsMenuItem = null;
    MenuItem mTopoMenuItem = null;
    MenuItem mGrayMenuItem = null;
    MenuItem mOceansMenuItem = null;
    // Create MapOptions for each type of basemap.
    final MapOptions mTopoBasemap = new MapOptions(MapOptions.MapType.TOPO);
    final MapOptions mStreetsBasemap = new MapOptions(MapOptions.MapType.STREETS);
    final MapOptions mGrayBasemap = new MapOptions(MapOptions.MapType.GRAY);
    final MapOptions mOceansBasemap = new MapOptions(MapOptions.MapType.OCEANS);


    ArcGISFeatureLayer mFeatureLayer;
    GraphicsLayer mGraphicsLayer;
    String mFeatureServiceURL;
    MenuItem mQueryUsMenuItem = null;
    MenuItem mQueryCaMenuItem = null;
    MenuItem mQueryFrMenuItem = null;
    MenuItem mQueryAuMenuItem = null;
    MenuItem mQueryBrMenuItem = null;



    GraphicsLayer mLocationLayer;
    Point mLocationLayerPoint;
    String mLocationLayerPointString;
    boolean mIsMapLoaded = false;
    EditText mSearchEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Retrieve the map and initial extent from XML layout
        mMapView = (MapView) findViewById(R.id.map);
// Enable map to wrap around date line.
//        mMapView.enableWrapAround(true);

        // Get the feature service URL from values->strings.xml
        mFeatureServiceURL = this.getResources().getString(R.string.featureServiceURL);
// Add Feature layer to the MapView
        mFeatureLayer = new ArcGISFeatureLayer(mFeatureServiceURL, ArcGISFeatureLayer.MODE.ONDEMAND);
//        mMapView.addLayer(mFeatureLayer);
// Add Graphics layer to the MapView
        mGraphicsLayer = new GraphicsLayer();
        mMapView.addLayer(mGraphicsLayer);


        mLocationLayer = new GraphicsLayer();
        mMapView.addLayer(mLocationLayer);

        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            public void onStatusChanged(Object source, STATUS status) {
                if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
                    mIsMapLoaded = true;
                }
            }
        });
    }

    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    protected void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar,menu);
//        // Get the basemap switching menu items.
//        mStreetsMenuItem = menu.getItem(0);
//        mTopoMenuItem = menu.getItem(1);
//        mGrayMenuItem = menu.getItem(2);
//        mOceansMenuItem = menu.getItem(3);
//
//// Also set the topo basemap menu item to be checked, as this is the default.
//        mTopoMenuItem.setChecked(true);



        // Get the query params menu items.
//        mQueryUsMenuItem = menu.getItem(0);
//        mQueryCaMenuItem = menu.getItem(1);
//        mQueryFrMenuItem = menu.getItem(2);
//        mQueryAuMenuItem = menu.getItem(3);
//        mQueryBrMenuItem = menu.getItem(4);




        View searchRef = menu.findItem(R.id.action_search).getActionView();
        mSearchEditText = (EditText) searchRef.findViewById(R.id.searchText);

        mSearchEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    onSearchButtonClicked(mSearchEditText);
                    return true;
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.World_Street_Map:
                mMapView.setMapOptions(mStreetsBasemap);
                mStreetsMenuItem.setChecked(true);
                return true;
            case R.id.World_Topo:
                mMapView.setMapOptions(mTopoBasemap);
                mTopoMenuItem.setChecked(true);
                return true;
            case R.id.Gray:
                mMapView.setMapOptions(mGrayBasemap);
                mGrayMenuItem.setChecked(true);
                return true;
            case R.id.Ocean_Basemap:
                mMapView.setMapOptions(mOceansBasemap);
                mOceansMenuItem.setChecked(true);
                return true;

            case R.id.Query_US:
                mQueryUsMenuItem.setChecked(true);
                new QueryFeatureLayer().execute("United States");
                return true;
            case R.id.Query_CA:
                mQueryCaMenuItem.setChecked(true);
                new QueryFeatureLayer().execute("Canada");
                return true;
            case R.id.Query_FR:
                mQueryFrMenuItem.setChecked(true);
                new QueryFeatureLayer().execute("France");
                return true;
            case R.id.Query_AU:
                mQueryAuMenuItem.setChecked(true);
                new QueryFeatureLayer().execute("Australia");
                return true;
            case R.id.Query_BR:
                mQueryBrMenuItem.setChecked(true);
                new QueryFeatureLayer().execute("Brazil");
                return true;
            case R.id.action_search:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onSearchButtonClicked(View view){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        String address = mSearchEditText.getText().toString();
        executeLocatorTask(address);
    }







    private class QueryFeatureLayer extends AsyncTask<String, Void, FeatureResult> {
        @Override
        protected FeatureResult doInBackground(String... params) {

            String whereClause = "CNTRY_NAME='" + params[0] + "'";

            // Define a new query and set parameters
            QueryParameters mParams = new QueryParameters();
            mParams.setWhere(whereClause);
            mParams.setReturnGeometry(true);

            // Define the new instance of QueryTask
            QueryTask queryTask = new QueryTask(mFeatureServiceURL);
            FeatureResult results;

            try {
                // run the querytask
                results = queryTask.execute(mParams);
                return results;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(FeatureResult results) {

            // Remove the result from previously run query task
            mGraphicsLayer.removeAll();

            // Define a new marker symbol for the result graphics
            SimpleMarkerSymbol sms = new SimpleMarkerSymbol(Color.BLUE, 5, SimpleMarkerSymbol.STYLE.CIRCLE);

            // Envelope to focus on the map extent on the results
            Envelope extent = new Envelope();

            // iterate through results
            for (Object element : results) {
                // if object is feature cast to feature
                if (element instanceof Feature) {
                    Feature feature
                            = (Feature) element;
                    // convert feature to graphic
                    Graphic graphic = new Graphic(feature.getGeometry(), sms, feature.getAttributes());
                    // merge extent with point
                    extent.merge((Point)graphic.getGeometry());
                    // add it to the layer
                    mGraphicsLayer.addGraphic(graphic);
                }
            }

            // Set the map extent to the envelope containing the result graphics
            mMapView.setExtent(extent, 100);
        }
    }



    private void executeLocatorTask(String address) {
        // Create Locator parameters from single line address string
        LocatorFindParameters findParams = new LocatorFindParameters(address);

        // Use the centre of the current map extent as the find location point
        findParams.setLocation(mMapView.getCenter(), mMapView.getSpatialReference());

        // Calculate distance for find operation
        Envelope mapExtent = new Envelope();
        mMapView.getExtent().queryEnvelope(mapExtent);
        // assume map is in metres, other units wont work, double current envelope
        double distance = (mapExtent != null && mapExtent.getWidth() > 0) ? mapExtent.getWidth() * 2 : 10000;
        findParams.setDistance(distance);
        findParams.setMaxLocations(2);

        // Set address spatial reference to match map
        findParams.setOutSR(mMapView.getSpatialReference());

        // Execute async task to find the address
        new LocatorAsyncTask().execute(findParams);
        mLocationLayerPointString = address;
    }

    private class LocatorAsyncTask extends AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {
        private Exception mException;
        @Override
        protected List<LocatorGeocodeResult> doInBackground(LocatorFindParameters... params) {
            mException = null;
            List<LocatorGeocodeResult> results = null;
            Locator locator = Locator.createOnlineLocator();
            try {
                results = locator.find(params[0]);
            } catch (Exception e) {
                mException = e;
            }
            return results;
        }


        protected void onPostExecute(List<LocatorGeocodeResult> result) {
            if (mException != null) {
                Log.w("PlaceSearch", "LocatorSyncTask failed with:");
                mException.printStackTrace();
                Toast.makeText(MainActivity.this, getString(R.string.addressSearchFailed), Toast.LENGTH_LONG).show();
                return;
            }

            if (result.size() == 0) {
                Toast.makeText(MainActivity.this, getString(R.string.noResultsFound), Toast.LENGTH_LONG).show();
            } else {
                // Use first result in the list
                LocatorGeocodeResult geocodeResult = result.get(0);

                // get return geometry from geocode result
                Point resultPoint = geocodeResult.getLocation();
                // create marker symbol to represent location
                SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(Color.RED, 16, SimpleMarkerSymbol.STYLE.CROSS);
                // create graphic object for resulting location
                Graphic resultLocGraphic = new Graphic(resultPoint, resultSymbol);
                // add graphic to location layer
                mLocationLayer.addGraphic(resultLocGraphic);

                // create text symbol for return address
                String address = geocodeResult.getAddress();
                TextSymbol resultAddress = new TextSymbol(20, address, Color.BLACK);
                // create offset for text
                resultAddress.setOffsetX(-4 * address.length());
                resultAddress.setOffsetY(10);
                // create a graphic object for address text
                Graphic resultText = new Graphic(resultPoint, resultAddress);
                // add address text graphic to location graphics layer
                mLocationLayer.addGraphic(resultText);

                mLocationLayerPoint = resultPoint;

                // Zoom map to geocode result location
                mMapView.zoomToResolution(geocodeResult.getLocation(), 2);
            }
        }
    }
}
