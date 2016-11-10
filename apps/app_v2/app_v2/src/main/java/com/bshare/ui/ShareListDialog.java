package com.bshare.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bshare.core.BSShareItem;
import com.bshare.core.BShareShareListAdapter;
import com.bshare.core.BShareWindowHandler;
import com.bshare.core.Config;
import com.bshare.core.Constants;
import com.bshare.core.PlatformType;
import com.bshare.platform.GeneralPlatform;
import com.bshare.platform.Platform;
import com.bshare.platform.PlatformFactory;
import com.bshare.utils.BSUtils;

/**
 * 
 * @author chris.xue
 * 
 */
public class ShareListDialog extends Dialog implements android.view.View.OnClickListener {

    public ShareListDialog(Context context, BSShareItem shareItem) {

        super(context, BSUtils.getResourseIdByName(context, "style", "bshare_dialog"));
        this.shareItem = shareItem;
    }

    public ShareListDialog(Context context, BSShareItem shareItem, BShareWindowHandler windowHandler) {
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

    protected BSShareItem shareItem;
    private List<BShareWindowHandler> windowHandlers = new ArrayList<BShareWindowHandler>();

    protected View btnMore;
    protected ListView lvPlatform;
    protected List<PlatformType> platformList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View content = LayoutInflater.from(getContext()).inflate(
                BSUtils.getResourseIdByName(getContext(), "layout", "bshare_share_list"), null);
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

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = display.getWidth() < display.getHeight() ? Constants.DIMENSIONS_PORTRAIT
                : Constants.DIMENSIONS_LANDSCAPE;

        addContentView(content, new FrameLayout.LayoutParams((int) (dimensions[0] * scale + 0.5f),
                (int) (dimensions[1] * scale + 0.5f)));
        // setContentView(content);
        platformList = new ArrayList<PlatformType>(Config.instance().getShareList());
        btnMore = findViewById(BSUtils.getResourseIdByName(getContext(), "id", "bs_share_button_more"));
        lvPlatform = (ListView) findViewById(BSUtils.getResourseIdByName(getContext(), "id",
                "bs_share_button_list_view"));
        BShareShareListAdapter adapter = new BShareShareListAdapter(getContext(), platformList, this);
        lvPlatform.setAdapter(adapter);

        if (btnMore != null) {
            if (Config.instance().isShouldDisplayMore()) {
                btnMore.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Dialog moreList = Config.instance().getDialogBuilder()
                                .buildMoreListDialog(getContext(), shareItem, null);
                        moreList.show();
                        if (isShowing()) {
                            dismiss();
                        }
                    }
                });
            } else {
                btnMore.setVisibility(View.GONE);
            }
        }
    }

    public void share(PlatformType platformType) {
        final Platform platform = PlatformFactory.getPlatform(getContext(), platformType);
        platform.setShareItem(shareItem);

        if (!BSUtils.isOnline(getContext())) {
            Toast.makeText(getContext(),
                    BSUtils.getResourseIdByName(getContext(), "string", "bshare_error_msg_offline"), Toast.LENGTH_SHORT)
                    .show();
        } else {

            if ((platform instanceof GeneralPlatform) == true) {
                platform.share();
            } else {
                platform.setAccessTokenAndSecret(getContext());
                if (platform.validation(true)) {

                    AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
                        @Override
                        protected Object doInBackground(Object... params) {
                            platform.share();
                            return null;
                        }
                    };
                    task.execute();
                }

            }

        }
        if (Config.instance().isAutoCloseShareList() && isShowing()) {
            dismiss();
        }
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

    @Override
    public void onClick(View item) {
        PlatformType p = (PlatformType) item.getTag();
        if (p != null) {
            final Platform platform = PlatformFactory.getPlatform(getContext(), p);
            platform.setShareItem(shareItem);

            if (!BSUtils.isOnline(getContext())) {
                Toast.makeText(getContext(),
                        BSUtils.getResourseIdByName(getContext(), "string", "bshare_error_msg_offline"),
                        Toast.LENGTH_SHORT).show();
            } else {

                if ((platform instanceof GeneralPlatform) == true) {
                    platform.share();
                } else {
                    platform.setAccessTokenAndSecret(getContext());
                    if (platform.validation(true)) {

                        AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
                            @Override
                            protected Object doInBackground(Object... params) {
                                platform.share();
                                return null;
                            }
                        };
                        task.execute();
                    }

                }

            }
            if (Config.instance().isAutoCloseShareList() && isShowing()) {
                dismiss();
            }
        }
    }

}