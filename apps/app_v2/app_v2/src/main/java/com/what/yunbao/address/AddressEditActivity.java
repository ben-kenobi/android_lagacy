package com.what.yunbao.address;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.util.CommonUtil;

public class AddressEditActivity extends Activity{
	
	private static final String TAG = "AddressEditActivity";
	private EditText name_et;
	private EditText phone_et;
	private EditText address_et;
	private WorkingAddress mWorkingAddr;
	private Button confirm_btn;
	private Button cancle_btn;
	private boolean flag = false;
	private boolean flag_submit1 = false;
	private boolean flag_submit2 = false;
	private boolean flag_submit3 = false;
	
	private TextView name_tv;
	private TextView phone_tv;
	private TextView address_tv;
	private LinearLayout name_ll;
	private LinearLayout phone_ll;
	private LinearLayout address_ll;
	
	private Toast mToast;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Log.e(TAG,"oncreat..........");
		setContentView(R.layout.address_add);
		mToast = Toast.makeText(this, null, Toast.LENGTH_SHORT);
		Intent intent;
		System.out.println(savedInstanceState+"  @aaaa");
		if(savedInstanceState!=null)
			intent=savedInstanceState.getParcelable("intent");
		else
			intent=getIntent();
		initActivityState(intent);
		setUpViews();
		textChange();
		MyOnclickListener listener;
		confirm_btn.setOnClickListener(listener=new MyOnclickListener());
		cancle_btn.setOnClickListener(listener);
	}
	/**
	 * 选择编辑还是新建address
	 * @param intent
	 * @return
	 */
	private boolean initActivityState(Intent intent) {
        mWorkingAddr = null;
        if (TextUtils.equals(Intent.ACTION_VIEW, intent.getAction())) {
            long addrId = intent.getLongExtra(Intent.EXTRA_UID, 0);
            mWorkingAddr = WorkingAddress.load(this, addrId);
            if (mWorkingAddr == null) {
                finish();
                return false;
            }
        } 
        else if(TextUtils.equals(Intent.ACTION_INSERT_OR_EDIT, intent.getAction())) {
        	mWorkingAddr = WorkingAddress.createEmptyNote(this);         
        } else {
            finish();
            return false;
        }
        return true;
    }
	
	//销毁时保存worthsaving 的数据
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (flag && !mWorkingAddr.existInDatabase()) {
            try {
				saveNote();
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
        outState.putParcelable("intent", getIntent());
    }

	private void setUpViews(){
		name_et = (EditText) findViewById(R.id.et_address_name);
		phone_et = (EditText) findViewById(R.id.et_address_phone);
		address_et = (EditText) findViewById(R.id.et_address_addr);
		confirm_btn = (Button) findViewById(R.id.btn_address_positive);
		cancle_btn = (Button) findViewById(R.id.btn_address_negative);
		
		name_tv = (TextView) findViewById(R.id.tv_address_name_info);
		phone_tv = (TextView) findViewById(R.id.tv_address_phone_info);
		address_tv = (TextView) findViewById(R.id.tv_address_addr_info);
		
		name_ll = (LinearLayout) findViewById(R.id.ll_address_name_info);
		phone_ll = (LinearLayout) findViewById(R.id.ll_address_phone_info);
		address_ll = (LinearLayout) findViewById(R.id.ll_address_addr_info);
		
	}
	
	private void textChange(){
		name_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Log.e(TAG, "name_tv  textchange");
				name_ll.setVisibility(0);
				if(s.toString().trim().length()==0){
					name_tv.setText("抱歉！名字不能为空");
					flag_submit1 = false;
					return;
				}else if(s.toString().trim().length()==1){
					name_tv.setText("抱歉！名字不便太短");
					flag_submit1 = false;
					return;
				}

				if(s.toString().trim().length()>1){
					flag_submit1 = true;
					name_ll.setVisibility(8);
				}
				
			}
		});
		phone_et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Pattern p = Pattern.compile("^((((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8})|(((010)|(021)|(022)|(023)|(0\\d{3}))-\\d{7,8}))$"); 
				phone_ll.setVisibility(0);
				
				if(s.toString().trim().length()==0){
					phone_tv.setText("抱歉！电话不能为空");
					flag_submit2 = false;
					return;
				}else if(p.matcher(s.toString().trim()).matches()==false){	
					phone_tv.setText("抱歉！电话需正确号码段11位数字或者区号-号码");
					flag_submit2 = false;
					return;
				}
				if(s.toString().trim().length()>10 && s.toString().trim().length()<14){
					flag_submit2 = true;
					phone_ll.setVisibility(8);					
				}
			}
		});
		address_et.addTextChangedListener(new TextWatcher() { 
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				Log.e(TAG, "address_tv  textchange");
				
				address_ll.setVisibility(0);
				Log.e(TAG, "LENGTH  "+s.toString().trim().length());
				if(s.toString().trim().length()==0){
					address_tv.setText("抱歉！地址不能为空"); 
					flag_submit3 = false;
					return;
				}else if(s.toString().trim().length()<=4){
					address_tv.setText("抱歉！地址不便太短");
					flag_submit3 = false;
					return;
				}

				if(s.toString().trim().length()>=5){
					flag_submit3 = true;
					address_ll.setVisibility(8);
				}		
			}						
		});
	}
	
	private void initAddrScreen() {
      	name_et.setText(mWorkingAddr.getContent());
        name_et.setSelection(name_et.getText().length());
        phone_et.setText(mWorkingAddr.getPhone());
        address_et.setText(mWorkingAddr.getmAddress());
   }
	
	//当launchMode 为singleTop或者singleTask时调用newIntent和onresume
//	@Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        initActivityState(intent);
//    }

	
	@Override
    protected void onResume() {
        super.onResume();
        initAddrScreen();
    }

    
    

    private boolean saveNote() throws Exception {
    	if(!(flag_submit1 && flag_submit2  && flag_submit3)){   		
    		return false;
    	}
    	String [] text = new String[] {name_et.getText().toString(),phone_et.getText().toString(),address_et.getText().toString()};
        boolean saved = mWorkingAddr.saveAddress(text);
        if (saved) {
            setResult(RESULT_OK);
        }
        return saved;
    }
    
    private class MyOnclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_address_positive) {
				if(!(flag_submit1 && flag_submit2 && flag_submit3)){
					CommonUtil.showToast("对不起，您的信息有误,请重新输入", mToast);
		    	}else{
					flag = true;
					try {
						if(saveNote())
							AddressEditActivity.this.finish();
						else CommonUtil.showToast("更新失败", mToast);
					} catch (Exception e) {
						e.printStackTrace();
						CommonUtil.showToast("更新异常", mToast);
					}
					
		    	}
			} else if (v.getId() == R.id.btn_address_negative) {
				flag = false;
				AddressEditActivity.this.finish();
			} 
			
		}	
    }
    
}
