package fj.swsk.cn.eqapp.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Map;

import fj.swsk.cn.eqapp.R;

/**
 * Created by xul on 2016/6/27.
 */
public class DeathThematicCalloutContent {
    /**
     * 生命线callout内容
     *
     * @param attributes
     * @return
     */
    public static ViewGroup getDeathCalloutContent(Context context,LayoutInflater mLayoutInflater, Map<String, Object> attributes) {
        ViewGroup deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content, null);
        TextView value1 = (TextView) deathCalloutContent.findViewById(R.id.value1);
        TextView value2 = (TextView) deathCalloutContent.findViewById(R.id.value2);
        TextView value3 = (TextView) deathCalloutContent.findViewById(R.id.value3);
        TextView value4 = (TextView) deathCalloutContent.findViewById(R.id.value4);
        String name_field = context.getResources().getString(R.string.death_name_field);
        String type_field = context.getResources().getString(R.string.death_type_field);
        String tel_field = context.getResources().getString(R.string.death_tel_field);
        String addr_field = context.getResources().getString(R.string.death_addr_field);
        Object name = attributes.get(name_field);
        Object type = attributes.get(type_field);
        Object tel = attributes.get(tel_field);
        Object addr = attributes.get(addr_field);
        if(name!=null) value1.setText(name.toString());
        if(type!=null) value2.setText(type.toString());
        if(tel!=null) value3.setText(tel.toString());
        if(addr!=null) value4.setText(addr.toString());

        return deathCalloutContent;
    }
}
