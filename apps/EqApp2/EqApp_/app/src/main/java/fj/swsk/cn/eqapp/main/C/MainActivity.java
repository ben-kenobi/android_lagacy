package fj.swsk.cn.eqapp.main.C;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.M.PopInfos;
import fj.swsk.cn.eqapp.main.V.BasePopWindow;
import fj.swsk.cn.eqapp.main.V.ClearEditText;
import fj.swsk.cn.eqapp.main.V.ControlflowPop;
import fj.swsk.cn.eqapp.main.V.EarthquakeDetailPop;
import fj.swsk.cn.eqapp.main.V.EarthquakeDistributionPop;
import fj.swsk.cn.eqapp.main.V.StrAryPopWindow;
import fj.swsk.cn.eqapp.map.layer.TianDiTuLayerType;
import fj.swsk.cn.eqapp.map.layer.TianDiTuTiledMapServiceLayer;
import fj.swsk.cn.eqapp.map.onPoiItemClick;
import fj.swsk.cn.eqapp.map.search.GraphicUtil;
import fj.swsk.cn.eqapp.map.search.SrchPlaceRslPopup;
import fj.swsk.cn.eqapp.subs.user.C.UserActivity;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;


public class MainActivity extends FragmentActivity implements OnStatusChangedListener,View.OnClickListener,StrAryPopWindow.OnPopListClickListener,ClearEditText.OnAfterClearTextListener {

    public MapView mMapView;
    public boolean mIsMapLoaded=false;

    public static final long serialVersionUID = 1L;
    public LocationDisplayManager lDisplayManager;

    public  StrAryPopWindow pickerOptionsPop;
    public EarthquakeDetailPop mEarthquakeDetailPop;
    public EarthquakeDistributionPop mEarthquakeDistributionPop;
    public ControlflowPop mControlflowPop;
    public ViewGroup root;

    public LayoutInflater mLayoutInflater;

    public ClearEditText searchet;
    public SrchPlaceRslPopup popup;
    public  LinearLayout barid;
    public  InputMethodManager imm;
    /**
     * 底部弹出菜单
     */
    public PopupWindow bottom_pop;
    /**
     * POI位置层
     */
    public GraphicsLayer poiLayer;
    /**
     * 在线地震灾损专题图层当前索引,-1代表未加载任何类型图层
     */
    public  int disaDamageWmsLayerPos = -1;
    /**
     * 地震灾损专题在地图中的层级
     */
    public final int disaDamageWmsLayerLevel = 3;


    /**
     * 生命线图层layerId缓存
     */
    public  Map<Integer, Long> layerIdMap = new HashMap<>();
    /**
     * 底部弹出菜单的锚点
     */
    public  TextView hideView;
    public Point p = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root=(ViewGroup)findViewById(R.id.rootrl);



