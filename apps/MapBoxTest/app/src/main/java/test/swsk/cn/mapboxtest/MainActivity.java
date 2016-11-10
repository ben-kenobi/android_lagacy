package test.swsk.cn.mapboxtest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.widgets.UserLocationView;
import com.mapbox.mapboxsdk.offline.OfflineManager;
import com.mapbox.mapboxsdk.offline.OfflineRegion;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.mapbox.mapboxsdk.offline.OfflineRegionError;
import com.mapbox.mapboxsdk.offline.OfflineRegionStatus;
import com.mapbox.mapboxsdk.offline.OfflineTilePyramidRegionDefinition;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private MapView mapView;
    private MapboxMap mMapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                // Set map style
                mMapboxMap=mapboxMap;
                mapboxMap.setStyleUrl(Style.MAPBOX_STREETS);

                // Set the camera's starting position
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(44.885, 126)) // set the camera's center position
                        .zoom(12)  // set the camera's zoom level
                        .tilt(20)  // set the camera's tilt
                        .build();

                // Move the camera to that position
                mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.mipmap.ic_launcher);
                Icon icon = iconFactory.fromDrawable(iconDrawable);

                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(44.875, 126))
                        .title("Hello World!")
                        .snippet("Welcome to my marker.").icon(icon));

                mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        return true;
                    }
                });
                location();
            }
        });
    }


    private void location(){
        mMapboxMap.setMyLocationEnabled(true);
        UserLocationView v = (UserLocationView)mapView.findViewById(R.id.userLocationView);
        Log.e("itag", v.getLocation().toString());
        LocationServices services = LocationServices.getLocationServices(this);
        if(!services.isGPSEnabled()){
            services.toggleGPS(true);
        }
        Location l = services.getLastLocation();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(l.getLatitude(), l.getLongitude())) // set the camera's center position
                .zoom(14)  // set the camera's zoom level
                .tilt(20)  // set the camera's tilt
                .build();
        mMapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }
    private void cache(){
        OfflineManager mOfflineManager = OfflineManager.getInstance(this);
        mOfflineManager.setAccessToken(ApiAccess.accssToken(this));

// Definition
        String styleURL = mMapboxMap.getStyleUrl();
        LatLngBounds bounds = mMapboxMap.getProjection().getVisibleRegion().latLngBounds;
        double minZoom = mMapboxMap.getMinZoom();
        double maxZoom = mMapboxMap.getMaxZoom();
        float pixelRatio = this.getResources().getDisplayMetrics().density;
        OfflineRegionDefinition definition = new OfflineTilePyramidRegionDefinition(
                styleURL, bounds, minZoom, maxZoom, pixelRatio);

// Your metadata
        Map<String,String> map = new HashMap<>();
        map.put("name","fuzhou");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(map);
        }catch(Exception e){}
        byte metadata[] = bos.toByteArray();

// Create region
        mOfflineManager.createOfflineRegion(definition, metadata,
                new OfflineManager.CreateOfflineRegionCallback() {
                    @Override
                    public void onCreate(OfflineRegion offlineRegion) {
                        offlineRegion.setDownloadState(OfflineRegion.STATE_ACTIVE);
                        offlineRegion.setObserver(new OfflineRegion.OfflineRegionObserver() {
                            @Override
                            public void onStatusChanged(OfflineRegionStatus status) {
                                Log.e("itag", "status: " +status.getRequiredResourceCount()+"----"+ status.getCompletedResourceCount());
                            }

                            @Override
                            public void onError(OfflineRegionError error) {
                                Log.e("itag", "11error: " + error);
                            }

                            @Override
                            public void mapboxTileCountLimitExceeded(long limit) {
                                Log.e("itag", "exceeded: " + limit);
                            }
                        });

                        Log.e("itag", "Offline region created: " + "fuzhou");
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("itag", "Error: " + error);
                    }
                });
    }


    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }






















    void callPhone(String num){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PRIVILEGED)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PRIVILEGED)) {



            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PRIVILEGED},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else {
            Intent intent = new Intent("android.intent.action.CALL_PRIVILEGED", Uri.parse("tel:" + num));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    void callPhone2(String num){


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {



            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else{
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }


    public void dial(){

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(new PhoneStateListener() {
            boolean comingPhone = false;

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_IDLE:/* 无任何状态 */
                        Log.d("itag", "phone idle");
                        if (this.comingPhone) {
                            this.comingPhone = false;
//                            backMyApp();
                            setSpeekModle(false);
                        }
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:/* 接起电话 */
                        Log.d("itag", "phone answer");
                        this.comingPhone = true;
                        setSpeekModle(true);
                        break;
                    case TelephonyManager.CALL_STATE_RINGING:/* 电话进来 */
                        Log.d("itag", "phone coming");
                        this.comingPhone = true;
                        setSpeekModle(true);
                        break;
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);


    }


    void setSpeekModle(boolean open){
        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        //audioManager.setMode(AudioManager.ROUTE_SPEAKER);
        int currVolume = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        audioManager.setMode(AudioManager.MODE_IN_CALL);

        if(!audioManager.isSpeakerphoneOn()&&true==open) {
            audioManager.setSpeakerphoneOn(true);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }else if(audioManager.isSpeakerphoneOn()&&false==open){
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,currVolume,
                    AudioManager.STREAM_VOICE_CALL);
        }
    }
}
