package fj.swsk.cn.eqapp.main.C;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.FeatureLayer;
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
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.query.QueryParameters;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.map.DeathTheaticInfo;
import fj.swsk.cn.eqapp.map.DeathTheaticSearchActivity;
import fj.swsk.cn.eqapp.map.DeathThematicCalloutContent;
import fj.swsk.cn.eqapp.map.PoiCalloutContent;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.FileUtils;

/**
 * Created by apple on 16/6/30.
 */
public class MainMapHandler {
    public static void onDistributionPopClicked(MainActivity ac,int position){
        if(ac.disaDamageWmsLayerPos!=-1){
            ac.mMapView.removeLayer(ac.disaDamageWmsLayerLevel);
        }
        if(ac.disaDamageWmsLayerPos==position){
            ac.disaDamageWmsLayerPos=-1;
            return ;
        }

        WMSLayer wmsLayer=new WMSLayer(ac.getResources().getString(R.string.dis_themMap_url),
                SpatialReference.create(4326));
        int levelnameids[]={R.string.buiCollapseDis_themMap,
                            R.string.deathsDis_themMap,
                            R.string.injDis_themMap,
                            R.string.ecoLoss_themMap};
        if(position>levelnameids.length-1){
            CommonUtils.toast("无法加载当前图层");
            return ;
        }
        wmsLayer.setVisibleLayer(new String[]{ac.getResources().getString(levelnameids[position])});
        wmsLayer.setImageFormat("image/png");
        wmsLayer.setOpacity(0.5f);
        ac.mMapView.addLayer(wmsLayer,ac.disaDamageWmsLayerLevel);
        ac.disaDamageWmsLayerPos=position;


    }

    public static void onControlflowPopClicked(MainActivity ac,int pos){
        if(ac.layerIdMap.containsKey(pos)){
            ac.mMapView.removeLayer(ac.mMapView.getLayerByID(ac.layerIdMap.get(pos)));
            ac.layerIdMap.remove(pos);
        }else{
            DeathTheaticInfo info = getShapefileInfo(ac,pos);
            for (Map.Entry<String,Object> en:info.map.entrySet()){
                long layerid = addDeathThematicLayer(ac,en.getKey(), (Symbol) en.getValue());
                if(layerid!=-1){
                    ac.layerIdMap.put(pos,layerid);
                }
            }
        }
    }



    public static long addDeathThematicLayer(MainActivity ac,String sharpFilePath, Symbol symbol) {

        try {
            String SDPath = FileUtils.getSDPath();

            ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(
                    SDPath + "/" + sharpFilePath);
            FeatureLayer featureLayer = new FeatureLayer(shapefileFeatureTable);
            Renderer renderer = new SimpleRenderer(symbol);
            featureLayer.setRenderer(renderer);
            ac.mMapView.addLayer(featureLayer);
            return featureLayer.getID();
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
            Toast.makeText(ac, "加载shp图层失败", Toast.LENGTH_SHORT);
        }
        return -1;
    }

