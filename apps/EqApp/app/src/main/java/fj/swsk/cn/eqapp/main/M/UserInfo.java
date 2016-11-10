package fj.swsk.cn.eqapp.main.M;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 16/6/28.
 */
public class UserInfo {
    public String TID,NAME,token;
    public String password,roleId,PHONE,EMAIL,
    CREATE_DATE,SEX,createUserId,upDate,upUserId,delDate,delUserId,remark,
    city,CO1_ID,CO1_NAME,CO2_ID,CO2_NAME,town,DEPART_NAME,DEPART_ID,JOB_POST_ID,JOB_POST_NAME;

    public String getTID() {
        return TID;
    }

    public void setTID(String TID) {
        this.TID = TID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String,String> getInfoMap(){
        Map<String,String> map = new HashMap<>();
        map.put("PHONE",PHONE);
        map.put("EMAIL",EMAIL);
        map.put("CO1_NAME",CO1_NAME);
        map.put("CO2_NAME",CO2_NAME);
        map.put("town",town);
        map.put("DEPART_NAME",DEPART_NAME);
        map.put("JOB_POST_NAME",JOB_POST_NAME);
        return map;

    }

}
