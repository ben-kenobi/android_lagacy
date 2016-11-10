package fj.swsk.cn.eqapp.main.M;

import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/6/20.
 */
public class PopInfos {

    public static final  String[]	pickerOptions=new String[]{"待提交数据","历史采集数据"
            ,"历史地震","应急预案","灾情简报","辅助决策报告","系统设置"};
    public static final  int[]	pickerOptionIcons=new int[]{R.mipmap.forsubmit,
            R.mipmap.datas,R.mipmap.eathquakehis,R.mipmap.prescription,R.mipmap.evaluate,R.mipmap.dicision,
            R.mipmap.settings};






    public static final  String[]	distributions=new String[]{"烈度","房屋倒塌分布图","人员死亡分布图"
            ,"人员受伤分布图","经济损失分布图"};
    public static final  int[]	distributionIcons=new int[]{R.mipmap.ic_density,R.mipmap.ic_housedamage,
            R.mipmap.ic_mendown,R.mipmap.ic_meninjury,R.mipmap.ic_economiclost};







//    public static final  String[]	controlflows=new String[]{"交通道路"
//            ,"水电站","燃气站","学校","医院","疏散场地","安置区域"};
    public static final  String[]	controlflows=new String[]{
            "学校","医院","疏散场地"};
    public static final  String[]	controlflows2=new String[]{
            "水电站","燃气站","学校","医院","疏散场地","安置区域","全部类型"};
    public static final  int[]	controlflowsIcons=new int[]{R.drawable.selector_school,R.drawable.selector_hospital,
            R.drawable.selector_evacuation,R.mipmap.town,R.mipmap.town,R.mipmap.town,R.mipmap.town};
    
}
