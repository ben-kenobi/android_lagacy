package cn.swsk.rgyxtqapp.bean;

import java.io.Serializable;
import java.util.Date;

/**
 *仓库表。
 *
 * @author 张益雄
 * @version 1.0 创建于2016/2/23
 */
public class TWareHouse implements Serializable{
	
	
	
	//主键。UUID。
	private String wareHouseId;
	//仓库名称。
	private String wareHouseName;
	//所属区域ID。
	private String belongRegionId;
	//所属区域。
	private String belongRegion;
	//仓库编号
	private String warehouseNo;
	//现有库存
	private String stock;
	//联系人
	private String linkman;
	//联系电话
	private String tel;
	//登入名
	private String loginName;
	//级别 （0：省，1：市，2：县）
	private String LV;
	//所属县市
	private String belongRegioncoci;
	//地址
	private String address;
	//经度
	private Integer lon;
	//纬度
	private Integer lat;
	//最大库容
	private String maxStock;
	//低库存容量预警阈值
	private String minStock;
	//防爆仓库（0：是，-1：否）
	private String antiExplosion;
	//安全保管箱(0：是，-1：否)
	private String safeBox;
	//父级ID
	private String parentId;
	//隶属区域
	private String belongRegionci;
	//登录密码
	private String pwd;
	//状态。默认：0。
	private Integer status;
	//备注。
	private String remark;
	//创建时间。
	private Date createTime = new Date();
	//创建者。
	private String creatorId;
	//装备表


	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public String getBelongRegionId() {
		return belongRegionId;
	}

	public void setBelongRegionId(String belongRegionId) {
		this.belongRegionId = belongRegionId;
	}

	public String getBelongRegion() {
		return belongRegion;
	}

	public void setBelongRegion(String belongRegion) {
		this.belongRegion = belongRegion;
	}

	public String getWarehouseNo() {
		return warehouseNo;
	}

	public void setWarehouseNo(String warehouseNo) {
		this.warehouseNo = warehouseNo;
	}

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLV() {
		return LV;
	}

	public void setLV(String LV) {
		this.LV = LV;
	}

	public String getBelongRegioncoci() {
		return belongRegioncoci;
	}

	public void setBelongRegioncoci(String belongRegioncoci) {
		this.belongRegioncoci = belongRegioncoci;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getLon() {
		return lon;
	}

	public void setLon(Integer lon) {
		this.lon = lon;
	}

	public Integer getLat() {
		return lat;
	}

	public void setLat(Integer lat) {
		this.lat = lat;
	}

	public String getMaxStock() {
		return maxStock;
	}

	public void setMaxStock(String maxStock) {
		this.maxStock = maxStock;
	}

	public String getMinStock() {
		return minStock;
	}

	public void setMinStock(String minStock) {
		this.minStock = minStock;
	}

	public String getAntiExplosion() {
		return antiExplosion;
	}

	public void setAntiExplosion(String antiExplosion) {
		this.antiExplosion = antiExplosion;
	}

	public String getSafeBox() {
		return safeBox;
	}

	public void setSafeBox(String safeBox) {
		this.safeBox = safeBox;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getBelongRegionci() {
		return belongRegionci;
	}

	public void setBelongRegionci(String belongRegionci) {
		this.belongRegionci = belongRegionci;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
}
