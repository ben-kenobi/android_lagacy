package cn.swsk.rgyxtqapp.bean;

import android.text.TextUtils;

import java.util.Date;



/**
 *盘点详情表。记录每个盘点记录中包含的盘点项目情况。
 *
 * @author 张益雄
 * @version 1.0 创建于2016/2/23
 */

public class TInventoryDetail {

	//主键。UUID。
	private String inventoryDetailId;
	//关联盘点记录ID。外键。
	private TInventory tInventory;
	//装备类别ID。外键。
	private String eqiupCategroyId;
	//装备类别。
	private String eqiupCategroy;	
	//记录在库的数量。
	private String paperQty;
	//盘店后的实际数量。
	private Integer realQty=-1;
	//退回厂家数量。
	private Integer returnQty=-1;
	//状态，默认：0。
	private Integer status;
	//备注。
	private String remark;
	//创建时间。
	private Date createTime = new Date();
	//创建者。
	private String creatorId;
	private int winNum;
	private int lessNum;


	public int getWinNum() {
		return winNum;
	}

	public void setWinNum(int winNum) {
		this.winNum = winNum;
	}

	public int getLessNum() {
		return lessNum;
	}

	public void setLessNum(int lessNum) {
		this.lessNum = lessNum;
	}

	public String getInventoryDetailId() {
		return inventoryDetailId;
	}

	public void setInventoryDetailId(String inventoryDetailId) {
		this.inventoryDetailId = inventoryDetailId;
	}

	public TInventory gettInventory() {
		return tInventory;
	}

	public void settInventory(TInventory tInventory) {
		this.tInventory = tInventory;
	}

	public String getEqiupCategroyId() {
		return eqiupCategroyId;
	}

	public void setEqiupCategroyId(String eqiupCategroyId) {
		this.eqiupCategroyId = eqiupCategroyId;
	}

	public String getEqiupCategroy() {
		return eqiupCategroy;
	}

	public void setEqiupCategroy(String eqiupCategroy) {
		this.eqiupCategroy = eqiupCategroy;
	}

	public String getPaperQty() {
		return paperQty;
	}

	public void setPaperQty(String paperQty) {
		this.paperQty = paperQty;
	}

	public Integer getRealQty() {
		return realQty;
	}

	public void setRealQty(Integer realQty) {
		this.realQty = realQty;
	}

	public Integer getReturnQty() {
		return returnQty;
	}

	public void setReturnQty(Integer returnQty) {
		this.returnQty = returnQty;
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
	public boolean incomplete(){
		return TextUtils.isEmpty(paperQty)||realQty==-1||
				returnQty==-1;
	}
}
