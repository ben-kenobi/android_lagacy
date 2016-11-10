package com.bshare.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bshare.core.BSShareItem;
import com.bshare.core.BShareWindowHandler;
import com.bshare.core.Config;
import com.bshare.core.Constants;
import com.bshare.core.PlatformType;
import com.bshare.platform.Platform;
import com.bshare.platform.PlatformFactory;
import com.bshare.utils.BSUtils;

public class MoreDialog extends Dialog {
    protected ListView list;
    protected BSShareItem shareItem;
    protected List<BShareWindowHandler> windowHandlers = new ArrayList<BShareWindowHandler>();
    protected volatile List<PlatformType> moreList;
    protected volatile List<String> moreListContent;

    public MoreDialog(Context context, BSShareItem shareItem) {
        super(context, BSUtils.getResourseIdByName(context, "style", "bshare_dialog"));
        this.shareItem = shareItem;
    }

    public MoreDialog(Context context, BSShareItem shareItem, BShareWindowHandler windowHandler) {
        this(context, shareItem);
        registerWindowHandler(windowHandler);
    }

    public void registerWindowHandler(BShareWindowHandler windowHandler) {
        if (windowHandler != null) {
            windowHandlers.add(windowHandler);
        }
    }

    public void unregisterWindowHandler(BShareWindowHandler windowHandler) {
        if (windowHandler != null) {
            windowHandlers.remove(windowHandler);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (windowHandlers.isEmpty()) {
                    for (BShareWindowHandler h : windowHandlers) {
                        if (h != null) {
                            h.onWindowClose(this);
                        }
                    }
                    windowHandlers.clear();
                }
            }
        });
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View content = LayoutInflater.from(getContext()).inflate(
                BSUtils.getResourseIdByName(getContext(), "layout", "bshare_share_more"), null);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = display.getWidth() < display.getHeight() ? Constants.DIMENSIONS_PORTRAIT
                : Constants.DIMENSIONS_LANDSCAPE;

        addContentView(content, new FrameLayout.LayoutParams((int) (dimensions[0] * scale + 0.5f),
                (int) (dimensions[1] * scale + 0.5f)));
        moreList = Config.instance().getMoreList();
        if (moreList == null || moreList.size() == 0) {
            moreList = new ArrayList<PlatformType>(0);
            moreListContent = new ArrayList<String>(0);
        } else {
            moreListContent = new ArrayList<String>(moreList.size());
            for (PlatformType pt : moreList) {
                moreListContent.add(getContext().getString(
                        BSUtils.getResourseIdByName(getContext(), "string", pt.getPlatfromName())));
            }
        }
        initView();
    }

    private void initView() {
        list = (ListView) findViewById(BSUtils.getResourseIdByName(getContext(), "id", "bs_more_list"));
        list.setFastScrollEnabled(true);
        list.setClickable(true);
        list.setAdapter(new BaseAdapter() {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        BSUtils.getResourseIdByName(getContext(), "layout", "bshare_more_list_item"), null);
                TextView tv = (TextView) convertView.findViewById(BSUtils.getResourseIdByName(getContext(), "id",
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
                Platform platform = PlatformFactory.getPlatform(getContext(), moreList.get(position));
                platform.setShareItem(shareItem);
                // platform.setBShareHandler(handler);
                platform.share();
                if (Config.instance().isAutoCloseShareList() && isShowing()) {
                    dismiss();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowing()) {
                dismiss();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
