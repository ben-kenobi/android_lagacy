package fj.swsk.cn.eqapp.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.M.PopInfos;
import fj.swsk.cn.eqapp.map.adapter.GridViewAdapter;

public class DeathTheaticSearchActivity extends BaseTopbarActivity {
    Intent intent;
    EditText distanceEditText;
    GridView mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_death_theatic_search;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);

        intent = this.getIntent();
        mGridView = (GridView)findViewById(R.id.gridview);
        distanceEditText = (EditText)findViewById(R.id.distanceEditText);
        mGridView.setAdapter(new GridViewAdapter(this, PopInfos.controlflows2));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Object txt = distanceEditText.getText();
                if (txt == null) {
                    Toast.makeText(DeathTheaticSearchActivity.this, "请输入搜索距离", Toast.LENGTH_SHORT);
                    return;
                }
                double dis = Double.parseDouble(txt.toString());//默认1KM
                intent.setClass(DeathTheaticSearchActivity.this, DeathThematicQueryMapActivity.class);
                intent.putExtra("deathThematic", ++position);
                intent.putExtra("distance", dis * 1000);
                startActivity(intent);
            }
        });
    }

    public void onExit(View v) {
        finish();
    }
}


