package fj.swsk.cn.eqapp.util;

import android.content.Intent;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BaseUtils {

	public static <T>T d2t(Class<T> clz,Map<String,Object> map) throws Exception {
		T t = clz.newInstance();

		for(String key:map.keySet()){
			try {
				Method m = clz.getMethod(setterName(key), map.get(key).getClass());
				m.invoke(t,map.get(key));
			}catch(Exception e){
				fj.swsk.cn.eqapp.util.CommonUtils.log(e.toString());
			}
		}
		return t;
	}
	public static String setterName(String key){
		if(!key.startsWith("set")){
			char ary[]=key.toCharArray();
			if(ary[0]>='a'&&ary[0]<='z'){
				ary[0]-='a'-'A';
			}
			key=String.valueOf(ary);
		}
		return key;
	}


	public static Intent titleIntent(String title){
		Intent intent = new Intent();
		intent.putExtra("title",title);
		return intent;
	}
	public static String intenTitle(Intent intent){
		return intent.getStringExtra("title");
	}




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
	public static boolean isMobileNo(String no){
		String telRegex = "[1][358]\\d{9}";
		if(TextUtils.isEmpty(no)) return false;
		else return no.matches(telRegex);
	}
}
