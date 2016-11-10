package cn.swsk.rgyxtqapp.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class TInventory {

	//主键。UUID。
	private String inventoryId;
	//盘点主题。
	private String title;
	//仓库名称。
	private String wareHouse;
	//盘点的仓库ID。
	private TWareHouse tWareHouse;
	//盘点日期。
	private String regDate;
	//盘点人ID。
	private String operatorId;
	//盘点人姓名。
	private String operator;
	//状态，默认：0。
	private Integer status;
	//备注。
	private String remark;
	//创建时间。
	private Date createTime = new Date();
	//创建者。
	private String creatorId;
	private String region;

	//盘点详情表
	private List<TInventoryDetail> tInventoryDetail=new ArrayList<>();

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public TWareHouse gettWareHouse() {
		return tWareHouse;
	}

	public void settWareHouse(TWareHouse tWareHouse) {
		this.tWareHouse = tWareHouse;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public List<TInventoryDetail> gettInventoryDetail() {
		return tInventoryDetail;
	}

	public void settInventoryDetail(List<TInventoryDetail> tInventoryDetail) {
		this.tInventoryDetail = tInventoryDetail;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}
	public boolean incomplete(){
		return TextUtils.isEmpty(title)||
				TextUtils.isEmpty(wareHouse)||
				TextUtils.isEmpty(region)||
				TextUtils.isEmpty(operator)||
				TextUtils.isEmpty(creatorId)||
				tInventoryDetail==null||
				tInventoryDetail.size()==0||
				tInventoryDetail.get(0).incomplete();

	}
}
