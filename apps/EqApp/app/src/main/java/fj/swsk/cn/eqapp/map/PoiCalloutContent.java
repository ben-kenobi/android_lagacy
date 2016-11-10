package fj.swsk.cn.eqapp.map;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import fj.swsk.cn.eqapp.R;

/**
 * Created by xul on 2016/6/27.
 */
public class PoiCalloutContent {
    /**
     * 组装一个POI的callout内容页面
     *
     * @param mLayoutInflater
     * @param attributes
     * @return
     */
    public static ViewGroup getPoiCalloutContent(LayoutInflater mLayoutInflater,Map<String, Object> attributes) {
        ViewGroup poiCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.poi_callout_content, null);
        TextView nameView = (TextView) poiCalloutContent.findViewById(R.id.name);
        TextView addressView = (TextView) poiCalloutContent.findViewById(R.id.address);
        TextView telView = (TextView) poiCalloutContent.findViewById(R.id.telephone);
        Object name = attributes.get("NAME");
        Object address = attributes.get("ADDRESS");
        Object tel = attributes.get("TELEPHONE");
        if (name != null) {
            nameView.setText(name.toString());
        }
        if (name != null) {
            addressView.setText(address.toString());
        }
        if (name != null) {
            telView.setText(tel.toString());
        }
        return poiCalloutContent;
    }
}
