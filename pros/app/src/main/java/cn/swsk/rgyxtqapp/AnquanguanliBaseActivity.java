package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by apple on 16/2/18.
 */
public abstract  class AnquanguanliBaseActivity extends Activity {
    private Button back;
    private Button more;
    private TextView title;
    public int resid=0;


    public abstract void preCreate();
    public abstract  void anaCreate();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preCreate();
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
        anaCreate();


    }

}
