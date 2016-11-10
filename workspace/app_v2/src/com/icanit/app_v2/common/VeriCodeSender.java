package com.icanit.app_v2.common;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.widget.TextView;

import com.icanit.app_v2.util.AppUtil;

public class VeriCodeSender implements Runnable {
	private TextView tv;
	private String url, message, veriCode;
	private VeriCodeTimer timer;
	private int seconds = 60;

	public VeriCodeSender(String url, String veriCode,
			TextView tv) {
		this.tv = tv;
		this.url = url;
		this.veriCode = veriCode;
	}
	public void setMessage(String message){
		this.message=message;
	}

	@Override
	public void run() {
		IConstants.MAIN_HANDLER.post(new Runnable(){
			public void run(){
				tv.setEnabled(false);
				tv.setText("��ȡ��...");
				tv.setTextColor(Color.rgb(0x99, 0x99, 0x99));
			}
		});
		JSONObject veriCodeResp = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(url, message);
			if (jsonResp != null && jsonResp.startsWith("{"))
				veriCodeResp = new JSONObject(jsonResp);

		} catch (Exception e) {
			e.printStackTrace();
		}
		final JSONObject jo = veriCodeResp;
		IConstants.MAIN_HANDLER.post(new Runnable() {
			public void run() {
				try {
					if (jo != null
							&& jo.getBoolean(IConstants.RESPONSE_SUCCESS)) {
						AppUtil.toast("��֤��:" + jo.getString(veriCode));
						startTimer();
					} else {
						String respDesc=null;
						try{
							respDesc=jo.getString(IConstants.RESPONSE_DESC);
						}catch (Exception e) {}
						if(respDesc==null||"".equals(respDesc)||"null".equals(respDesc))
							respDesc="���ŷ���ʧ��,�Ժ�����";
						AppUtil.toast(respDesc);
						tv.setEnabled(true);
						tv.setTextColor(Color.rgb(0x33, 0x33, 0x33));
						tv.setText("��ȡ��֤��");
					}
				} catch (JSONException e) {
					e.printStackTrace();
					AppUtil.toast("���������쳣,�Ժ�����");
					tv.setEnabled(true);
					tv.setTextColor(Color.rgb(0x33, 0x33, 0x33));
					tv.setText("��ȡ��֤��");
				}
			}
		});

	}

	private void startTimer() {
		if (timer == null) {
			timer = new VeriCodeTimer(tv);
		}
		timer.start(seconds);
	}
	

}
