package com.icanit.app.bmap;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.icanit.app.R;
import com.icanit.app.common.IConstants;
import com.icanit.app.entity.AppCommunity;
import com.icanit.app.util.AppUtil;

/**public class BMapActivity extends MapActivity implements LocationListener {
	private BMapManager mapManager;
	private MapView map;
	private MKSearch search;
	private MapController mapController;
	private EditText editText;
	private ImageButton textDisposer,backButton;
	private Button searchConfirmButton;
	public  GeoPoint centerPoint;
	private MKLocationManager locationManager;
	private MyLocationOverlay myLocationOverlay;
	private TextView textView;
	private AppCommunity community;
	private CommunityBoundOverlay cbo=new CommunityBoundOverlay();
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_bmap);
		init();
		initMapNsearch();
		bindListeners();
	}

	private void init() {
		mapManager = AppUtil.getMapUtilInstance().getMapManager();
		initMapActivity(mapManager);
//		locationManager = mapManager.getLocationManager();
		map = (MapView) findViewById(R.id.mapView1);
		mapController = map.getController();
		search = new MKSearch();
		editText = (EditText) findViewById(R.id.editText1);
//		myLocationOverlay = new MyLocationOverlay(this,map);
//		map.getOverlays().add(myLocationOverlay);
		map.setBuiltInZoomControls(true);
		textDisposer=(ImageButton)findViewById(R.id.imageButton1);
		backButton=(ImageButton)findViewById(R.id.imageButton2);
		searchConfirmButton=(Button)findViewById(R.id.button1);
		textView=(TextView)findViewById(R.id.textView1);
		community=(AppCommunity)getIntent().getSerializableExtra(IConstants.COMMUNITY_INFO);
		textView.setText(community.commName);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		AppUtil.bindSearchTextNtextDisposerNsearchConfirm(editText, textDisposer,searchConfirmButton);
		search.init(mapManager, new MyMKSearchListener(map,this,search,cbo));
		search.geocode(community.commName,community.cityName );
		searchConfirmButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				String text = editText.getText().toString();
				if(!AppUtil.isNumber(text)){
					Toast.makeText(BMapActivity.this, "无效数值输入", Toast.LENGTH_SHORT).show();return;
				}
				int distance= Integer.parseInt(text);
				cbo.setDistance(distance);
				search.poiSearchNearBy("kfc", centerPoint, distance);
			}
		});
	}

	private void initMapNsearch() {
		
	}

	protected boolean isRouteDisplayed() {
		return false;
	}

	protected void onResume() {
		super.onResume();
		AppUtil.getMapUtilInstance().startMap(myLocationOverlay, locationManager, this, mapManager);
	}

	protected void onStop() {
		super.onStop();
		AppUtil.getMapUtilInstance().stopMap(myLocationOverlay, locationManager, this, mapManager);
	}

	public void onLocationChanged(Location location) {
		if (location != null) {
			mapController.setCenter(new GeoPoint(
					(int) (location.getLatitude() * 1E6), (int) (location
							.getLongitude() * 1E6)));
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			AppUtil.getMapUtilInstance().finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}*/
