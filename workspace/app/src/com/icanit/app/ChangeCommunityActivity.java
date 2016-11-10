package com.icanit.app;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.icanit.app.adapter.CommunityLvAdapter;
import com.icanit.app.common.IConstants;
import com.icanit.app.entity.AppCommunity;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;

public class ChangeCommunityActivity extends Activity {
	private ListView lv_communityList;
	private DataService dataService;
	private ImageButton backButton,locateButton;
	private EditText editText;
	private ImageButton textDisposer;
	private CommunityLvAdapter commAdapter;
	private Handler handler=new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1)
				commAdapter.notifyDataSetChanged();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changecommunity);
		try {
			init();
			initlv();
			customizeBackButton();
			bindListeners();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	private void init() throws AppException {
		lv_communityList=(ListView)findViewById(R.id.listView1);
		backButton=(ImageButton)findViewById(R.id.imageButton1);
		locateButton=(ImageButton)findViewById(R.id.imageButton2);
		dataService=AppUtil.getServiceFactory().getDataServiceInstance(this);
		editText=(EditText)findViewById(R.id.editText1);
		textDisposer=(ImageButton)findViewById(R.id.imageButton3);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void initlv() throws AppException{
		lv_communityList.setAdapter(commAdapter=new CommunityLvAdapter(this));
		DataService.THREAD_POOL.submit(new Runnable(){
			public void run(){
				try {
					List<AppCommunity> communities=dataService.findCommunities();
					commAdapter.setCommunities(communities);
					System.out.println("communities="+communities+   "@changeCommunityActivity");
					handler.sendEmptyMessage(1);
				} catch (AppException e) {
					e.printStackTrace();
				}
			}
		});
		lv_communityList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View self, int position,
					long id) {
				Intent intent=getIntent().setClass(ChangeCommunityActivity.this.getApplicationContext(),HomeActivity.class)
						.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).	setAction(IConstants.AFTER_COMMUNITY_CHANGE);
				ChangeCommunityActivity.this.startActivity(intent);
				AppUtil.getSharedPreferencesUtilInstance().reserveCommunityInfo(commAdapter.getCommunities().get(position));
			}
		});
	}
	private void customizeBackButton(){
		AppUtil.bindBackListener(backButton);
	}
	private void bindListeners(){
		AppUtil.bindEditTextNtextDisposer(editText, textDisposer);
	}

}
