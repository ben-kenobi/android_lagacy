package com.what.weibo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.weibo.sdk.android.api.WeiboAPI;
import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.model.AccountModel;
import com.tencent.weibo.sdk.android.model.BaseVO;
import com.tencent.weibo.sdk.android.model.ModelResult;
import com.tencent.weibo.sdk.android.network.HttpCallback;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;
import com.what.weibo.R;


public class WeiBoRequestMessageActivity extends Activity implements OnClickListener{
	private TextView mShareTv;
    private EditText mShareEt;
    private Button mSharedBtn;
    private TextView mTitle;
    //分享的基础文件
    private String content = "";
    //分享的图片url
    private String picUrl = "";
    //标题
    private String title = "";
    private Handler handler = new Handler(){
    	@Override
    	public void handleMessage(android.os.Message msg) {
    		Toast.makeText(WeiBoRequestMessageActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    		WeiBoRequestMessageActivity.this.finish();
    	};
    };

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_reqmessage);
        initViews();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
        	content = bundle.getString("content");
        	title = bundle.getString("title");
        	picUrl = bundle.getString("picUrl");        	
        }
       //模拟数据
        content = "# 社区便利 # # 福州 # 发现这个团购不错哦！您也来看看吧。【永辉生鲜】10元配送http://v.youku.com/v_show/id_XMTg1NzE1OTUy.html@社区便利";
        picUrl = "http://h.hiphotos.baidu.com/album/w%3D2048/sign=ba4fd5d5d0c8a786be2a4d0e5331c83d/d1160924ab18972b4b5775e5e7cd7b899e510aab.jpg";
	   
        mTitle.setText(title);
        mShareEt.setText(content);

    }
    
	private void initViews() { 		
	    mSharedBtn = (Button) findViewById(R.id.btn_share);
	    mSharedBtn.setOnClickListener(this);
	    mShareTv = (TextView) findViewById(R.id.tv_text_count);
	    mShareEt = (EditText) findViewById(R.id.et_share_msg);  
	    mShareEt.addTextChangedListener(new MyWatcher());
	    mTitle = (TextView) findViewById(R.id.tv_share_title);
	}

	private class MyWatcher implements TextWatcher{

		@Override
		public void afterTextChanged(Editable s) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			int num = 140-s.toString().length();
			mShareTv.setText("您还可以输入"+num+"个字");
			
		}
		
	}
	
	@Override
	public void onClick(View v) {
	    if (R.id.btn_share == v.getId()) { 
	        if("分享到新浪微博".equals(title)){
		        StatusesAPI status = new StatusesAPI(AccessTokenKeeper.readAccessToken(this));
		        status.update(String.valueOf(mShareEt.getText()), "", "", new RequestListener() {
					
					@Override
					public void onIOException(IOException arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onError(WeiboException arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onComplete4binary(ByteArrayOutputStream arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onComplete(String response) {
	//					pd= ProgressDialog.show(MainHandler.this, "下载文件", "正在下载……");
						 
	                    JSONObject obj;
						try {
							obj = new JSONObject(response);
							JSONObject obj_user = new JSONObject(obj.optString("user"));
							AccessTokenKeeper.saveUserName(WeiBoRequestMessageActivity.this,obj_user.optString("name"));
							Log.e("微博用户名为：", "??:"+obj_user.optString("name"));
						} catch (JSONException e) {							
							e.printStackTrace();
						}						
						handler.sendEmptyMessage(0);
						
						
					}
				});
		        
		        //分享图片功能得获得高级接口才能使用
	//	        status.uploadUrlText(String.valueOf(mShareEt.getText()),
	//	        		"http://h.hiphotos.baidu.com/album/w%3D2048/sign=ba4fd5d5d0c8a786be2a4d0e5331c83d/d1160924ab18972b4b5775e5e7cd7b899e510aab.jpg",
	//	        		 "", "", "", new RequestListener() {
	//	     				
	//	     				@Override
	//	     				public void onIOException(IOException arg0) {
	//	     					
	//	     				}
	//	     				
	//	     				@Override
	//	     				public void onError(WeiboException arg0) {
	//	     					
	//	     				}
	//	     				
	//	     				@Override
	//	     				public void onComplete4binary(ByteArrayOutputStream arg0) {
	//	     					
	//	     				}
	//	     				
	//	     				@Override
	//	     				public void onComplete(String arg0) {
	////	     					Toast.makeText(RequestMessageActivity.this, "分享成功", Toast.LENGTH_LONG).show();用handler
	//	     					
	//	     				}
	//	     			});
	        	
	        }else if("分享到腾讯微博".equals(title)){
				reAddWeibo();		
	        }
	        
	    }
	}

	/**
	 * 腾讯发送多类型微博
	 * */
	protected void reAddWeibo(){
		String accessToken = Util.getSharePersistent(getApplicationContext(), "ACCESS_TOKEN");
		AccountModel account = new AccountModel(accessToken);
		WeiboAPI api = new WeiboAPI(account);
		String content = String.valueOf(mShareEt.getText());
		api.reAddWeibo(WeiBoRequestMessageActivity.this,content,picUrl,"","","","", mCallBack, null, BaseVO.TYPE_JSON);
	}
	private HttpCallback mCallBack = new HttpCallback() {
   		@Override
   		public void onResult(Object object) {
   			ModelResult result = (ModelResult) object;
   			if(result.isExpires()){
   				Toast.makeText(WeiBoRequestMessageActivity.this, result.getError_message(), Toast.LENGTH_SHORT).show();
   			}else{
   				if(result.isSuccess()){
   	   				Toast.makeText(WeiBoRequestMessageActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
   	   				WeiBoRequestMessageActivity.this.finish();
   	   			}else{
   	   				Toast.makeText(WeiBoRequestMessageActivity.this, result.getError_message(), Toast.LENGTH_SHORT).show();
   	   				WeiBoRequestMessageActivity.this.finish();
   	   			}
   			}
   		}
	};
    
    @Override
    protected void onDestroy() {
    	
    	super.onDestroy();
    	handler.removeMessages(0);
    	mCallBack = null;
    }

}
