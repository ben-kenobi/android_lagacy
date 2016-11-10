package cn.swsk.rgyxtqapp.bean;

/**
 * Created by apple on 16/4/25.
 */
public class BaseEquip {
    private int checkstate;
    private String uniqueCode;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCheckstate() {
        return checkstate;
    }

    public void setCheckstate(int checkstate) {
        this.checkstate = checkstate;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ) return false;

        BaseEquip equip = (BaseEquip) o;
//		if(TextUtils.isEmpty(newOld)) {
//
//			if (getOriginalNo() != null ? !getOriginalNo().equals(equip.getOriginalNo()) : equip.getOriginalNo() != null)
//				return false;
//			if (getPackNo() != null ? !getPackNo().equals(equip.getPackNo()) : equip.getPackNo() != null)
//				return false;
//			if (getManuFacturerId() != null ? !getManuFacturerId().equals(equip.getManuFacturerId()) : equip.getManuFacturerId() != null)
//				return false;
//			return !(getBatch() != null ? !getBatch().equals(equip.getBatch()) : equip.getBatch() != null);
//		}else{
//			if (getOrderNo() != null ? !getOrderNo().equals(equip.getOrderNo()) : equip.getOrderNo() != null)
//				return false;
//			if (getProduceDate() != null ? !getProduceDate().equals(equip.getProduceDate()) : equip.getProduceDate() != null)
//				return false;
//			return !(getBatch() != null ? !getBatch().equals(equip.getBatch()) : equip.getBatch() != null);
//		}
        if( (getUniqueCode() != null ? !getUniqueCode().equals(equip.getUniqueCode()) : equip.getUniqueCode() != null)){
            return false;
        }
        return !(getCode() != null ? !getCode().equals(equip.getCode()) : equip.getCode() != null);


    }

    @Override
    public int hashCode() {
        int result=0;
//		if(TextUtils.isEmpty(newOld)) {
//
//			 result = getOriginalNo() != null ? getOriginalNo().hashCode() : 0;
//			result = 31 * result + (getPackNo() != null ? getPackNo().hashCode() : 0);
//			result = 31 * result + (getManuFacturerId() != null ? getManuFacturerId().hashCode() : 0);
//			result = 31 * result + (getBatch() != null ? getBatch().hashCode() : 0);
//		}else{
//			result = getOrderNo() != null ? getOrderNo().hashCode() : 0;
//			result = 31 * result + (getProduceDate() != null ? getProduceDate().hashCode() : 0);
//			result = 31 * result + (getBatch() != null ? getBatch().hashCode() : 0);
//		}
        result = 31 * result + (getUniqueCode() != null ? getUniqueCode().hashCode() : 0);
        result = 31 * result + (getCode() != null ? getCode().hashCode() : 0);

        return result;
    }



}
