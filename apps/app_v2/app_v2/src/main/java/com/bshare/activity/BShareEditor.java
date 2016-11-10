package com.bshare.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import com.bshare.BShare;
import com.bshare.core.*;
import com.bshare.platform.Platform;
import com.bshare.platform.PlatformFactory;
import com.bshare.utils.BSUtils;
import com.bshare.utils.ImageUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @author Chris.xue
 * 可编辑内容的分享界面。
 */
public class BShareEditor extends Activity {
    public static final int RESULT_CODE_AUTHORIZATION = 1;
    public static final int SHOW_PROGRESS_DIALOG = 2;
    public static final int HIDE_PROGRESS_DIALOG = 3;
    //记录当前处于激活状态的平台位置。
    private int[] isActive;
    //待分享的内容。
    private volatile BSShareItem shareItem;
    private GridView platform_gridview;
    private Button backButton;
    private Button shareButton;
    private EditText editText;
    private ImageButton mentionButton;
    private ImageButton topicButton;
    private TextView count;
    private ProgressDialog pd;
    private Builder dialogDialogBuilder;
    private ImageView sharePic;
    //待上传的图片
    private Bitmap thumbnail;
    private byte[] img;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case SHOW_PROGRESS_DIALOG:
                    pd.show();
                    break;
                case HIDE_PROGRESS_DIALOG:
                    pd.hide();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private class PlatformHolder {
        private View gridItem;
        private ImageView platformLogo;
        private ImageView selectedIco;
        private TextView platformName;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(BSUtils.getResourseIdByName(this, "layout", "bshare_editor"));
        sharePic = (ImageView) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_pic"));
        sharePic.setClickable(true);
        shareItem = (BSShareItem) getIntent().getParcelableExtra(Constants.KEY_EXTRAS_SHARE_ITEM);
        sharePic.setVisibility(View.INVISIBLE);
        if (shareItem == null) {
            shareItem = new BSShareItem();
        } else if (shareItem.getImg() != null && shareItem.getImg().length > 0) {
            try {
                thumbnail = ImageUtils.decodeFile(shareItem.getImg(), 120);
                sharePic.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                sharePic.setVisibility(View.VISIBLE);
                sharePic.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog ad = dialogDialogBuilder.create();
                        ad.setButton2(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                "bshare_alert_cancel_button")), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        if (img == null) {

                            ad.setMessage(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_remove_pic_message")));
                            ad.setButton(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_remove_pic_remove")), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    img = shareItem.getImg();
                                    shareItem.setImg(null);
                                }
                            });

                        } else {
                            ad.setMessage(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_add_pic_message")));
                            ad.setButton(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_add_pic_add")), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    shareItem.setImg(img);
                                    img = null;
                                }
                            });
                        }
                        ad.show();
                    }
                });
                System.gc();
            } catch (Exception e) {
                Log.e("bshare", "Exception", e);
            }
        }
        dialogDialogBuilder = new Builder(this);
        pd = new ProgressDialog(this);
        pd.setIndeterminate(true);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        pd.setMessage(getString(BSUtils.getResourseIdByName(this, "string", "bshare_editor_dialog_message")));
        platform_gridview = (GridView) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_platform_grid"));
        backButton = (Button) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_button_back"));
        shareButton = (Button) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_button_share"));
        editText = (EditText) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_edittext"));
        mentionButton = (ImageButton) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_button_mention"));
        topicButton = (ImageButton) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_button_topic"));
        count = (TextView) findViewById(BSUtils.getResourseIdByName(this, "id", "bs_share_text_count"));

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topicButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpannableString span = new SpannableString(" #"
                        + getString(BSUtils.getResourseIdByName(BShareEditor.this, "string", "bshare_topic_span"))
                        + "#");
                int end = editText.getSelectionEnd();
                int start = editText.getSelectionStart();
                editText.getEditableText().delete(Math.min(start, end), Math.max(start, end));
                editText.append(span);
                if (editText.getEditableText().charAt(start + 1) == '#') {
                    start++;
                }
                editText.setSelection(start + 1, start + span.length() - 2);
            }
        });

        mentionButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SpannableString span = new SpannableString(" @"
                        + getString(BSUtils.getResourseIdByName(BShareEditor.this, "string", "bshare_mention_span")));
                int end = editText.getSelectionEnd();
                int start = editText.getSelectionStart();
                editText.getEditableText().delete(Math.min(start, end), Math.max(start, end));
                editText.append(span);
                editText.setSelection(start + 2, start + span.length());
            }
        });

        shareButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final List<PlatformType> shareList = Config.instance().getShareList();
                shareItem.setContent(editText.getEditableText().toString());
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        int needAuth = 0;
                        int success = 0;
                        int failure = 0;
                        long stamp = System.currentTimeMillis();
                        try {
                            BShareMessageDispatcher.fireBulkShareStart(stamp);
                            handler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                            for (int i = 0; i < shareList.size(); i++) {
                                if (isActive[i] == 1) {
                                    PlatformType type = shareList.get(i);
                                    if (type.isOauth()) {
                                        Platform p = PlatformFactory.getPlatform(getApplication(), type);
                                        BSShareItem sItem = new BSShareItem(type, shareItem.getTitle(), shareItem
                                                .getContent(), shareItem.getUrl());
                                        sItem.setImageUrl(shareItem.getImageUrl());
                                        sItem.setImg(shareItem.getImg());
                                        p.setShareItem(sItem);
                                        p.setAccessTokenAndSecret(getApplication());
                                        if (p.validation(false)) {
                                            try {
                                                p.share();
                                                success++;
                                            } catch (Exception e) {
                                                failure++;
                                                Log.e("bshare", "Exception", e);
                                            }
                                        } else {
                                            needAuth++;
                                            failure++;
                                        }
                                    }
                                }
                            }
                            if (Config.instance().isAutoCloseShareList()) {
                                finish();
                            }
                        } catch (Exception e) {
                            Log.e("bshare", "Exception", e);
                        } finally {
                            BShareMessageDispatcher.fireBulkShareComplete(stamp, success, failure, needAuth);
                            handler.sendEmptyMessage(HIDE_PROGRESS_DIALOG);
                        }
                    }
                });
                t.start();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                count.setText(s.length() + "/140");
            }
        });

        if (TextUtils.isEmpty(shareItem.getContent())) {
            editText.append(shareItem.getTitle() + " " + shareItem.getUrl());
        } else {
            editText.append(shareItem.getContent());
        }

        platform_gridview.setAdapter(new MyAdapter(this));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CODE_AUTHORIZATION:
                switch (resultCode) {
                    case RESULT_OK:
                        Toast.makeText(this,
                                BSUtils.getResourseIdByName(this, "string", "bshare_toast_authorization_success"),
                                Toast.LENGTH_SHORT).show();
                        Bundle extras = data.getExtras();
                        TokenInfo tokenInfo = extras.getParcelable("tokenInfo");
                        int pos = extras.getInt("position");
                        if (pos >= 0 && pos < isActive.length) {
                            isActive[pos] = 1;
                        }
                        if (tokenInfo != null) {
                            platform_gridview.setAdapter(new MyAdapter(this));
                            ((MyAdapter) platform_gridview.getAdapter()).notifyDataSetChanged();
                        }
                        break;
                    default:
                        Toast.makeText(this,
                                BSUtils.getResourseIdByName(this, "string", "bshare_toast_authorization_failed"),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyAdapter extends BaseAdapter {

        private WeakReference<Context> context;

        private List<PlatformType> szPlatformNavigation = Config.instance().getShareList();

        // 定义 图片资源
        private int[] szIconItemNavigation;

        private int[] szIconItemInactiveNavigation;

        // 定义gridview标题资源
        private String[] szTitleItemNavigation;

        MyAdapter(Context c) {
            context = new WeakReference<Context>(c);
            szPlatformNavigation = Config.instance().getShareList();
            if (isActive == null) {
                isActive = new int[szPlatformNavigation.size()];
                for (int i = 0; i < isActive.length; i++) {
                    isActive[i] = 1;
                }
            }
            szIconItemNavigation = new int[szPlatformNavigation.size()];
            szIconItemInactiveNavigation = new int[szPlatformNavigation.size()];
            szTitleItemNavigation = new String[szPlatformNavigation.size()];

            for (int i = 0; i < szPlatformNavigation.size(); i++) {
                PlatformType p = szPlatformNavigation.get(i);
                szIconItemNavigation[i] = BSUtils.getResourseIdByName(BShareEditor.this, "drawable",
                        "bshare_platform_item_" + p.getPlatformId());
                szIconItemInactiveNavigation[i] = BSUtils.getResourseIdByName(BShareEditor.this, "drawable",
                        "bshare_platform_item_inactive_" + p.getPlatformId());
                szTitleItemNavigation[i] = getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                        p.getPlatfromName()));
            }
        }

        @Override
        public int getCount() {
            return szIconItemNavigation.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Context ctx = context.get();
            PlatformHolder holder;
            if (ctx == null) {
                return null;
            }
            if (convertView == null || convertView.getTag() == null) {
                holder = new PlatformHolder();
                // 获得convertView
                convertView = LayoutInflater.from(ctx).inflate(
                        BSUtils.getResourseIdByName(ctx, "layout", "platform_grid_item"), null);
                holder.gridItem = convertView.findViewById(BSUtils.getResourseIdByName(ctx, "id",
                        "bs_platform_grid_item"));
                holder.platformLogo = (ImageView) convertView.findViewById(BSUtils.getResourseIdByName(ctx, "id",
                        "bs_platform_logo"));
                holder.platformName = (TextView) convertView.findViewById(BSUtils.getResourseIdByName(ctx, "id",
                        "bs_platfrom_name"));
                holder.selectedIco = (ImageView) convertView.findViewById(BSUtils.getResourseIdByName(ctx, "id",
                        "bs_platform_selected"));

                convertView.setTag(holder);
            } else {
                holder = (PlatformHolder) convertView.getTag();
            }

            final PlatformType p = szPlatformNavigation.get(position);

            holder.platformName.setText(szTitleItemNavigation[position]);
            if (BShare.instance(ctx).isBindAccount(ctx, p)) {

                holder.platformLogo.setBackgroundResource(szIconItemNavigation[position]);

                final PlatformHolder ph = holder;
                if (isActive[position] == 1) {
                    holder.selectedIco.setVisibility(View.VISIBLE);
                } else {
                    holder.selectedIco.setVisibility(View.INVISIBLE);
                }
                holder.gridItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (ph.selectedIco.getVisibility() == View.INVISIBLE) {
                            isActive[position] = 1;
                        } else {
                            isActive[position] = 0;
                        }
                        ph.selectedIco.setVisibility(ph.selectedIco.getVisibility() == View.INVISIBLE
                                ? View.VISIBLE
                                : View.INVISIBLE);
                    }
                });

                if (p.isOauth()) {
                    holder.gridItem.setOnLongClickListener(new OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            AlertDialog ad = dialogDialogBuilder.create();
                            String message = getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_delete_account"));
                            Platform platform = PlatformFactory.getPlatform(ctx, p);
                            String name = platform.getUserName(getApplication());
                            if (!TextUtils.isEmpty(name)) {
                                message += "[" + name + "]";
                            }
                            ad.setMessage(message);
                            ad.setButton(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_ok_button")), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    BShare.instance(ctx).removeCredential(ctx, p);
                                    platform_gridview.setAdapter(new MyAdapter(BShareEditor.this));
                                    ((MyAdapter) platform_gridview.getAdapter()).notifyDataSetChanged();
                                }
                            });
                            ad.setButton2(getString(BSUtils.getResourseIdByName(BShareEditor.this, "string",
                                    "bshare_alert_cancel_button")), new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            ad.show();
                            return true;
                        }
                    });
                } else if (p == PlatformType.MORE || p == PlatformType.SMS || p == PlatformType.EMAIL) {
                    holder.selectedIco.setVisibility(View.INVISIBLE);
                    holder.gridItem.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            String content = editText.getText().toString();
                            if (p == PlatformType.SMS || p == PlatformType.EMAIL) {
                                Platform platform = PlatformFactory.getPlatform(ctx, p);
                                platform.setShareItem(new BSShareItem(PlatformType.MORE, shareItem.getTitle(),
                                        TextUtils.isEmpty(content) ? shareItem.getContent() : content, shareItem
                                        .getUrl()));
                                platform.share();
                            } else {
                                Intent intent = new Intent();
                                intent.setAction(Config.instance().getMoreAction());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                intent.putExtra(Constants.KEY_EXTRAS_SHARE_ITEM, new BSShareItem(PlatformType.MORE,
                                        shareItem.getTitle(), TextUtils.isEmpty(content)
                                        ? shareItem.getContent()
                                        : content, shareItem.getUrl()));
                                startActivity(intent);
                            }

                        }
                    });
                } else {
                    holder.selectedIco.setVisibility(View.INVISIBLE);

                    holder.gridItem.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            String content = editText.getText().toString();
                            Platform platform = PlatformFactory.getPlatform(BShareEditor.this, p);
                            platform.setShareItem(shareItem);
                            shareItem.setContent(content);
                            Intent intent = new Intent();
                            intent.setAction(Constants.BROWSER_ACTION);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("url", platform.getUrl());
                            startActivity(intent);
                        }
                    });

                }
            } else {
                isActive[position] = 0;
                holder.platformLogo.setBackgroundResource(szIconItemInactiveNavigation[position]);
                holder.selectedIco.setVisibility(View.INVISIBLE);
                holder.gridItem.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                handler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
                                Platform platform = PlatformFactory.getPlatform(ctx, p);
                                String url = platform.getUrl();
                                handler.sendEmptyMessage(HIDE_PROGRESS_DIALOG);
                                if (TextUtils.isEmpty(url)) {
                                    return;
                                }
                                Intent i = new Intent(BShareEditor.this, AuthorizationActivity.class);
                                Bundle data = new Bundle();
                                data.putString("url", url);
                                data.putParcelable("platform", p);
                                data.putInt("position", position);
                                i.putExtras(data);
                                startActivityForResult(i, RESULT_CODE_AUTHORIZATION);

                            }
                        });
                        t.start();

                    }
                });
            }

            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        pd.dismiss();
        sharePic.setBackgroundDrawable(null);
        if (thumbnail != null && !thumbnail.isRecycled()) {
            thumbnail.recycle();
        }
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        isActive = savedInstanceState.getIntArray("isActive");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray("isActive", isActive);
        super.onSaveInstanceState(outState);
    }

}
