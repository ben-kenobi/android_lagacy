package fj.swsk.cn.guidetest;

import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fj.swsk.cn.guidetest.util.CommonUtils;

/**
 * Created by apple on 16/7/14.
 */
public class TestActivity extends AppCompatActivity  implements View.OnClickListener {
    static int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    @Override
    public void finishActivityFromChild(Activity child, int requestCode) {
        super.finishActivityFromChild(child, requestCode);
        CommonUtils.log(child+"   ==================");
        CommonUtils.log(getClass().getName() + "  finishActivityFromChild");
    }

    @Override
    public void onClick(View v) {
       startActivityForResult(new Intent(this, MainActivity.class), 22);
    }
}
