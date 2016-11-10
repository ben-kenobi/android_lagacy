package cn.swsk.rgyxtqapp.bean;

import java.util.List;

import cn.swsk.rgyxtqapp.utils.IConstants;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/11/2.
 */
public class TCodeCount {

    public String id;

    public String userName;

    public String code="";

    public String time;
    //手动0 自动1
    public String type;
    //保存类型
    public String saveType;
    //所属市
    public String regionci;
    //所属县
    public String regionco;
    public  TCodeCount(int type,String saveType,List<TEquip> equips){
        super();
        userName= PushUtils.getUserName();
        time= IConstants.sdfTime.format(new java.util.Date());
        this.type = type+"";
        this.saveType=saveType;
        TWareHouse wh=PushUtils.sWareHouse;
        if(wh!=null){
            this.regionci=wh.getBelongRegionci();
            this.regionco=wh.getBelongRegioncoci();
        }
        for (TEquip equip:equips){
            if(equip.getCheckstate()==1){
                if (code.length() > 0) {
                    code += ","+equip.getCode();
                }else{
                    code += equip.getCode();
                }
            }
        }

    }

}
