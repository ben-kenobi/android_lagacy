package com.icanit.app_v2.fragment;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.activity.PayIndexActivity;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.common.VeriCodeSender;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.what.yunbao.setting.SettingAccManaChangeTelActivity;

public class PayHomeFragment extends Fragment implements OnClickListener {

	private View self;

	private int resId = R.layout.fragment4pay_curaccount_pay;

	private Button toggleTradeInfo, getVeriCode, submit;

	private TextView merName, boundToPay, orderNum, tradeTime, phone,
			changePhone;

	private TableRow tr0, tr1;

	private EditText veriCode;

	private AppOrder order;
	
	private VeriCodeSender sender;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		bindListeners();
		fillTradeInfo();
	}

	public android.view.View onCreateView(android.view.LayoutInflater inflater,
			android.view.ViewGroup container, Bundle savedInstanceState) {
		ViewParent pv = self.getParent();
		if (pv != null)
			((ViewGroup) pv).removeAllViews();
		return self;
	};

	private void fillTradeInfo() {
		merName.setText("icanit");
		boundToPay.setText(String.format("%.2f", order.sum) + " 元");
		orderNum.setText(order.orderNumber);
		tradeTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA).format(order.orderTime));
		phone.setText("手机号码：" + AppUtil.formatPhoneNum(order.userPhone));
	}

	private void init() {
		self = LayoutInflater.from(getActivity()).inflate(resId, null, false);
		toggleTradeInfo = (Button) self.findViewById(R.id.button1);
		getVeriCode = (Button) self.findViewById(R.id.button2);
		submit = (Button) self.findViewById(R.id.button3);
		merName = (TextView) self.findViewById(R.id.textView1);
		boundToPay = (TextView) self.findViewById(R.id.textView2);
		orderNum = (TextView) self.findViewById(R.id.textView3);
		tradeTime = (TextView) self.findViewById(R.id.textView4);
		phone = (TextView) self.findViewById(R.id.textView5);
		changePhone = (TextView) self.findViewById(R.id.textView6);
		tr0 = (TableRow) self.findViewById(R.id.tableRow1);
		tr1 = (TableRow) self.findViewById(R.id.tableRow2);
		veriCode = (EditText) self.findViewById(R.id.editText1);
		order = (AppOrder) getActivity().getIntent().getSerializableExtra(IConstants.ORDER_INFO);

	}

	private void bindListeners() {
		toggleTradeInfo.setOnClickListener(this);
		getVeriCode.setOnClickListener(this);
		submit.setOnClickListener(this);
		changePhone.setOnClickListener(this);
		changePhone.getPaint().setUnderlineText(true);
	}

	private void sendVeriCode() {
		if(sender==null)
			sender=new VeriCodeSender(UriConstants.GET_ORDER_VERICODE,"tempVeriCode",getVeriCode);
		sender.setMessage("orderId=" + order.id + "&userPhone=" + order.userPhone);
		IConstants.THREAD_POOL.submit(sender);
	}

	private void submit() {
		final String veri=veriCode.getText().toString();
		if(!AppUtil.isVeriCode(veri)){
			AppUtil.toast("请正确输入验证码");
			return ;
		}
		final ProgressDialog pd=DialogUtil.createProgressDialogNshow("订单支付", "处理中。请稍等",false,getActivity());
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				try {
					final AppOrder respOrder=AppUtil.getServiceFactory().getDataServiceInstance(getActivity()).confirmAccountPay
					(veri, order.id);
					IConstants.MAIN_HANDLER.post(new Runnable(){
						public void run(){
							if(respOrder==null){
								AppUtil.toast("支付失败。。。请确认账户余额和验证码");
							}else	
								((PayIndexActivity)getActivity()).informPayResult(respOrder);
							
						}
					});
				} catch (AppException e) {
					e.printStackTrace();
				}
				pd.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == toggleTradeInfo) {
			if (tr0.getVisibility() == View.GONE) {
				tr0.setVisibility(View.VISIBLE);
				tr1.setVisibility(View.VISIBLE);
			} else {
				tr0.setVisibility(View.GONE);
				tr1.setVisibility(View.GONE);
			}
		} else if (v == getVeriCode) {
			sendVeriCode();
		} else if (v == submit) {
			submit();
		} else if (v == changePhone) {
			Intent intent = new Intent(getActivity(),
					SettingAccManaChangeTelActivity.class);
			startActivity(intent);
		}

	}

}
