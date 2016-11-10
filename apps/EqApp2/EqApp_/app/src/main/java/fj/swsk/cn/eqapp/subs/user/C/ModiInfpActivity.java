package fj.swsk.cn.eqapp.subs.user.C;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.util.CommonUtils;

/**
 * Created by apple on 16/6/16.
 */
public class ModiInfpActivity extends BaseTopbarActivity implements View.OnClickListener {

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes= R.layout.modiinfo_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        mEditText=(EditText)findViewById(R.id.editText1);
        String type = getIntent().getStringExtra("type");
        if("cellphone".equals(type)){
            mTopbar.setTitleText("电话号码修改");
            mEditText.setHint("输入新的电话号码");
        }else if("mailbox".equals(type)){
            mTopbar.setTitleText("邮箱修改");
            mEditText.setHint("输入新的邮箱");
        }
        mEditText.setTypeface(Typeface.SANS_SERIF);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit){
            CommonUtils.log("submit"+mEditText.getText());
            setResult(1);
            finish();
        }
    }
}
