package com.icanit.bdmapversion2.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.icanit.bdmapversion2.R;
import com.icanit.bdmapversion2.entity.Business;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class IntentListActivity extends Activity{
	private List<Business> mList=new ArrayList<Business>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		List<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
		
		setContentView(R.layout.intentlist);
		mList=(List<Business>) getIntent().getSerializableExtra("ddd");
		for(int i=0;i<mList.size();i++){
			HashMap<String,Object> map=new HashMap<String,Object>();			
			map.put("bussinessName", mList.get(i).getBusinessName());
			map.put("distance", mList.get(i).getBusinessDistance());
			list.add(map);
		}
		Comparator<HashMap<String,Object>> comparator=new Comparator<HashMap<String,Object>>() {

			@Override
			public int compare(HashMap<String, Object> lhs,
					HashMap<String, Object> rhs) {
				int minus=(int)Double.parseDouble(String.valueOf(lhs.get("distance")))-(int)Double.parseDouble(String.valueOf(rhs.get("distance"))); 
				return minus;
			}
			
		};
		Collections.sort(list, comparator);		
		SimpleAdapter sa=new SimpleAdapter(this, list,R.layout.intentlist_item, new String[]{"bussinessName","distance"}, new int[]{R.id.textView1,R.id.textView2});
		((ListView)findViewById(R.id.lives_bussiness_list)).setAdapter(sa);
	}
}
