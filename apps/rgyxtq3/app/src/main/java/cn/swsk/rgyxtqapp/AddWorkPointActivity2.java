package cn.swsk.rgyxtqapp;

/**
 * Created by tom on 2015/10/28.
 */

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import cn.swsk.rgyxtqapp.custom.topbar;

public class AddWorkPointActivity2 extends Activity {
    private WorkPointView wpv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workpoint2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        initViews();


    }


    private void initViews() {
        wpv=(WorkPointView)findViewById(R.id.wpv);
        topbar mTopbar = (topbar) findViewById(R.id.workPointTopbar);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
            }
        });
    }


}
