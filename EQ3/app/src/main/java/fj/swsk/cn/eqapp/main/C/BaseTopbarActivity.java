package fj.swsk.cn.eqapp.main.C;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.Common.Topbar;

/**
 * Created by apple on 16/6/15.
 */
public class BaseTopbarActivity extends Activity {
    protected int layoutRes;
    protected Topbar mTopbar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes);
        mTopbar = (Topbar) findViewById(R.id.topbar);
        mTopbar.letfListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

    }
}
