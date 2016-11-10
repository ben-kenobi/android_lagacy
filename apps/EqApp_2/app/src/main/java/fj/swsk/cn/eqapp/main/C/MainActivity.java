package fj.swsk.cn.eqapp.main.C;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.android.map.ogc.WMSLayer;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Future;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.UpdateManager;
import fj.swsk.cn.eqapp.main.M.LocationInfo;
import fj.swsk.cn.eqapp.main.M.PopInfos;
import fj.swsk.cn.eqapp.main.V.BasePopWindow;
import fj.swsk.cn.eqapp.main.V.ClearEditText;
import fj.swsk.cn.eqapp.main.V.ControlflowPop;
import fj.swsk.cn.eqapp.main.V.EarthquakeDetailPop;
import fj.swsk.cn.eqapp.main.V.EarthquakeDistributionPop;
import fj.swsk.cn.eqapp.main.V.LocationChooserPop;
import fj.swsk.cn.eqapp.main.V.StrAryPopWindow;
import fj.swsk.cn.eqapp.map.DeathTheaticInfo;
import fj.swsk.cn.eqapp.map.DeathTheaticSearchActivity;
import fj.swsk.cn.eqapp.map.DeathThematicCalloutContent;
import fj.swsk.cn.eqapp.map.PoiCalloutContent;
import fj.swsk.cn.eqapp.map.layer.TianDiTuLayerType;
import fj.swsk.cn.eqapp.map.layer.TianDiTuTiledMapServiceLayer;
import fj.swsk.cn.eqapp.map.onPoiItemClick;
import fj.swsk.cn.eqapp.map.search.GraphicUtil;
import fj.swsk.cn.eqapp.map.search.SrchPlaceRslPopup;
import fj.swsk.cn.eqapp.subs.collect.C.EditSceneActivity;
import fj.swsk.cn.eqapp.subs.collect.Common.ImgPicker;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.more.C.EQHisActivity;
import fj.swsk.cn.eqapp.subs.more.Common.MoreClickHandler;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;
import fj.swsk.cn.eqapp.subs.user.C.UserActivity;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;
import rx.Subscription;

import static android.net.Uri.encode;

public class MainActivity extends FragmentActivity implements OnStatusChangedListener,
        View.OnClickListener, StrAryPopWindow.OnPopListClickListener,
        ClearEditText.OnAfterClearTextListener {

    public static int[] sampleImgResIds={R.mipmap.density,R.mipmap.housedamage,R.mipmap.mendown,
    R.mipmap.meninjury,R.mipmap.economiclost};


    private LayoutInflater mLayoutInflater;
    private static final long serialVersionUID = 1L;
    private StrAryPopWindow pickerOptionsPop;
    private LocationChooserPop mLocationChooserPop;
    private EarthquakeDetailPop mEarthquakeDetailPop;
    private EarthquakeDistributionPop mEarthquakeDistributionPop;
    private ControlflowPop mControlflowPop;
    private ViewGroup root;
    LocationDisplayManager lDisplayManager;
    MapView mMapView;
    ClearEditText searchet;
    SrchPlaceRslPopup popup;
    LinearLayout barid;
    InputMethodManager imm;
    /**
     * 底部弹出菜单
     */
    PopupWindow bottom_pop;
    /**
     * POI位置层
     */
    GraphicsLayer poiLayer;
    /**
     * 在线地震灾损专题图层当前索引,-1代表未加载任何类型图层
     */
    int disaDamageWmsLayerPos = -1;
    /**
     * 地震灾损专题在地图中的层级
     */
    long disaDamageWmsLayerLevel = 3;

    ImgPicker mImgPicker;
    static Location curLoc;
    /**
     * 地震位置层，存放不需移除的标志点
     */
    GraphicsLayer earthquakeLayer;
    /**
     * 生命线图层layerId缓存
     */
    Map<Integer, Long> layerIdMap = new HashMap<>();
    /**
     * 底部弹出菜单的锚点
     */
    TextView hideView;
    Point p = null;

    ViewGroup samplelo;
    ImageView sampleimg;
    TextView samplename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PushUtils.loginUser == null) {
            PushUtils.logout(this);
            CommonUtils.log("no  user------");
            return;
        }
        setContentView(R.layout.activity_main);
