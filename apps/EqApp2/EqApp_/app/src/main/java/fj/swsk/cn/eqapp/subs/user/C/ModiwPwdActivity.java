package fj.swsk.cn.eqapp.subs.user.C;

import android.os.Bundle;
import android.view.View;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/16.
 */
public class ModiwPwdActivity extends BaseTopbarActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.modipwd_activity;
        super.onCreate(savedInstanceState);
        mTopbar.rightListener=this;
    }

    @Override
    public void onClick(View v) {
        if(v==mTopbar.rightButton){
            CommonUtils.log("rightbtnclicked");
            setResult(1);
            finish();
        }
    }
}
