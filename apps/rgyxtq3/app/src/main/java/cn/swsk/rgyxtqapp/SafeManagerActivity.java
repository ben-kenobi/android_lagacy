package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import cn.swsk.rgyxtqapp.adapter.SafeManagerGridViewAdapter;
import cn.swsk.rgyxtqapp.contants.SafeManagerFinal;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;

/**
 * Created by tom on 2015/10/17.
 */
public class SafeManagerActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener {

    private GridView dgSafeManager;
    private String text;
    private boolean isroot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_manager);

        topbar mTopbar = (topbar) findViewById(R.id.safeManagerTopbar);
        mTopbar.setRightButtonIsvisable(false);
        final boolean root = getIntent().getBooleanExtra("root",false);
        if(root){
            mTopbar.leftButton.setText("<登出");
            isroot = true;
        }
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                if(root){
                   UserManageActivity.logout(SafeManagerActivity.this);
                }
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
         Intent intent = new Intent();
        CommonUtils.log(text);
        if (text.equals(SafeManagerFinal.itemName[0])) {
            intent.setClass(this, ImportManagementActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);

        } else if (text.equals(SafeManagerFinal.itemName[1])) {
            intent.setClass(this,ExportManageActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[2])) {
            intent.setClass(this,AnquanChangeStatus.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[0])) {
            intent.setClass(this,ProblemHandleActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[3])) {
            intent.setClass(this,InventoryManageActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        } else if (text.equals(SafeManagerFinal.itemName[4])) {
            intent.setClass(this,StatisticsActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        }else if (text.equals(SafeManagerFinal.itemName[5])) {
            intent.setClass(this,StockWarningActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        }else if (text.equals(SafeManagerFinal.itemName[6])) {
            intent.setClass(this,AnquanSystemconfActivity.class);
            intent.putExtra("title",text);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isroot&&keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
