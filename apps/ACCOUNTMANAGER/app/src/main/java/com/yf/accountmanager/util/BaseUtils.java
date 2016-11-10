package com.yf.accountmanager.util;

import java.util.Comparator;
import java.util.List;

public class BaseUtils {
	public static <T> void sort(List<T> list,Comparator<T> c){
		int size=list.size();
		int i,j;
		T t1,t2;
		for( i=1;i<size;i++){
			t1=list.get(i);
			for(j=i-1;j>=0&&c.compare(t2=list.get(j),t1)>0;j--){
				list.set(j+1,t2);
			}
			list.set(j+1,t1);
		}
	}
	
	public static void  useLegacySortJDK(){
		//jdk7.0 may cause IllegalArgumentException:Comparison method violates its general contract
		//add code below  to  avoid this Exception  through  use former jdk version to sort; 
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	}
}
