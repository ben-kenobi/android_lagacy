package fj.swsk.cn.guidetest;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

/**
 * Created by apple on 16/7/15.
 */
public class DetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation==
                Configuration.ORIENTATION_LANDSCAPE){
            finish();
            return;
        }
        if(savedInstanceState==null){
            DetailFrag detailFrag = new DetailFrag();
            detailFrag.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(android.R.id.content,detailFrag).commit();
        }
    }
}
