package com.icanit.app_v2.util;

import java.lang.reflect.Field;
import java.util.Comparator;

import com.icanit.app_v2.entity.AppMerchant;

public class SortUtil {
	public static class ComparatorParamPair{
		public String fieldName;boolean ascend;
		public ComparatorParamPair(){}
		public ComparatorParamPair(String fieldName,boolean ascend){
			this.fieldName=fieldName;this.ascend=ascend;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (ascend ? 1231 : 1237);
			result = prime * result
					+ ((fieldName == null) ? 0 : fieldName.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ComparatorParamPair other = (ComparatorParamPair) obj;
			if (ascend != other.ascend)
				return false;
			if (fieldName == null) {
				if (other.fieldName != null)
					return false;
			} else if (!fieldName.equals(other.fieldName))
				return false;
			return true;
		}
	}
	
	
	public static class MerchantComparator implements Comparator<AppMerchant> {
		boolean ascend;	Field field;
		public MerchantComparator(String fieldName, boolean ascend) throws NoSuchFieldException {
			this.ascend = ascend;
			setField(fieldName);
		}
		public MerchantComparator(ComparatorParamPair cpp) throws NoSuchFieldException{
			this.ascend=cpp.ascend;setField(cpp.fieldName);
		}
		public MerchantComparator() {
		}
		public void setField(String fieldName) throws NoSuchFieldException{
			this.field=AppMerchant.class.getField(fieldName);
		}
		public void setComparatorParamPair(ComparatorParamPair cpp) throws NoSuchFieldException{
			this.ascend=cpp.ascend;setField(cpp.fieldName);
		}
		
		public void setAscend(boolean b){
			this.ascend=b;
		}
		public int compare(AppMerchant lhs, AppMerchant rhs) {
			if(field==null)return 0;
			int i = 0;
			try {
				Object o1 = field.get(lhs);
				Object o2 = field.get(rhs);
				if (ascend) {
					if (o1.getClass() == Integer.class) {
						return (Integer)o1-(Integer)o2;
					} else if (o1.getClass() == Double.class) {
						return (int)(double)((Double)o1*100-(Double)o2*100);
					} else {
						return o1.toString().compareTo(o2.toString());
					}
				} else {
					if (o1.getClass() == Integer.class) {
						return (Integer)o2-(Integer)o1;
					} else if (o1.getClass() == Double.class) {
						return (int)(double)((Double)o2*100-(Double)o1*100);
					} else {
						return o2.toString().compareTo(o1.toString());
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return i;
		}
	}
}
