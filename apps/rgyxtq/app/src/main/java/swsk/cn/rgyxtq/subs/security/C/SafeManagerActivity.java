package swsk.cn.rgyxtq.subs.security.C;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.security.Common.SafeManagerFinal;
import swsk.cn.rgyxtq.util.NetworkUtils;

/**
 * Created by tom on 2015/10/17.
 */
public class SafeManagerActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener {

    private GridView dgSafeManager;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_manager);

        Topbar mTopbar = (Topbar) findViewById(R.id.safeManagerTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        Intent intent = getIntent();
        mTopbar.setTitleText(intent.getStringExtra("title"));

        dgSafeManager = (GridView) findViewById(R.id.dgSafeManager);
        SafeManagerGridViewAdapter safeManagerGridViewAdapter = new SafeManagerGridViewAdapter(this);
        dgSafeManager.setAdapter(safeManagerGridViewAdapter);
        dgSafeManager.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        if (isHas == false) {
            Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tv = (TextView) view.findViewById(R.id.main_gridview_item_name);
         text = tv.getText().toString();
        final Intent intent = new Intent();
        if (text.equals(SafeManagerFinal.itemName[0])) {
//            intent.setClass(this,ImportManagementActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[1])) {
//            intent.setClass(this,ExportManageActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[2])) {
        } else if (text.equals(SafeManagerFinal.itemName[3])) {
        } else if (text.equals(SafeManagerFinal.itemName[4])) {
        } else if (text.equals(SafeManagerFinal.itemName[5])) {
        }else if (text.equals(SafeManagerFinal.itemName[6])) {
        }
    }
}
