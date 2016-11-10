package cn.swsk.rgyxtqapp.bean;

/**
 * Created by tom on 2015/10/18.
 */
public class WorkPlan {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFamc() {
        return famc;
    }

    public void setFamc(String famc) {
        this.famc = famc;
    }

    public String getFazzsj() {
        return fazzsj;
    }

    public void setFazzsj(String fazzsj) {
        this.fazzsj = fazzsj;
    }

    public String getZyqjlx() {
        return zyqjlx;
    }

    public void setZyqjlx(String zyqjlx) {
        this.zyqjlx = zyqjlx;
    }

    public String getHbxz() {
        return hbxz;
    }

    public void setHbxz(String hbxz) {
        this.hbxz = hbxz;
    }

    public String getZjyj() {
        return zjyj;
    }

    public void setZjyj(String zjyj) {
        this.zjyj = zjyj;
    }

    public String getZjfwj() {
        return zjfwj;
    }

    public void setZjfwj(String zjfwj) {
        this.zjfwj = zjfwj;
    }

    public String getAqsj() {
        return aqsj;
    }

    public void setAqsj(String aqsj) {
        this.aqsj = aqsj;
    }

    public String getTqxs() {
        return tqxs;
    }

    public void setTqxs(String tqxs) {
        this.tqxs = tqxs;
    }

    public String getZylx() {
        return zylx;
    }

    public void setZylx(String zylx) {
        this.zylx = zylx;
    }



    @Override
    public String toString() {
        return "WorkPlan{" +
                "famc='" + famc + '\'' +
                ", fazzsj='" + fazzsj + '\'' +
                ", zyqjlx='" + zyqjlx + '\'' +
                ", zylx='" + zylx + '\'' +
                ", hbxz='" + hbxz + '\'' +
                ", zjyj='" + zjyj + '\'' +
                ", zjfwj='" + zjfwj + '\'' +
                ", aqsj='" + aqsj + '\'' +
                ", aqsj='" + id + '\'' +
                ", aqsj='" + status + '\'' +
                ", tqxs='" + tqxs + '\'' +
                '}';
    }
    private String famc;
    private String fazzsj;
    private String  zyqjlx;
    private String zylx;
    private String  hbxz;
    private String zjyj;
    private String zjfwj;
    private String aqsj;
    private String tqxs;
    private String id;
    private String status;
}
