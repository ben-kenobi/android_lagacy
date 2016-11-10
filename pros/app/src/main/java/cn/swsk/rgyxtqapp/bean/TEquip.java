package cn.swsk.rgyxtqapp.bean;



import java.util.Date;



/**
 *装备表。记录安全装备信息。
 *
 * @author 张益雄
 * @version 1.0 创建于2016/2/22
 */

public class TEquip {

	//主键。UUID。
	private String id;
	//装备编号。自定义流水号。
	private String no;
	//原始编号。即厂家自己的出厂编号。
	private String originalNo;
	//所在库房编号。外键。
	private String wareHouseId;
	private String boxno;
	//所在库房名称。
	private String wareHouse;
	//装备名称。
	private String equipName;
	//类别ID。外键。
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
	//生产批次。
	private String batch;
	//拼音检索。
	private String pinYin;
	//生产日期。
	private String produceDate;
	//有效期。单位：天。
	private Integer expiryDate;
	//到期时间。
	private String dueDate;
	//状态。默认：0。（-1：删除）
	private Integer status;
	//备注。
	private String remark;
	//创建时间。
	private Date createTime = new Date();
	//创建者。
	private String creatorId;

	private int checkstate;

	public static  TEquip defaul(String id){

		TEquip equi=new TEquip();
		equi.setId(id);
		equi.setOriginalNo("1238917");
		equi.setBoxno("1111");
		equi.setManuFacturerId(id);
		equi.setEquipCategroy("leibie");
		equi.setBatch("11");
		equi.setProduceDate("2012-01-01");
		equi.setManuFacturer("zhongtian");
		return equi;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getOriginalNo() {
		return originalNo;
	}

	public void setOriginalNo(String originalNo) {
		this.originalNo = originalNo;
	}

	public String getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(String wareHouseId) {
		this.wareHouseId = wareHouseId;
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
		return equipCategroy;
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

	public int getCheckstate() {
		return checkstate;
	}

	public void setCheckstate(int checkstate) {
		this.checkstate = checkstate;
	}

	public String getBoxno() {
		return boxno;
	}

	public void setBoxno(String boxno) {
		this.boxno = boxno;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TEquip equip = (TEquip) o;

		if (getOriginalNo() != null ? !getOriginalNo().equals(equip.getOriginalNo()) : equip.getOriginalNo() != null)
			return false;
		if (getBoxno() != null ? !getBoxno().equals(equip.getBoxno()) : equip.getBoxno() != null)
			return false;
		if (getManuFacturerId() != null ? !getManuFacturerId().equals(equip.getManuFacturerId()) : equip.getManuFacturerId() != null)
			return false;
		return !(getBatch() != null ? !getBatch().equals(equip.getBatch()) : equip.getBatch() != null);

	}

	@Override
	public int hashCode() {
		int result = getOriginalNo() != null ? getOriginalNo().hashCode() : 0;
		result = 31 * result + (getBoxno() != null ? getBoxno().hashCode() : 0);
		result = 31 * result + (getManuFacturerId() != null ? getManuFacturerId().hashCode() : 0);
		result = 31 * result + (getBatch() != null ? getBatch().hashCode() : 0);
		return result;
	}
}
