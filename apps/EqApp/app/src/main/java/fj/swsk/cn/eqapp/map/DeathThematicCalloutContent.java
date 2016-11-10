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
    public static ViewGroup getDeathCalloutContent(Context context,LayoutInflater mLayoutInflater,
                                                   Map<String, Object> attributes) {
        ViewGroup deathCalloutContent;
        switch (Integer.parseInt(attributes.get("layerId").toString())) {
            case R.string.road:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_road, null);
                TextView value1 = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView value2 = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String name_field = context.getResources().getString(R.string.death_road_name_field);
                String class_field = context.getResources().getString(R.string.death_road_class_field);
                Object name = attributes.get(name_field);
                Object type = attributes.get(class_field);
                if(name!=null) value1.setText(name.toString());
                if(type!=null) value2.setText(type.toString());
                break;
            case R.string.hydr_station:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_hydrstation, null);
                TextView hydr_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView hydr_class_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String hydr_name_field = context.getResources().getString(R.string.death_hydrStation_name_field);
                String hydr_class_field = context.getResources().getString(R.string.death_hydrStation_class_field);
                Object hydr_name = attributes.get(hydr_name_field);
                Object hrdr_class = attributes.get(hydr_class_field);
                if(hydr_name!=null) hydr_name_value.setText(hydr_name.toString());
                if(hrdr_class!=null) hydr_class_value.setText(hrdr_class.toString());
                break;
            case R.string.natgas_station:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_natgasstation, null);
                TextView natgas_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView natgas_class_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String natgas_name_field = context.getResources().getString(R.string.death_natgasStation_name_field);
                String natgas_class_field = context.getResources().getString(R.string.death_natgasStation_class_field);
                Object natgas_name = attributes.get(natgas_name_field);
                Object natgas_class = attributes.get(natgas_class_field);
                if(natgas_name!=null) natgas_name_value.setText(natgas_name.toString());
                if(natgas_class!=null) natgas_class_value.setText(natgas_class.toString());
                break;
            case R.string.school:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_school, null);
                TextView school_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView school_note_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String school_name_field = context.getResources().getString(R.string.death_school_name_field);
                String school_note_field = context.getResources().getString(R.string.death_school_note_field);
                Object school_name = attributes.get(school_name_field);
                Object school_note = attributes.get(school_note_field);
                if(school_name!=null) school_name_value.setText(school_name.toString());
                if(school_note!=null) school_note_value.setText(school_note.toString());
                break;
            case R.string.hospital:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_hospital, null);
                TextView hospital_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView hospital_membership_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                TextView hospital_grade_value = (TextView) deathCalloutContent.findViewById(R.id.value3);
                String hospital_name_field = context.getResources().getString(R.string.death_hospital_name_field);
                String hospital_membership_field = context.getResources().getString(R.string.death_hospital_membership_field);
                String hospital_grade_field = context.getResources().getString(R.string.death_hospital_grade_field);
                Object hospital_name = attributes.get(hospital_name_field);
                Object hospital_membership = attributes.get(hospital_membership_field);
                Object hospital_grade = attributes.get(hospital_grade_field);
                if(hospital_name!=null) hospital_name_value.setText(hospital_name.toString());
                if(hospital_membership!=null) hospital_membership_value.setText(hospital_membership.toString());
                if(hospital_grade!=null) hospital_grade_value.setText(hospital_grade.toString());
                break;
            case R.string.city_site:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_citysite, null);
                TextView citySite_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView citySite_class_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String citySite_name_field = context.getResources().getString(R.string.death_citySite_name_field);
                String citySite_class_field = context.getResources().getString(R.string.death_citySite_class_field);
                Object citySite_name = attributes.get(citySite_name_field);
                Object citySite_class = attributes.get(citySite_class_field);
                if(citySite_name!=null) citySite_name_value.setText(citySite_name.toString());
                if(citySite_class!=null) citySite_class_value.setText(citySite_class.toString());
                break;
            case R.string.resettlement_region:
                deathCalloutContent = (ViewGroup) mLayoutInflater.inflate(R.layout.death_callout_content_resettlementregion, null);
                TextView resettlement_name_value = (TextView) deathCalloutContent.findViewById(R.id.value1);
                TextView resettlement_class_value = (TextView) deathCalloutContent.findViewById(R.id.value2);
                String resettlement_name_field = context.getResources().getString(R.string.death_resettlementRegion_name_field);
                String resettlement_class_field = context.getResources().getString(R.string.death_resettlementRegion_class_field);
                Object resettlement_name = attributes.get(resettlement_name_field);
                Object resettlement_class = attributes.get(resettlement_class_field);
                if(resettlement_name!=null) resettlement_name_value.setText(resettlement_name.toString());
                if(resettlement_class!=null) resettlement_class_value.setText(resettlement_class.toString());
                break;
            default:
                return null;
        }

        return deathCalloutContent;
    }
}