        mLayoutInflater = getLayoutInflater();
//        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");
        searchet = (ClearEditText)findViewById(R.id.searchet);
        searchet.setOnAfterClearTextListener(this);
        imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    }


    private void initMap(){
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.enableWrapAround(true);
        mMapView.setOnStatusChangedListener(this);

//国家天地图
        //TianDiTuTiledMapServiceLayer t_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.VEC_C);
        //final TianDiTuTiledMapServiceLayer t_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.CVA_C);
        //福建省天地图
        final TianDiTuTiledMapServiceLayer t_fj_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_VEC_C);
        final TianDiTuTiledMapServiceLayer t_fj_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_CVA_C);
        mMapView.addLayers(new Layer[]{t_fj_vec, t_fj_cva});//, t_vec, t_cva
        mMapView.setOnZoomListener(new OnZoomListener() {

            @Override
            public void preAction(float paramFloat1, float paramFloat2,
                                  double paramDouble) {
                double resolution = mMapView.getResolution() / paramDouble;
                //if (resolution > 0.0109) {
                // t_fj_vec.setVisible(false);
                // t_fj_cva.setVisible(false);
                //} else {
                //t_fj_vec.setVisible(true);
                // t_fj_cva.setVisible(true);
                // }
            }

            @Override
            public void postAction(float paramFloat1, float paramFloat2,
                                   double paramDouble) {
                // TODO Auto-generated method stub
                //放缩后刷新标注层，防止叠加
                //t_cva.refresh();
                t_fj_cva.refresh();
            }
        });
        /**
         * 实现直接点击地图查询POI、生命线属性信息
         */
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float var1, float var2) {
                imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);
                mMapView.getCallout().hide();
                if (poiLayer != null && poiLayer.isVisible()) {
                    MainMapHandler.queryPoiFeatures(MainActivity.this,var1, var2);
                } else if (layerIdMap.size() > 0) {
                    for (long layerId : layerIdMap.values()) {
                        MainMapHandler.queryDeathThetimacFeatures(MainActivity.this,var1, var2, layerId);
                    }
                }
            }
        });
        //长按地图底部弹出周边查询入口
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float v, float v1) {
                if (bottom_pop == null) {
                    MainMapHandler.initBottomMenuPop(MainActivity.this);
                }
                p = mMapView.toMapPoint(v, v1);
                Graphic g = GraphicUtil.createPictureMarkerSymbol(
                        getResources().getDrawable(R.mipmap.mark),
                        null, p.getX(), p.getY());
                if (poiLayer == null) {
                    poiLayer = new GraphicsLayer();
                    mMapView.addLayer(poiLayer);
                }
                poiLayer.removeAll();
                poiLayer.addGraphic(g);
                mMapView.getCallout().hide();
                mMapView.zoomTo((Point) g.getGeometry(), 1);
                bottom_pop.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) { // 地图页面按钮点击事件集中此处处理
        int id = v.getId();
        if(id==R.id.user_manage){
            startActivity(new Intent(this, UserActivity.class));
        }else if(id==R.id.more){
            initNshowGroupOption();

        }else if(id == R.id.info){// 地震三要素显示按钮
            showEarthquakePop(v);
        }else if(id==R.id.controlflow){//
            showControlflowPop(v);
        }else if(id==R.id.distribution){// 分布图按钮
            showEarthquakeDistributionPop(v);
        }else if(id == R.id.location){
            // 定位到地震震源发生位置
        }else if(id == R.id.locationing){
            // 定位到当前人员所处位置
            if(lDisplayManager!=null&&lDisplayManager.isStarted()){
                if(lDisplayManager.getAutoPanMode()== LocationDisplayManager.AutoPanMode.COMPASS){
                    mMapView.setRotationAngle(0);
                    lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
                }else{
                    lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.COMPASS);
                }
            }

        }else if(id == R.id.measure){
            // 测距
        }else if(id==R.id.aspectmeasure){
            // 测面
        }
        imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);

    }

    public void onSearch(View v){
        if(!TextUtils.isEmpty(searchet.getText())){
            if(!popup.isShowing()){
                popup.showAsDropDown(barid,0,-5);
            }
            onAfterClearText(null);
            IConstants.THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    final Map<String, Object> map = JsonTools.getMap(
                            HttpUtils.getJsonContent(IConstants.poiServiceUrl
                                    + Uri.encode(searchet.getText().toString(), "utf-8") +
                                    "?pageIndex=1&pageSize=10"));

                    if (map != null && (int) map.get("count") > 0) {
                        IConstants.MAIN_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                final ArrayList<LinkedHashMap> result = (ArrayList<LinkedHashMap>) map.get("result");
                                if (!popup.isShowing()) {
                                    popup.showAsDropDown(barid, 0, -5);
                                }
                                popup.updateData(result);
                            }
                        });
                    }
                }
            });

        }else{
            CommonUtils.toast("请输入关键字");
        }
        imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);

    }




    @Override
    public void onclick(BasePopWindow pop,int position) {
        if(pop == pickerOptionsPop){

        }else if(pop == mEarthquakeDistributionPop){
            // 这里处理 分布图列表点击事件 根据position判断哪个被点击
            MainMapHandler.onDistributionPopClicked(this,position);
        }else if(pop == mControlflowPop){
            // 这里处理  选择道路加油站后点击的确定事件
            MainMapHandler.onControlflowPopClicked(this,position);
        }
    }


    public void startLocating() {
        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.start();
        //设置定位模式
        lDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.LOCATION);
        //增加误差圆
        lDisplayManager.setAccuracyCircleOn(true);
        lDisplayManager.setShowLocation(true);
        lDisplayManager.setAllowNetworkLocation(true);

    }


    @Override
    public void onStatusChanged(Object source, STATUS status) {
        if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
            mIsMapLoaded = true;
            startLocating();

        }
    }


    @Override
    public void onAfterClearText(View v) {
        popup.clearData();
        if(poiLayer!=null){
            poiLayer.removeAll();
            mMapView.getCallout().hide();
        }
    }

    public void onClearEditTextClick(View v){
        if(popup==null){
            barid = (LinearLayout)findViewById(R.id.barid);
            ImageButton moreBnt = (ImageButton) findViewById(R.id.more);
            int width = barid.getWidth()-moreBnt.getWidth()-1;
            popup=new SrchPlaceRslPopup(this, mLayoutInflater, new ArrayList<LinkedHashMap>(),
                    width, new onPoiItemClick() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, LinkedHashMap attributes, long id) {
                    Callout callout = mMapView.getCallout();
                    if(callout!=null && callout.isShowing()){
                        callout.hide();
                    }
                    if(attributes!=null){
                        double lon = (double)attributes.get("LNG");
                        double lat = (double)attributes.get("LAT");
                        Graphic g = GraphicUtil.createPictureMarkerSymbol(
                                getResources().getDrawable(R.mipmap.mark),
                                attributes,lon,lat);
                        if(poiLayer == null){
                            poiLayer=new GraphicsLayer();
                            mMapView.addLayer(poiLayer);
                        }
                        poiLayer.removeAll();
                        poiLayer.addGraphic(g);
                        mMapView.zoomTo((Point)g.getGeometry(),1);
                    }
                }
            });
            popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    imm.hideSoftInputFromWindow(searchet.getWindowToken(),0);
                }
            });
            popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        }

        if(!popup.isEmpty()){
            if(!popup.isShowing()){
                popup.showAsDropDown(barid,0,-5);
            }
        }

    }
    public void OnHidePopMenu(View v) {
        bottom_pop.dismiss();
        poiLayer.removeAll();
    }















































    private void showControlflowPop(View v){
        if(mControlflowPop==null){
            mControlflowPop= new ControlflowPop(v,this,PopInfos.controlflows,PopInfos.controlflowsIcons,this);
        }
        if(mControlflowPop!=null)
            mControlflowPop.showAtLocation(root);

    }
    private void showEarthquakePop(View v){
        if(mEarthquakeDetailPop==null){
            mEarthquakeDetailPop=new EarthquakeDetailPop(v,this);

        }
        if(mEarthquakeDetailPop!=null)
            mEarthquakeDetailPop.showAtLocation(root);
    }

    private void showEarthquakeDistributionPop(View v){
        if(mEarthquakeDistributionPop==null){
            mEarthquakeDistributionPop=new EarthquakeDistributionPop(v,this, PopInfos.distributions,PopInfos.distributionIcons,this);

        }
        if(mEarthquakeDistributionPop!=null)
            mEarthquakeDistributionPop.showAtLocation(root);

    }


    private void initNshowGroupOption() {
        if(pickerOptionsPop==null){
            pickerOptionsPop=new StrAryPopWindow(findViewById(R.id.more),this,PopInfos.pickerOptions,PopInfos.pickerOptionIcons,false,
                    (int)getResources().getDimension(R.dimen.dp_180),this);
        }
        if(pickerOptionsPop!=null)
            pickerOptionsPop.showAsDropDown();
    }


    protected void onPause() {
        super.onPause();
        mMapView.pause();
        if (lDisplayManager != null) {
            lDisplayManager.pause();
        }
    }

    protected void onResume() {
        super.onResume();
        mMapView.unpause();
        if (lDisplayManager != null) {
            lDisplayManager.start();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (lDisplayManager != null) {
            lDisplayManager.stop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMapView == null) {
            initMap();
        }
    }
}

