//        ArcGISRuntime.setClientId("1eFHW78avlnRUPHm");

        root = (ViewGroup) findViewById(R.id.rootrl);
        mImgPicker = new ImgPicker(this);
        mLayoutInflater = getLayoutInflater();
        searchet = (ClearEditText) findViewById(R.id.searchet);
        searchet.setOnAfterClearTextListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        registerMessageReceiver();
        rxLastLocation();
        PushUtils.pushChannelId(this);
        AlarmRec.SetAlarm(this);
        sampleimg=(ImageView)findViewById(R.id.sampleimg);
        samplelo=(ViewGroup)findViewById(R.id.samplelo);
        samplename=(TextView)findViewById(R.id.samplename);
        samplelo.setVisibility(View.GONE);
        EQInfo i = null;
        EQInfo.setIns(i);
    }

    /**
     * 载入地图
     */
    public void initMap() {
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
                //double resolution = mMapView.getResolution() / paramDouble;
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
                    if (queryPoiFeatures(var1, var2)) {
                        return;
                    }
                }
                if (layerIdMap.size() > 0) {
                    for (long layerId : layerIdMap.values()) {
                        queryDeathThetimacFeatures(var1, var2, layerId);
                    }
                }
            }
        });
        //长按地图底部弹出周边查询入口
        mMapView.setOnLongPressListener(new OnLongPressListener() {
            @Override
            public boolean onLongPress(float v, float v1) {
                if (bottom_pop == null) {
                    initBottomMenuPop();
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

    /**
     * 初始化底部弹出菜单界面
     */

    public void initBottomMenuPop() {
        View view = mLayoutInflater.inflate(R.layout.bottom_pop_view, null);
        ImageView imgBnt = (ImageView) view.findViewById(R.id.peripheralQuery);
        hideView = (TextView) findViewById(R.id.hideView);
        imgBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("x", p.getX());
                intent.putExtra("y", p.getY());
                intent.setClass(MainActivity.this, DeathTheaticSearchActivity.class);
                startActivity(intent);
            }
        });
        bottom_pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        bottom_pop.setAnimationStyle(R.style.MenuAnimationFade);
        bottom_pop.setBackgroundDrawable(new BitmapDrawable());
        bottom_pop.setOutsideTouchable(true);
        bottom_pop.setFocusable(true);
        bottom_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                poiLayer.removeAll();
            }
        });
    }

    /**
     * 离线shapefile数据查询
     *
     * @param featureTable
     * @param var1
     * @param var2
     * @return
     */
    public Future<FeatureResult> queryShapeFileFeatures(ShapefileFeatureTable featureTable, float var1, float var2, int tolerance) {
        QueryParameters queryParam = new QueryParameters();
        Point minPoint = mMapView.toMapPoint(var1 - tolerance, var2 - tolerance);
        Point maxPoint = mMapView.toMapPoint(var1 + tolerance, var2 + tolerance);
        queryParam.setGeometry(new Envelope(minPoint.getX()
                , minPoint.getY()
                , maxPoint.getX()
                , maxPoint.getY()));
        queryParam.setSpatialRelationship(SpatialRelationship.CONTAINS);
        queryParam.setOutFields(new String[]{"*"});
        queryParam.setReturnGeometry(true);
        Future<FeatureResult> resultFuture = featureTable.queryFeatures(queryParam, null);
        return resultFuture;
    }

    /**
     * 查询生命线专题要素
     */
    public boolean queryDeathThetimacFeatures(float var1, float var2, long layerId) {

        FeatureLayer layer = (FeatureLayer) mMapView.getLayerByID(layerId);
        final ShapefileFeatureTable featureTable = (ShapefileFeatureTable) layer.getFeatureTable();
        try {
            Future<FeatureResult> resultFuture = queryShapeFileFeatures(featureTable, var1, var2, 50);
            for (Object result : resultFuture.get()) {
                Feature feature = (Feature) result;
                Callout mCallout = mMapView.getCallout();
                mCallout.setStyle(R.xml.calloutstyle);
                Geometry geo = feature.getGeometry();
                Map<String, Object> attributes = feature.getAttributes();
                attributes.put("layerId", layer.getName());
                ViewGroup deathCalloutContent = DeathThematicCalloutContent.getDeathCalloutContent(
                        MainActivity.this,
                        mLayoutInflater,
                        attributes);
                mCallout.setContent(deathCalloutContent);
                if (geo.getType() == Geometry.Type.POLYLINE) {
                    mCallout.show(mMapView.toMapPoint(var1, var2));
                } else if (geo.getType() == Geometry.Type.POINT) {
                    mCallout.show((Point) geo);
                }
                return true;
            }

        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }
        return false;
    }


    /**
     * 查询POI要素信息
     *
     * @param var1
     * @param var2
     */
    public boolean queryPoiFeatures(float var1, float var2) {
        int[] poiIDs = poiLayer.getGraphicIDs(var1, var2, 50);
        if (poiIDs != null && poiIDs.length > 0) {
            //默认第一个为显示的图形
            Graphic g = poiLayer.getGraphic(poiIDs[0]);
            Callout mcallout = mMapView.getCallout();
            mcallout.setStyle(R.xml.calloutstyle);

            ViewGroup poiCalloutContent = PoiCalloutContent.getPoiCalloutContent(
                    mLayoutInflater,
                    g.getAttributes());
            mcallout.setContent(poiCalloutContent);
            mcallout.show((Point) g.getGeometry());
            return true;
        }
        return false;
    }

    /**
     * 启动定位器
     */
    public void startLocating() {
        lDisplayManager = mMapView.getLocationDisplayManager();
        lDisplayManager.start();
        //设置定位模式
        lDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
        //增加误差圆
        lDisplayManager.setAccuracyCircleOn(true);
        lDisplayManager.setAllowNetworkLocation(true);
        lDisplayManager.setLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                curLoc = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    @Override
    public void onClick(View v) { // 地图页面按钮点击事件集中此处处理
        int id = v.getId();
        if (id == R.id.user_manage) {
            startActivity(new Intent(this, UserActivity.class));
        } else if (id == R.id.message) {

            Intent i = new Intent(this, InstructionsListActivity.class);
            startActivity(i);
        } else if (id == R.id.more) {
            initNshowGroupOption();

        } else if (id == R.id.info) {// 地震三要素显示按钮
            showEarthquakePop(v);
        } else if (id == R.id.controlflow) {//
            showControlflowPop(v);
        } else if (id == R.id.distribution) {// 分布图按钮
            showEarthquakeDistributionPop(v);
        } else if (id == R.id.location) {
            locateEq();
        } else if (id == R.id.locationing) {
            if(curLoc==null||"manualprovider".equals(curLoc.getProvider())){
                DialogUtil.getMessageDialogBuilder(this, "定位失败，是否手动选择位置", "提示", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initNshowLocationChooser();
                    }
                }).show();
                return ;
            }
                // 定位到当前人员所处位置
            if (lDisplayManager != null) {
                if (lDisplayManager.isStarted()) {
                    if (lDisplayManager.getAutoPanMode() == AutoPanMode.LOCATION) {
                        //切换到罗盘模式
                        lDisplayManager.setAutoPanMode(AutoPanMode.COMPASS);
                    } else {
                        lDisplayManager.setAutoPanMode(AutoPanMode.LOCATION);
                        mMapView.setRotationAngle(0);
                    }
                }
            }


        } else if (id == R.id.measure) {
            // 测距
        } else if (id == R.id.aspectmeasure) {
            // 测面
        } else if (id == R.id.cam) {
//            mImgPicker.showImagePickDialog();
            if (curLoc == null) {
                CommonUtils.toast("未定位到当前位置，无法进行采集");

            } else {
                Intent intent = new Intent(this, EditSceneActivity.class);
                Tscene ts = new Tscene();
                ts.loc_lat = curLoc.getLatitude();
                ts.loc_lon = curLoc.getLongitude();
                CommonUtils.context.curscene = ts;

                startActivity(intent);
            }
        }else if(id==R.id.lcconfirm){
            mLocationChooserPop.dismiss();
            LocationInfo li =mLocationChooserPop.getSelInfo();
            curLoc=new Location("manualprovider");
            curLoc.setLongitude(li.LON);
            curLoc.setLatitude(li.LAT);
            mMapView.centerAt(curLoc.getLatitude(), curLoc.getLongitude(), true);
            mMapView.setScale(mMapView.getMaxScale() * 16);

        }else if(id==R.id.lccancel){
            mLocationChooserPop.dismiss();

        }
        imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);
    }


    @Override
    public void onclick(BasePopWindow pop, int position) {
//        if (poiLayer != null) {
//            poiLayer.removeAll();
//        }
//        Callout callout = mMapView.getCallout();
//        if (callout != null && callout.isShowing()) {
//            callout.hide();
//        }
        if (pop == pickerOptionsPop) {
            MoreClickHandler.clickAt(position,pickerOptionsPop.adapter.getItem(position).toString(), this);

        } else if (pop == mEarthquakeDistributionPop) {
            samplelo.setVisibility(View.GONE);
            mEarthquakeDistributionPop.adapter.setSelIdx(position);
            // 这里处理 分布图列表点击事件 根据position判断哪个被点击
            if (disaDamageWmsLayerPos != -1) {
                mMapView.removeLayer(mMapView.getLayerByID(disaDamageWmsLayerLevel));
            }
            if (disaDamageWmsLayerPos == position) {
                //点击同一图层，添加移除切换
                disaDamageWmsLayerPos = -1;
                return;
            }


            EQInfo info = EQInfo.getIns();
//            if (info==null || !info.hasLayer()) {
//                CommonUtils.toast("当前没有可展示的分布图");
//                disaDamageWmsLayerPos = -1;
//                return;
//            }


            //WMSLayer wmslayer = new WMSLayer(this.getResources()
//                    .getString(R.string.dis_themMap_url), SpatialReference.create(4326));
            WMSLayer wmslayer = new WMSLayer("http://192.168.1.202:20081/geoserver/fjseism/wms", SpatialReference.create(4490));
          // 设置可见图层

            String visiblelayername = null;
//            switch (position) {
//                case 0:
//                    visiblelayername = "ha01_"+info.obsTime;
//                    break;
//                case 1:
//                    visiblelayername = "ha02_"+info.obsTime;
//                    break;
//                case 2:
//                    visiblelayername = "ha03_"+info.obsTime;
//                    break;
//                case 3:
//                    visiblelayername = "ha04_"+info.obsTime;
//                    break;
//                case 4:
//                    visiblelayername = "ha05_"+info.obsTime;
//                    break;
//                default:
//                    Toast.makeText(MainActivity.this, "无法加载当前图层", Toast.LENGTH_SHORT);
//                    return;
//            }

            samplename.setText(mEarthquakeDistributionPop.adapter.getItem(position) + "");
            sampleimg.setImageResource(sampleImgResIds[position]);
            samplelo.setVisibility(View.VISIBLE);

            wmslayer.setVisibleLayer(new String[]{"ha01_1472195902302"});

            wmslayer.setImageFormat("image/png");
            wmslayer.setOpacity(1);
            //增加到地图中指定层级位置
            mMapView.addLayer(wmslayer);
            disaDamageWmsLayerLevel = wmslayer.getID();

            disaDamageWmsLayerPos = position;
        } else if (pop == mControlflowPop) {
            // 这里处理  选择道路加油站后点击的确定事件
            // 获取被选中要展示的选项下标
            if (layerIdMap.containsKey(position)) {
                mMapView.removeLayer(mMapView.getLayerByID(layerIdMap.get(position)));
                layerIdMap.remove(position);
            } else {
                DeathTheaticInfo inf = getShapefileInfo(this, position);
                for (Map.Entry<Integer, Object> entry : inf.map.entrySet()) {
                    long layerId = addDeathThematicLayer(getResources().getString(entry.getKey()),
                            (Symbol) entry.getValue(), entry.getKey().toString());
                    if (layerId != -1) {
                        layerIdMap.put(position, layerId);
                    }
                }
            }
        }
    }

    /**
     * 获取矢量图层的符号、路径、类型等信息
     *
     * @param position
     * @return
     */
    public static DeathTheaticInfo getShapefileInfo(Context context, int position) {
        Map<Integer, Object> map = new HashMap<>();
        DeathTheaticInfo inf = new DeathTheaticInfo();
        switch (position) {
//            case 0:
//                map.put(R.string.road,
//                        new SimpleLineSymbol(Color.BLUE, 1));
//                break;
//            case 1:
//                map.put(R.string.hydr_station,
//                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
//                break;
//            case 2:
//                map.put(R.string.natgas_station,
//                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
//                break;
            case 0:
                map.put(R.string.school,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.school_1)));
                break;
            case 1:
                map.put(R.string.hospital,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.hospital_i)));
                break;
            case 2:
                map.put(R.string.city_site,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.shelters)));
                break;
            case 6:
                map.put(R.string.resettlement_region,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                break;
            case 7:
                //map.put(R.string.road,
                //        new SimpleLineSymbol(Color.BLUE, 1));
                map.put(R.string.hydr_station,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                map.put(R.string.natgas_station,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                map.put(R.string.school,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.school)));
                map.put(R.string.hospital,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.hospital)));
                map.put(R.string.city_site,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.shelters)));
                map.put(R.string.resettlement_region,
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                break;
            default:
                return null;
        }

        inf.map = map;
        return inf;
    }

    /**
     * 增加生命线专题图层
     *
     * @param sharpFilePath
     * @param symbol
     * @return
     */
    private long addDeathThematicLayer(String sharpFilePath, Symbol symbol, String layerName) {
        try {
            String SDPath = FileUtils.getSDPath();
            String fileName = SDPath + "/" + sharpFilePath;
            File file = new File(fileName);
            if (file.exists()) {
                ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(fileName);
                FeatureLayer featureLayer = new FeatureLayer(shapefileFeatureTable);
                Renderer renderer = new SimpleRenderer(symbol);
                featureLayer.setName(layerName);
                featureLayer.setRenderer(renderer);
                mMapView.addLayer(featureLayer);
                return featureLayer.getID();
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
            Toast.makeText(MainActivity.this, "加载shp图层失败", Toast.LENGTH_SHORT);
        }
        return -1;
    }


    /**
     * 点击弹出下拉列表
     *
     * @param v
     */
    public void onClearEditTextClick(View v) {
        if (popup == null) {

            barid = (LinearLayout) findViewById(R.id.barid);
            ImageButton moreBnt = (ImageButton) findViewById(R.id.more);
            int width = barid.getWidth() - moreBnt.getWidth() - 1;
            popup = new SrchPlaceRslPopup(MainActivity.this,
                    mLayoutInflater,
                    new ArrayList<LinkedHashMap>(),
                    width,
                    new onPoiItemClick() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, LinkedHashMap attributes, long id) {
                            //隐藏callout
                            Callout callout = mMapView.getCallout();
                            if (callout != null && callout.isShowing()) {
                                callout.hide();
                            }
                            if (attributes != null) {
                                double lng = (double) attributes.get("LNG");
                                double lat = (double) attributes.get("LAT");
                                Graphic g = GraphicUtil.createPictureMarkerSymbol(
                                        getResources().getDrawable(R.mipmap.mark),
                                        attributes, lng, lat);
                                if (poiLayer == null) {
                                    poiLayer = new GraphicsLayer();
                                    mMapView.addLayer(poiLayer);
                                }
                                poiLayer.removeAll();
                                poiLayer.addGraphic(g);
                                mMapView.zoomTo((Point) g.getGeometry(), 1);
                            }
                        }
                    });
            popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);
                }
            });
            popup.setInputMethodMode(SrchPlaceRslPopup.INPUT_METHOD_NEEDED);
        }
        if (!popup.isEmpty()) {
            if (!popup.isShowing())
                popup.showAsDropDown(barid, 0, -5);
        }
    }


    /**
     * 查询位置
     *
     * @param v
     */
    public void onSearch(View v) {
        if (!TextUtils.isEmpty(searchet.getText())) {
            if (!popup.isShowing()) {
                popup.showAsDropDown(barid, 0, -5);
            }
            //清空下拉列表数据和地图上的marker
            onAfterClearText(null);
            IConstants.THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    final Map<String, Object> map = JsonTools.getMap(
                            HttpUtils.getJsonContent(
                                    IConstants.poiServiceUrl
                                            + encode(searchet.getText().toString(), "utf-8")
                                            + "?pageIndex=1&pageSize=10"
                            ));
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
                    } else {
                        Toast.makeText(MainActivity.this, "未查询到任何数据", Toast.LENGTH_SHORT);
                    }
                }
            });
        } else {
            Toast.makeText(this, "请输入关键字", Toast.LENGTH_SHORT);
        }
        imm.hideSoftInputFromWindow(searchet.getWindowToken(), 0);
    }

    @Override
    public void onAfterClearText(View v) {
        popup.clearData();
        if (poiLayer != null) {
            poiLayer.removeAll();
            mMapView.getCallout().hide();
        }
    }

    /**
     * 隐患底部弹出菜单
     *
     * @param v
     */
    public void OnHidePopMenu(View v) {
        bottom_pop.dismiss();
        poiLayer.removeAll();
    }


    private void locateEq() {
        EQInfo info = EQInfo.getIns();
        if (info==null) {
            CommonUtils.toast("当前没有地震点");
            return;
        }
        //TODO 定位到地震震源发生位置
        mMapView.centerAt(info.lat, info.lon, true);
        Graphic g = GraphicUtil.createPictureMarkerSymbol(getResources().getDrawable(R.mipmap.mark), null, info.lon, info.lat);
        //创建比较稳定要素的图层
        if (earthquakeLayer == null) {
            earthquakeLayer = new GraphicsLayer();
            mMapView.addLayer(earthquakeLayer);
        }
        earthquakeLayer.removeAll();
        earthquakeLayer.addGraphic(g);
    }


    @Override
    public void onStatusChanged(Object source, STATUS status) {
        if ((source == mMapView) && (status == STATUS.INITIALIZED)) {
            startLocating();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode== EQHisActivity.ReturnFlag){
            locateEq();
            showEarthquakePop(findViewById(R.id.info));
            return ;
        }


//        if (curLoc == null) {
//            CommonUtils.toast("未定位到当前位置，无法保存");
//        }
//        mImgPicker.onActivityResult(requestCode, resultCode, data, curLoc);
    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
        if (lDisplayManager != null) {
//            lDisplayManager.pause();
        }
        isForeground = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
        if (lDisplayManager != null) {
            lDisplayManager.start();
        }
        isForeground = true;

        UpdateManager.check(this, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (lDisplayManager != null) {
//            lDisplayManager.stop();
        }
        rxUnsub();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (PushUtils.loginUser == null) {
            PushUtils.logout(this);
            finish();
            return;
        }
        if (mMapView == null) {
            initMap();
        }
        rxLocationing();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMessageReceiver != null)
            unregisterReceiver(mMessageReceiver);
    }

    private void showControlflowPop(View v) {
        if (mControlflowPop == null) {
            mControlflowPop = new ControlflowPop(v, this, PopInfos.controlflows, PopInfos.controlflowsIcons, this);
        }
        if (mControlflowPop != null)
            mControlflowPop.showAtLocation(root);

//        mControlflowPop.showAtLocation(root);
    }

    private void showEarthquakePop(View v) {
        if (mEarthquakeDetailPop == null) {
            mEarthquakeDetailPop = new EarthquakeDetailPop(v, this);
            mEarthquakeDetailPop.locationing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locateEq();
                }
            });

        }
        if (mEarthquakeDetailPop != null) {
            mEarthquakeDetailPop.updateInfo();
            mEarthquakeDetailPop.showAtLocation(root);
        }
    }

    private void showEarthquakeDistributionPop(View v) {
        if (mEarthquakeDistributionPop == null) {
            mEarthquakeDistributionPop = new EarthquakeDistributionPop(v, this, PopInfos.distributions, PopInfos.distributionIcons, this);

        }
        if (mEarthquakeDistributionPop != null)
            mEarthquakeDistributionPop.showAtLocation(root);

    }

    private void initNshowGroupOption() {
        if (pickerOptionsPop == null) {
            pickerOptionsPop = new StrAryPopWindow(findViewById(R.id.more), this, PopInfos.pickerOptions, PopInfos.pickerOptionIcons, false,
                    (int) getResources().getDimension(R.dimen.dp_180), this);
        }
        if (pickerOptionsPop != null)
            pickerOptionsPop.showAsDropDown();
    }


    private void initNshowLocationChooser(){
        if (mLocationChooserPop == null) {
            mLocationChooserPop = new LocationChooserPop(this);
        }
        if (mLocationChooserPop != null)
            mLocationChooserPop.show();



    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }


    public static boolean isForeground = false;

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                locateEq();
                showEarthquakePop(findViewById(R.id.info));
            }
        }
    }


    Subscription mSubscription;


    private void rxLastLocation() {
//        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
//        locationProvider.getLastKnownLocation()
//                .subscribe(new Action1<Location>() {
//                    @Override
//                    public void call(Location location) {
//                        CommonUtils.log(location + "   =======rxlocation last known");
//                    }
//                });
    }

    private void rxLocationing() {
        //        LocationRequest request = LocationRequest.create() //standard GMS LocationRequest
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setNumUpdates(5)
//                .setInterval(100);
//
//        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(this);
//         mSubscription = locationProvider.getUpdatedLocation(request)
//        .subscribe(new Action1<Location>() {
//            @Override
//            public void call(Location location) {
//                CommonUtils.log(location+"   =======rxlocationing");
//            }
//        });

    }

    private void rxUnsub() {

        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }


}