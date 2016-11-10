package fj.swsk.cn.eqapp.map;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.CallbackListener;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureResult;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.io.File;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.C.MainActivity;
import fj.swsk.cn.eqapp.map.layer.TianDiTuLayerType;
import fj.swsk.cn.eqapp.map.layer.TianDiTuTiledMapServiceLayer;
import fj.swsk.cn.eqapp.map.search.GraphicUtil;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.FileUtils;

public class DeathThematicQueryMapActivity extends BaseTopbarActivity {
    MapView mMapView;
    Intent intent;
    GraphicsLayer glayer;
    LayoutInflater mLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_death_thematic_query_map;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);

        intent = getIntent();
        mLayoutInflater = getLayoutInflater();
    }

    public void onExit(View v) {
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.unpause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMapView == null) {
            initMap();
        }
    }

    public void initMap() {
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.enableWrapAround(true);
        //国家天地图
        //TianDiTuTiledMapServiceLayer t_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.VEC_C);
        //final TianDiTuTiledMapServiceLayer t_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.CVA_C);
        //福建省天地图
        final TianDiTuTiledMapServiceLayer t_fj_vec = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_VEC_C,FileUtils.getSDPath() + "/eqAppData/mapLayer/vec_fj");
        final TianDiTuTiledMapServiceLayer t_fj_cva = new TianDiTuTiledMapServiceLayer(TianDiTuLayerType.FJ_CVA_C,FileUtils.getSDPath() + "/eqAppData/mapLayer/cia_fj");
        mMapView.addLayers(new Layer[]{t_fj_vec, t_fj_cva});//t_vec, t_cva,
        mMapView.setOnZoomListener(new OnZoomListener() {

            @Override
            public void preAction(float paramFloat1, float paramFloat2,
                                  double paramDouble) {
                // double resolution = mMapView.getResolution() / paramDouble;
//                if (resolution > 0.0109) {
//                    t_fj_vec.setVisible(false);
//                    t_fj_cva.setVisible(false);
//                } else {
//                    t_fj_vec.setVisible(true);
//                    t_fj_cva.setVisible(true);
//                }
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
        mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if ((o == mMapView) && (status == STATUS.INITIALIZED)) {
                    glayer = new GraphicsLayer();
                    mMapView.addLayer(glayer);
                    final Dialog dialog = DialogUtil.progressD(DeathThematicQueryMapActivity.this);
                    dialog.show();
                    int position = intent.getIntExtra("deathThematic", -1);
                    double distance = intent.getDoubleExtra("distance", -1);
                    final DeathTheaticInfo inf = MainActivity.getShapefileInfo(DeathThematicQueryMapActivity.this, position);
                    for (final Map.Entry<Integer, Object> entry : inf.map.entrySet()) {
                        String sharpFilePath = getResources().getString(entry.getKey());
                        final PictureMarkerSymbol symbol = (PictureMarkerSymbol) entry.getValue();
                        String SDPath = FileUtils.getSDPath();
                        try {
                            String fileName = SDPath + "/" + sharpFilePath;
                            File file = new File(fileName);
                            if (file.exists()) {
                                ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(fileName);
                                //此处粗略地将米转化为弧度
                                double degree = distance / 33 / 3600;
                                Polygon circle = drawTool.getCircle(new Point(intent.getDoubleExtra("x", -1),
                                        intent.getDoubleExtra("y", -1)), degree);
                                queryShapeFileFeatures(shapefileFeatureTable,
                                        circle,
                                        SpatialRelationship.CONTAINS,
                                        new CallbackListener<FeatureResult>() {
                                            @Override
                                            public void onCallback(FeatureResult resultFuture) {
                                                for (Object result : resultFuture) {
                                                    Feature feature = (Feature) result;
                                                    Point p = (Point) feature.getGeometry();
                                                    Map<String, Object> attributes = feature.getAttributes();
                                                    attributes.put("layerId", entry.getKey());
                                                    Graphic g = GraphicUtil.createPictureMarkerSymbol(
                                                            symbol,
                                                            attributes,
                                                            p.getX(),
                                                            p.getY());
                                                    glayer.addGraphic(g);
                                                }
                                                dialog.dismiss();
                                            }

                                            @Override
                                            public void onError(Throwable var1) {
                                                dialog.dismiss();
                                            }
                                        }
                                );
                            }
                        } catch (Exception e) {
                            Log.e("MainActivity", e.toString());
                        }
                    }
                }
            }
        });
        mMapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float var1, float var2) {
                mMapView.getCallout().hide();
                queryDeathFeaturesInGLayr(var1, var2);
            }
        });
    }

    public void queryShapeFileFeatures(ShapefileFeatureTable featureTable,
                                       Geometry geo,
                                       SpatialRelationship spatialRel,
                                       final CallbackListener<FeatureResult> callback) {
        QueryParameters queryParam = new QueryParameters();
        queryParam.setGeometry(geo);
        queryParam.setSpatialRelationship(spatialRel);
        queryParam.setOutFields(new String[]{"*"});
        queryParam.setReturnGeometry(true);
        featureTable.queryFeatures(queryParam, new CallbackListener<FeatureResult>() {
            @Override
            public void onCallback(FeatureResult var1) {

                callback.onCallback(var1);
            }

            @Override
            public void onError(Throwable var1) {

                callback.onError(var1);
            }
        });
    }

    /**
     * 在Graphiclayer层查询要素信息
     *
     * @param var1
     * @param var2
     */
    public void queryDeathFeaturesInGLayr(float var1, float var2) {
        int[] poiIDs = glayer.getGraphicIDs(var1, var2, 50);
        if (poiIDs != null && poiIDs.length > 0) {
            //默认第一个为显示的图形
            Graphic g = glayer.getGraphic(poiIDs[0]);
            Callout mcallout = mMapView.getCallout();
            mcallout.setStyle(R.xml.calloutstyle);

            ViewGroup deathCalloutContent = DeathThematicCalloutContent.getDeathCalloutContent(
                    DeathThematicQueryMapActivity.this,
                    mLayoutInflater,
                    g.getAttributes());
            mcallout.setContent(deathCalloutContent);
            Geometry geo = g.getGeometry();
            if (geo.getType() == Geometry.Type.POLYLINE) {
                Polyline pline = (Polyline) geo;
                mcallout.show((Point) pline.getPoint(0));
            } else if (geo.getType() == Geometry.Type.POINT) {
                mcallout.show((Point) geo);
            }
        }
    }
}

