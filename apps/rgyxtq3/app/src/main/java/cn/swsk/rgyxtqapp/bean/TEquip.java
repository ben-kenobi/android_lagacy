package cn.swsk.rgyxtqapp.bean;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *装备表。记录安全装备信息。
 *
 * @author 张益雄
 * @version 1.0 创建于2016/2/22
 */
public class TEquip extends BaseEquip implements Serializable{
	public static final Map<String,String> cateMap = new HashMap<>();
	static {

		cateMap.put("0001","测试类型");
		cateMap.put("1","BL-1A");
		cateMap.put("32","甲增雨防雹");
		cateMap.put("33","BL-1A");
		cateMap.put("34","增雨弹");
		cateMap.put("35","增雨弹");

		cateMap.put("001","高炮炮弹");
		cateMap.put("002","火箭弹");
		cateMap.put("003","焰条");
		cateMap.put("004","焰弹");
		cateMap.put("000","其他");


		cateMap.put("01","高炮");
		cateMap.put("02","火箭发射架");
		cateMap.put("03","焰条播撒器");
		cateMap.put("04","焰弹发射器");
		cateMap.put("05","碘化银-丙酮溶液播撒器");
		cateMap.put("06","液态二氧化碳播撒器");
		cateMap.put("07","液氮播撒器");
		cateMap.put("08","吸湿性粗粒粉剂播撒装置");
		cateMap.put("00","其他");


	}



	//主键。UUID。
	private String equipId;
	//弹ID
	private String equipIds;
	//装箱编号
	private String packNo;
	//是否补码
	private String complement;
	//11.完整二维码
	//箱子类型ID
	private String packTypeId;
	//箱子装弹量
	private String packNum;
	//装备编号。自定义流水号。
	private String equipNo;
	//原始编号。即厂家自己的出厂编号。弹编号
	private String originalNo;
	//所在库房
	private TWareHouse tWareHouse;
	//所在库房ID
	//private String warehouseId;
	//所在库房名称。
	private String wareHouse;
	//装备名称。
	private String equipName;
	//装备类别
	private Map<String,Object> tequipCategroy;
	//装备类别ID
	private String equipCategroyId;
	//类别名称。
	private String equipCategroy;
	//装备型号编号。外键。
	private String modelId;
	//装备型号名称。
	private String model;
	//生产厂家编号。外键。
	private String manuFacturerId;
	//生产厂家名称。
	private String manuFacturer;
	//目标库
	private String targetWareHouse;
	//生产批次。
	private String batch;
	//拼音检索。
	private String pinYin;
	//生产日期。
	private String produceDate;
	//过期时间
	private String outDate;
	//有效期。单位：天。
	private Integer expiryDate;
	//到期时间。
	private String dueDate;
	//在库状态
	private String estatus;
	//状态。默认：0。（-1：删除）
	private Integer status;
	//备注。
	private String remark;
	//区分新旧二维码
	private String newOld;
	//分类码
	private String equipType;
	//使用方式
	private String userWay;
	//催化剂种类--装备样式
	private String equipStyle;
	//顺序号
	private String orderNo;
	//校验码
	private String checkNo;
	//创建时间。
	private Date createTime = new Date();
	//创建者。
	private String creatorId = "sys";
	//出入库明细
	private List tequipAttr;
	//出入库明细表
	private List tInoutDetail;
	//装备详细
	private List tProblemDetail;




	private String equipNum;

//	private String produceDate;



	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public String getEquipIds() {
		return equipIds;
	}

	public void setEquipIds(String equipIds) {
		this.equipIds = equipIds;
	}

	public String getPackNo() {
		return packNo;
	}

	public void setPackNo(String packNo) {
		this.packNo = packNo;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}


	public String getPackTypeId() {
		return packTypeId;
	}

	public void setPackTypeId(String packTypeId) {
		this.packTypeId = packTypeId;
	}

	public String getPackNum() {
		return packNum;
	}

