package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.swsk.rgyxtqapp.utils.CommonUtils;

/**
 * Created by apple on 16/2/18.
 */
public abstract  class AnquanguanliBaseActivity extends Activity {
    public Button back;
    public Button more;
    public TextView title;
    public int resid=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(resid);
        back = (Button)findViewById(R.id.headerback);
        more = (Button)findViewById(R.id.headerright);
        title = (TextView)findViewById(R.id.headertitle);

        Intent intent = getIntent();
        title.setText(intent.getStringExtra("title"));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
