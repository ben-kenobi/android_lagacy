package com.bshare.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.bshare.core.*;
import com.bshare.platform.Platform;
import com.bshare.platform.PlatformFactory;
import com.bshare.utils.BSUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Chris.xue
 * 更多平台界面。
 */
public class BShareMore extends Activity {
    private ListView list;
    private BSShareItem shareItem;
    private BShareHandler handler;
    private volatile List<PlatformType> moreList;
    private volatile List<String> moreListContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View content = LayoutInflater.from(this).inflate(BSUtils.getResourseIdByName(this, "layout", "bshare_more"),
                null);
        setContentView(content);
        shareItem = getIntent().getParcelableExtra(Constants.KEY_EXTRAS_SHARE_ITEM);
        handler = getIntent().getParcelableExtra(Constants.KEY_EXTRAS_BSHARE_HANDLER);
        if (handler == null) {
            handler = new DefaultHandler();
        }
        moreList = Config.instance().getMoreList();
        if (moreList == null || moreList.size() == 0) {
            moreList = new ArrayList<PlatformType>(0);
            moreListContent = new ArrayList<String>(0);
        } else {
            moreListContent = new ArrayList<String>(moreList.size());
            for (PlatformType pt : moreList) {
                moreListContent.add(this.getString(BSUtils.getResourseIdByName(this, "string", pt.getPlatfromName())));
            }
        }
        initView();
    }

    private void initView() {
        list = (ListView) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_more_list"));
        list.setFastScrollEnabled(true);
        list.setClickable(true);
        list.setAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(BShareMore.this).inflate(
                        BSUtils.getResourseIdByName(BShareMore.this, "layout", "bshare_more_item"), null);
                TextView tv = (TextView) convertView.findViewById(BSUtils.getResourseIdByName(BShareMore.this, "id",
                        "tv_bs_more_list_item"));
                if (tv == null) {
                    Log.v("bshare", "tv is null");
                } else {
                    tv.setText(moreListContent.get(position));
                }
                return convertView;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public Object getItem(int index) {
                return moreList.get(index);
            }

            @Override
            public int getCount() {
                return moreList.size();
            }
        });

        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Platform platform = PlatformFactory.getPlatform(BShareMore.this, moreList.get(position));
                platform.setShareItem(shareItem);
                if (platform.getPlatformType() == PlatformType.SMS || platform.getPlatformType() == PlatformType.EMAIL) {
                    platform.share();
                    return;
                }
                Intent i = new Intent(Constants.BROWSER_ACTION);
                i.putExtra("url", platform.getUrl());
                startActivity(i);
                if (Config.instance().isAutoCloseShareList()) {
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
