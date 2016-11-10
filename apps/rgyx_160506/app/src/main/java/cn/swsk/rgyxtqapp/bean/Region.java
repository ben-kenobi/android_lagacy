package cn.swsk.rgyxtqapp.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 区域表，实体类。
 *
 * @author 林久久
 * @version 1.0 创建于2015/11/18
 */
public class Region implements Serializable {

    // 区域主键
    private String regionId;
    // 区域名称
    private String regionName;
    // 层级
    private Integer lv = 0;
    // 经度
    private String lon;
    // 纬度
    private String lat;
    // 缩放级别
    private Integer zoom;
    // 创建时间
    private Date createTime = new Date();
    // 创建者
    private String creator = "system";
    // 父级ID
    private String parentId;
    // zTree用
    private Boolean open;
    // zTree用
    private Boolean isRoot;
    // 父级区域
    private Region parent;
    // 拥有的子级区域


    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public Integer getLv() {
        return lv;
    }

    public void setLv(Integer lv) {
        this.lv = lv;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public Integer getZoom() {
        return zoom;
    }

    public void setZoom(Integer zoom) {
        this.zoom = zoom;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Boolean getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Boolean isRoot) {
        this.isRoot = isRoot;
    }

    public Region getParent() {
        return parent;
    }

    public void setParent(Region parent) {
        this.parent = parent;
    }
}
