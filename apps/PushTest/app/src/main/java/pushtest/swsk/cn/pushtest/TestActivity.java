package pushtest.swsk.cn.pushtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by apple on 16/3/30.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        if(null!=getIntent()){
            Bundle bundle = getIntent().getExtras();
            String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            tv.setText("{\ntitle: "+title+"  ,\ncontent: "+content+"\n}");
        }
        addContentView(tv,new ViewGroup.LayoutParams(-1,-1));
    }
}
