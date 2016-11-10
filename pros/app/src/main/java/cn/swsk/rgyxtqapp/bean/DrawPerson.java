package cn.swsk.rgyxtqapp.bean;

/**
 * Created by apple on 16/2/23.
 */
public class DrawPerson {
    private String name;
    private String identity;
    private String phone;

    public static DrawPerson getDefault(){
        DrawPerson p = new DrawPerson();
        p.name="姓名";
        p.identity="身份";
        p.phone="电话";
        return p;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