    public static DeathTheaticInfo getShapefileInfo(Context context, int position) {
        Map<String, Object> map = new HashMap<>();
        switch (position) {
            case 0:
                map.put(context.getResources().getString(R.string.road),
                        new SimpleLineSymbol(Color.BLUE, 1));
                break;
            case 1:
                map.put(context.getResources().getString(R.string.gas_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.gas_station)));
                break;
            case 2:
                map.put(context.getResources().getString(R.string.hydr_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                break;
            case 3:
                map.put(context.getResources().getString(R.string.natgas_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                break;
            case 4:
                map.put(context.getResources().getString(R.string.school),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.school)));
                break;
            case 5:
                map.put(context.getResources().getString(R.string.hospital),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                break;
            case 6:
                map.put(context.getResources().getString(R.string.shelter),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.shelters)));
                break;
            case 7:
                map.put(context.getResources().getString(R.string.resettlement_region),
                        new SimpleFillSymbol(Color.RED));
                break;
            case 8:
                //map.put(context.getResources().getString(R.string.road),
                //        new SimpleLineSymbol(Color.BLUE, 1));
                map.put(context.getResources().getString(R.string.gas_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.gas_station)));
                map.put(context.getResources().getString(R.string.hydr_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                map.put(context.getResources().getString(R.string.natgas_station),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                map.put(context.getResources().getString(R.string.school),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.school)));
                map.put(context.getResources().getString(R.string.hospital),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.mark)));
                map.put(context.getResources().getString(R.string.shelter),
                        new PictureMarkerSymbol(context.getResources().getDrawable(R.mipmap.shelters)));
                //map.put(context.getResources().getString(R.string.resettlement_region),
                //         new SimpleFillSymbol(Color.RED));
                break;
            default:
                return null;
        }
        DeathTheaticInfo inf = new DeathTheaticInfo();
        inf.map = map;
        return inf;
    }


    public static void initBottomMenuPop(final MainActivity ac){
        View view =ac. mLayoutInflater.inflate(R.layout.bottom_pop_view,
                null,false);
        ImageView imgbtn = (ImageView)view.findViewById(R.id.peripheralQuery);
        ac.hideView=(TextView)ac.findViewById(R.id.hideView);
        imgbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("x",ac.p.getX());
                intent.putExtra("y",ac. p.getY());
                intent.setClass(ac, DeathTheaticSearchActivity.class);
                ac.startActivity(intent);

            }
        });
        ac. bottom_pop=new PopupWindow(view,-1,-2,true);
        ac.bottom_pop.setAnimationStyle(R.style.MenuAnimationFade);
        ac.bottom_pop.setBackgroundDrawable(new BitmapDrawable());
        ac.bottom_pop.setOutsideTouchable(true);
        ac.bottom_pop.setFocusable(true);
        ac.bottom_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ac.poiLayer.removeAll();
            }
        });


    }


    public  static Future<FeatureResult> queryShapeFileFeatures(MainActivity ac,ShapefileFeatureTable featureTable,
                                                        float var1,float var2,int tolerance){
        QueryParameters queryParam = new QueryParameters();
        Point minPoint = ac.mMapView.toMapPoint(var1 - tolerance, var2 - tolerance);
        Point maxPoint = ac.mMapView.toMapPoint(var1 + tolerance, var2 + tolerance);
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



    public static void  queryDeathThetimacFeatures(MainActivity ac,float f1,float f2,long layerid){
        FeatureLayer layer = (FeatureLayer)ac.mMapView.getLayerByID(layerid);
        final ShapefileFeatureTable featureTable = (ShapefileFeatureTable)layer.getFeatureTable();
        try {
            Future<FeatureResult> resultFuture = queryShapeFileFeatures(ac,featureTable, f1, f2, 50);

            for (Object result : resultFuture.get()) {
                Feature feature = (Feature) result;
                Callout mCallout = ac.mMapView.getCallout();
                mCallout.setStyle(R.xml.calloutstyle);
                Geometry geo = feature.getGeometry();
                Map<String, Object> attributes = feature.getAttributes();
                ViewGroup deathCalloutContent = DeathThematicCalloutContent.getDeathCalloutContent(
                        ac,
                        ac.mLayoutInflater,
                        attributes);
                mCallout.setContent(deathCalloutContent);

                mCallout.show((Point) geo);
                break;
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.toString());
        }
    }

    public static void queryPoiFeatures(MainActivity ac,float f1,float f2){
        int[] poiIDs = ac.poiLayer.getGraphicIDs(f1,f2,50);
        if(poiIDs!=null && poiIDs.length>0){
            Graphic g=ac.poiLayer.getGraphic(poiIDs[0]);
            Callout mcallout = ac.mMapView.getCallout();
            mcallout.setStyle(R.xml.calloutstyle);
            mcallout.setContent(PoiCalloutContent.getPoiCalloutContent(ac.mLayoutInflater, g.getAttributes()));
            mcallout.show((Point)g.getGeometry());

        }
    }

}
