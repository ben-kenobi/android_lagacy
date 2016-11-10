package cn.swsk.rgyxtqapp.bean;

import java.io.Serializable;
import java.util.Date;

public class WareHouseT extends BaseEquip implements Serializable{
	
	//主键
	private String id;
	//库房名称
	private String wareHouseName;
	//火箭弹数量
	private String equipNum="0";
	private int returnNum;
	private int returnNo;
	//创建时间。
	private Date createTime = new Date();
	//状态更新时间
	private Date updateTime;
	private int houseNo=0;
	//使用人
	private String userName;
	//对库状态   分发：1 移库：2 归还：3 调拨：4
	private String status;
	//状态更新的状态    正常发射：5，正常归还：12 故障销毁：7 故障归还：6  无：0
	private String estatus="0";

	//生产厂家名称。
	private String manuFacturer;
	//生产日期。
	private String produceDate;

	public String getManuFacturer() {
		return manuFacturer;
	}

	public void setManuFacturer(String manuFacturer) {
		this.manuFacturer = manuFacturer;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getReturnNo() {
		return returnNo;
	}

	public void setReturnNo(int returnNo) {
		this.returnNo = returnNo;
	}

	public int getDefNum(){
		return Integer.parseInt(equipNum)-returnNum;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public int getReturnNum() {
		return returnNum;
	}


	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public String getEquipNum() {
		return equipNum;
	}





	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}


	public void setEquipNum(String equipNum) {
		this.equipNum = equipNum;
	}

	public void setReturnNum(int returnNum) {
		this.returnNum = returnNum;
	}

	public int getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}

//	@Override
//	public boolean equals(Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//
//		WareHouseT that = (WareHouseT) o;
//
//		return getUniqueCode().equals(that.getUniqueCode());
//
//	}
//
//	@Override
//	public int hashCode() {
//		return getUniqueCode().hashCode();
//	}
}
