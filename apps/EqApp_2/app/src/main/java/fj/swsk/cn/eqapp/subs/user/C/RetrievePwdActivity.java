package fj.swsk.cn.eqapp.subs.user.C;

import android.content.DialogInterface;
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
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.StringUtil;

/**
 * Created by apple on 16/6/16.
 */
public class RetrievePwdActivity extends BaseTopbarActivity implements View.OnClickListener {

    private EditText username,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.retrievepwd_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        email=(EditText)findViewById(R.id.editText1);
        username=(EditText)findViewById(R.id.editText2);

        email.setTypeface(Typeface.SANS_SERIF);
        username.setTypeface(Typeface.SANS_SERIF);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit){
            String un  = username.getText().toString();
            String em = email.getText().toString();
            if(TextUtils.isEmpty(un)){
                CommonUtils.toast("请填写用户名");
                username.requestFocus();
                return ;
            }
            if(!StringUtil.isEmail(em)){
                CommonUtils.toast("邮箱格式不正确");
                email.requestFocus();
                return;
            }
            String path = IConstants.retrievePwd+ "?name="+CommonUtils.UrlEnc(un)+
                    "&email="+em;
            NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    DialogUtil.getDefault(RetrievePwdActivity.this, "提示",map.get("msg")+"" ).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();

                        }
                    }).show();
                }
            });




        }
    }
}
