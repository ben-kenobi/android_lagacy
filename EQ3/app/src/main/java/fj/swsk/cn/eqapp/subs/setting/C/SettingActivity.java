package fj.swsk.cn.eqapp.subs.setting.C;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.Topbar;
import fj.swsk.cn.eqapp.main.Common.UpdateManager;
import fj.swsk.cn.eqapp.subs.setting.Common.SettingSectionAdap;
import fj.swsk.cn.eqapp.subs.setting.M.SettingDatas;
import fj.swsk.cn.eqapp.util.SharePrefUtil;

/**
 * Created by apple on 16/7/6.
 */
public class SettingActivity extends BaseTopbarActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ListView lv;
    private SettingSectionAdap adapt;
    PopupWindow bottom_pop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.setting_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        lv = (ListView) findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);
        lv.setAdapter(adapt = new SettingSectionAdap(this));
        lv.setOnItemClickListener(this);

        updateItems();

        SettingDatas.items[2][1] = "1".equals(SharePrefUtil.getByUser("updateNotice", "1")) ? "未发现新版本" : "发现新版本";
    }

    private void updateItems() {
        SettingDatas.items[0][1] = PushUtils.getPwdReservedDays() + "天";
        adapt.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 2) {
            initNshowBottomPop();
        } else if (position == 4) {
            Intent intent = new Intent();
            intent.setClass(this, OfflineMapDownload.class);
            startActivity(intent);
        } else if (position == 6) {

        } else if (position == 7) {
            UpdateManager.check(this, true);
        }
    }

    public void initNshowBottomPop() {
        if (bottom_pop == null) {
            View view = getLayoutInflater().inflate(R.layout.bottom_pop_view_choosenum, null);

            bottom_pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            bottom_pop.setAnimationStyle(R.style.MenuAnimationFade);
            bottom_pop.setBackgroundDrawable(new BitmapDrawable());
            bottom_pop.setOutsideTouchable(true);
            bottom_pop.setFocusable(true);
            bottom_pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

            final NumberPicker np = (NumberPicker) view.findViewById(R.id.numberPicker);
            np.setMaxValue(60);
            np.setMinValue(0);
            np.setValue(PushUtils.getPwdReservedDays());
            np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            Topbar topbar = (Topbar) view.findViewById(R.id.topbar);
            topbar.letfListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottom_pop.dismiss();
                }
            };
            topbar.rightListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottom_pop.dismiss();
                    PushUtils.setPwdReservedDays(np.getValue());
                    updateItems();
                }
            };

        }
        bottom_pop.showAtLocation(mTopbar, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logout) {
            PushUtils.logout(this);
        }
    }
}
