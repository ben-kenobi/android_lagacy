package fj.swsk.cn.eqapp.main.M;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 16/6/28.
 */
public class UserInfo {
    private String id,name,token;
    public String password,roleId,phone,email,
    createDate,createUserId,upDate,upUserId,delDate,delUserId,remark,
    city,county,town,department,duty;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String,String> getInfoMap(){
        Map<String,String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("email",email);
        map.put("city",city);
        map.put("county",county);
        map.put("town",town);
        map.put("department",department);
        map.put("duty",duty);
        return map;

    }

}
