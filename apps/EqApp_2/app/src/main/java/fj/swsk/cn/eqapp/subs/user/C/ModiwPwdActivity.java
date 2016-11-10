package fj.swsk.cn.eqapp.subs.user.C;

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
import fj.swsk.cn.eqapp.util.SharePrefUtil;
import fj.swsk.cn.eqapp.util.StringUtil;

/**
 * Created by apple on 16/6/16.
 */
public class ModiwPwdActivity extends BaseTopbarActivity implements View.OnClickListener{
    EditText oldpwd,newpwd,newpwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.modipwd_activity;
        super.onCreate(savedInstanceState);
        mTopbar.rightListener=this;
        oldpwd=(EditText)findViewById(R.id.editText1);
        newpwd=(EditText)findViewById(R.id.editText2);
        newpwd2=(EditText)findViewById(R.id.editText3);
    }

    @Override
    public void onClick(View v) {
        if(v==mTopbar.rightButton){
            String paras="";
            if(!TextUtils.isEmpty(paras = check())){

                NetUtil.commonRequest(this, IConstants.upPwd + "?" + paras, new NetUtil.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        CommonUtils.toast(map.get("msg").toString());
                        finish();
                    }
                });
            }
        }
    }
    String check(){
        StringBuffer paras = new StringBuffer("");
        String old = oldpwd.getText().toString(),
        newp = newpwd.getText().toString(),
        newp2 = newpwd2.getText().toString();

        if(!SharePrefUtil.getPwd().equals(old)){
            CommonUtils.toast("原密码不匹配,请重新输入");
            oldpwd.requestFocus();
            return null;
        }
        if(!StringUtil.isPwd(newp)){
            CommonUtils.toast("新密码不符合规范，只能由6到20位的数字,字母及下划线组成");
            newpwd.requestFocus();
            return null;
        }
        if(!newp.equals(newp2)){
            CommonUtils.toast("两次输入的新密码不一致");
            newpwd2.requestFocus();
            return null;
        }
        paras.append("name="+CommonUtils.UrlEnc(PushUtils.getLoginName()));
        paras.append("&pwd="+old);
        paras.append("&newPwd="+newp);


        return paras.toString();
    }
}
