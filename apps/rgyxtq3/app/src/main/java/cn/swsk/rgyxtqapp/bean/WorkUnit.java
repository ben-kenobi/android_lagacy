package cn.swsk.rgyxtqapp.bean;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作业单元表，实体类。
 *
 * @author 林久久
 * @version 1.0 创建于2015/11/18
 */
public class WorkUnit implements Serializable {

    // 作业单元主键
    private String wuId;
    // 作业单元编号（类型为固定点时为必填）
    private String wuNo;
    // 作业单元名称
    private String wuName;
    // 作业单元类型（0：流动点；1：固定点）
    private Integer wuType = 0;
    // 车牌号（类型为流动点时为必填）
    private String cardNo;
    // 联系电话
    private String tel;
    // 车载GPS绑定的电话号码
    private String gpsNo;
    // 创建时间
    private Date createTime = new Date();
    // 创建者
    private String creator = "system";

    private String iden = "作业分队";

    public String getIden() {
        return iden;
    }

    public void setIden(String iden) {
        this.iden = iden;
    }

    public String getWuId() {
        return wuId;
    }

    public void setWuId(String wuId) {
        this.wuId = wuId;
    }

    public String getWuNo() {
        return wuNo;
    }

    public void setWuNo(String wuNo) {
        this.wuNo = wuNo;
    }

    public String getWuName() {
        return wuName;
    }

    public void setWuName(String wuName) {
        this.wuName = wuName;
    }

    public Integer getWuType() {
        return wuType;
    }

    public void setWuType(Integer wuType) {
        this.wuType = wuType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGpsNo() {
        return gpsNo;
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo = gpsNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