	public void setPackNum(String packNum) {
		this.packNum = packNum;
	}

	public String getEquipNo() {
		return equipNo;
	}

	public void setEquipNo(String equipNo) {
		this.equipNo = equipNo;
	}

	public String getOriginalNo() {
		if(TextUtils.isEmpty(originalNo)){
			return produceDate+batch+orderNo;
		}
		return originalNo;
	}

	public void setOriginalNo(String originalNo) {
		this.originalNo = originalNo;
	}

	public String getWareHouse() {
		return wareHouse;
	}

	public void setWareHouse(String wareHouse) {
		this.wareHouse = wareHouse;
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getEquipCategroyId() {
		return equipCategroyId;
	}

	public void setEquipCategroyId(String equipCategroyId) {
		this.equipCategroyId = equipCategroyId;
	}

	public String getEquipCategroy() {
		return cateMap.get(getEquipCategroyId());
	}

	public void setEquipCategroy(String equipCategroy) {
		this.equipCategroy = equipCategroy;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManuFacturerId() {
		return manuFacturerId;
	}

	public void setManuFacturerId(String manuFacturerId) {
		this.manuFacturerId = manuFacturerId;
	}

	public String getManuFacturer() {
		return manuFacturer;
	}

	public void setManuFacturer(String manuFacturer) {
		this.manuFacturer = manuFacturer;
	}

	public String getTargetWareHouse() {
		return targetWareHouse;
	}

	public void setTargetWareHouse(String targetWareHouse) {
		this.targetWareHouse = targetWareHouse;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}

	public String getProduceDate() {
		return produceDate;
	}

	public void setProduceDate(String produceDate) {
		this.produceDate = produceDate;
	}

	public Integer getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Integer expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
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



	public Object gettWareHouse() {
		return tWareHouse;
	}

//	public String getWarehouseId() {
//		return warehouseId;
//	}
//
//	public void setWarehouseId(String warehouseId) {
//		this.warehouseId = warehouseId;
//	}


	public List getTequipAttr() {
		return tequipAttr;
	}

	public void setTequipAttr(List tequipAttr) {
		this.tequipAttr = tequipAttr;
	}

	public List gettInoutDetail() {
		return tInoutDetail;
	}

	public void settInoutDetail(List tInoutDetail) {
		this.tInoutDetail = tInoutDetail;
	}

	public List gettProblemDetail() {
		return tProblemDetail;
	}

	public void settProblemDetail(List tProblemDetail) {
		this.tProblemDetail = tProblemDetail;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}


	public static Map<String, String> getCateMap() {
		return cateMap;
	}

	public void settWareHouse(TWareHouse tWareHouse) {
		this.tWareHouse = tWareHouse;
	}

	public Map<String, Object> getTequipCategroy() {
		return tequipCategroy;
	}

	public void setTequipCategroy(Map<String, Object> tequipCategroy) {
		this.tequipCategroy = tequipCategroy;
	}

	public String getNewOld() {
		return newOld;
	}

	public void setNewOld(String newOld) {
		this.newOld = newOld;
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public String getUserWay() {
		return userWay;
	}

	public void setUserWay(String userWay) {
		this.userWay = userWay;
	}

	public String getEquipStyle() {
		return equipStyle;
	}

	public void setEquipStyle(String equipStyle) {
		this.equipStyle = equipStyle;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public static  TEquip defaul(String id){

		TEquip equi=new TEquip();
		equi.setEquipId(id);

		equi.setOriginalNo("1238917");
		equi.setPackNo("1111");
		equi.setManuFacturerId(id);
		equi.setEquipCategroy("leibie");
		equi.setBatch("11");
		equi.setProduceDate("2012-01-01");
		equi.setManuFacturer("zhongtian");
		return equi;
	}

	public String getEquipNum() {
		return equipNum;
	}

	public void setEquipNum(String equipNum) {
		this.equipNum = equipNum;
	}



	
}
