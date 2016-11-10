package com.what.yunbao.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.what.yunbao.R;
import com.what.yunbao.collection.CollectionActivity;
import com.what.yunbao.collection.WorkingCollection;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends Activity{
	private TestBusinessAdapter mAdapter;
	private ListView collection_lv;
	private WorkingCollection mWorkingColl;
	private long count = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		collection_lv = (ListView) findViewById(R.id.lv_collection_list);
		
		List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
    	for(int i = 0;i < 5;i++){
    		HashMap<String,String> map = new HashMap<String,String>();
        	map.put("name", "云宝体验便利店"+i);
        	map.put("address", "福建省福州市鼓楼区纱帽井"+i+"号");
        	list.add(map);
    	}
		
		mAdapter = new TestBusinessAdapter(this,list); 
		collection_lv.setAdapter(mAdapter);
		
		collection_lv.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {

				menu.setHeaderTitle("操作");
				menu.add(0, 0, 0, "拨打电话");
				menu.add(0, 1, 0, "加入收藏");
				menu.add(0, 2, 0, "XXXX");
				menu.add(0, 3, 0, "收藏列表");
				 
			}
		});
		
		
		((Button)findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				deleteDirectory("/data/data/com.what.yunbao/databases");
			}
		});
		
		((ImageButton)findViewById(R.id.imageButton1)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		collection_lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.e("----------------------d   ", arg2+"");
				Log.e("----------------------f   ", arg3+"");
				//注意一个是long一个是int 用的时候类型转换
				TextView tv = (TextView) arg1.findViewWithTag(arg2);
				TextView tv_2 = (TextView) arg1.findViewWithTag(arg2+100);
				Toast.makeText(getApplicationContext(), tv.getText()+"///"+tv_2.getText(), 2000).show();
			}
			
		});

	}
	
	
	public boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			Log.e("what","Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				Log.d("what","delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		file.delete();
		return true;
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
//		int i = (Integer) collection_lv.getAdapter().getItem(menuInfo.position);
		switch (item.getItemId()){
		case 1:
			Toast.makeText(this, "收藏", 2000).show();
			mWorkingColl = new WorkingCollection(this);
			if(!mWorkingColl.saveCollection(count ++, "商铺名字"+count, "地址"+count)){
				Toast.makeText(this, "您的收藏夹已存在该商铺", 2000).show();
			};
			break;
		case 3:
			startActivity(new Intent(this,CollectionActivity.class));
			break;
		case 2:
			
			break;
		default :
			Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+"10086")); 
        	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        	Toast.makeText(getApplication(), "工作时间：每天9:00-20:00", 3000).show();
        	startActivity(intent);
			break;
		}
		return super.onContextItemSelected(item);
		
	}
	
}
