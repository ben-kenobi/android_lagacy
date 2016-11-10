package com.what.yunbao.test;

import com.what.yunbao.R;
import com.what.yunbao.collection.WorkingCollection;
import com.what.yunbao.person.PersonalCenterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TT extends Activity{
	private static final String TAG = "TT";
	private TextView tv;
	private boolean flag;
	private CheckBox cb;
	private long id;
	private WorkingCollection mCollection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "进入商铺详情..................");
		setContentView(R.layout.test2);
		tv = (TextView) findViewById(R.id.textView1);
		if(TextUtils.equals(Intent.ACTION_VIEW, getIntent().getAction())){
			flag = false;
			id = getIntent().getLongExtra(Intent.EXTRA_UID, 0);
			Toast.makeText(this, "商家序号是 "+id, 2000).show();
			tv.setText("您进入的商家编号是：" + id);
		}	
		if(TextUtils.equals(Intent.ACTION_NEW_OUTGOING_CALL, getIntent().getAction())){
			flag = true;
			id = getIntent().getLongExtra(Intent.EXTRA_UID, 0);
			Toast.makeText(this, "商家序号是 "+id, 2000).show();
			tv.setText("您进入的商家编号是：" + id);
		}
		mCollection = new WorkingCollection(getApplicationContext());
		cb = (CheckBox) findViewById(android.R.id.checkbox);
		if(mCollection.beCollected(id)){
			cb.setChecked(true);
		}
		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(cb.isChecked()){
					mCollection.saveCollection(id, "收藏商家"+id, "收藏商家地址"+id);
				}else{
					mCollection.removeCollection(id);
				}
				
			}
		});
		
	}
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			if(flag){
				startActivity(new Intent(this,PersonalCenterActivity.class));
				Toast.makeText(this, "引导进入主界面", 2000).show();
				return true;
			}
			return super.dispatchKeyEvent(event);
		default:
    		return super.dispatchKeyEvent(event);
		}
	}
	
	
}
