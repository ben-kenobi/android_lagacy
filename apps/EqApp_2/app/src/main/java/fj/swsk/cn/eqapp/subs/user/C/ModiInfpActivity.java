package fj.swsk.cn.eqapp.subs.user.C;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.StringUtil;

/**
 * Created by apple on 16/6/16.
 */
public class ModiInfpActivity extends BaseTopbarActivity implements View.OnClickListener {

    private EditText mEditText;
    int flag = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.modiinfo_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        mEditText=(EditText)findViewById(R.id.editText1);
        String type = getIntent().getStringExtra("type");
        if("phone".equals(type)){
            mTopbar.setTitleText("电话号码修改");
            mEditText.setHint("输入新的电话号码");
            flag = 0;
        }else if("email".equals(type)){
            mTopbar.setTitleText("邮箱修改");
            mEditText.setHint("输入新的邮箱");
            flag = 1;
        }
        mEditText.setTypeface(Typeface.SANS_SERIF);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit){
            String paras="";
            if(!TextUtils.isEmpty(paras = check())){

                NetUtil.commonRequest(this, IConstants.upUserInfo + "?" + paras, new NetUtil.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        CommonUtils.toast(map.get("msg").toString());
                        setResult(1);
                        if(flag==0){
                            PushUtils.loginUser.phone=mEditText.getText().toString();
                        }else if(flag==1){
                            PushUtils.loginUser.email=mEditText.getText().toString();
                        }
                        finish();
                    }
                });
            }
        }

    }

    String check(){
        StringBuffer sb = new StringBuffer("");
        String content = mEditText.getText().toString();
        if(flag==0){
            if(!StringUtil.isPhoneNum(content)){
                CommonUtils.toast("不是合法的手机号码");
                return null;
            }else{
                sb.append("phone="+content);

            }
        }else if(flag==1){
            if(!StringUtil.isEmail(content)){
                CommonUtils.toast("不是正确的邮箱");
                return null;
            }else{
                sb.append("email="+content);
            }

        }


        sb.append("&token="+ PushUtils.getToken());

        return sb.toString();
    }
}
