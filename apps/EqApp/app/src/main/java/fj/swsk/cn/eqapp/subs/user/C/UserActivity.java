package fj.swsk.cn.eqapp.subs.user.C;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.user.Common.SimpleSectionAdap;
import fj.swsk.cn.eqapp.util.SharePrefUtil;

/**
 * Created by apple on 16/6/15.
 */
public class UserActivity extends BaseTopbarActivity implements View.OnClickListener ,AdapterView.OnItemClickListener{
    public static final int MODIFY_RETURN=101;
    public static final int MODIFYPWD_RETURN=102;


    private ListView lv;
    private SimpleSectionAdap adapt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.user_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);
        lv = (ListView)findViewById(R.id.pinnedlv);
        lv.setBackgroundResource(R.color.listviewbg);
        lv.setAdapter(adapt = new SimpleSectionAdap(this));
        lv.setOnItemClickListener(this);
        adapt.setDetails(getDetails());
        updateInfo();

    }







    private Map<String,String> getDetails(){
//        Map<String,String> map = new HashMap<>();
//        for(int i=0;i< UserDatas.itemkeys.length-1;i++){
//            for(int j = 0;j<UserDatas.itemkeys[i].length;j++){
//                map.put(UserDatas.itemkeys[i][j],"ffwefsdkkkke");
//            }
//        }
        try {
//            SharePrefUtil.putByUser("infos", JsonTools.getJsonString(map));
            return (Map<String,String>)(Map)JsonTools.getMap(SharePrefUtil.getByUser("infos", ""));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }



    void updateInfo(){
        adapt.setDetails(PushUtils.loginUser.getInfoMap());

    }




    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String key = adapt.getItem(position).toString();
        if (key.equals("PHONE") || "EMAIL".equals(key)) {
            Intent intent = new Intent(this, ModiInfpActivity.class);
            intent.putExtra("type", key);
            startActivityForResult(intent, MODIFY_RETURN);

        } else if ("modipwd".equals(key)) {
            Intent intent = new Intent(this, ModiwPwdActivity.class);
            startActivityForResult(intent, MODIFYPWD_RETURN);
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==MODIFY_RETURN&&resultCode==1){
            updateInfo();

        }else if(requestCode==MODIFYPWD_RETURN&&resultCode==1){
        }
    }

    //    @SuppressLint("NewApi")
//    private void initializeAdapter() {
//        lv.setFastScrollEnabled(isFastScroll);
//        if (isFastScroll) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                lv.setFastScrollAlwaysVisible(true);
//            }
//            lv.setAdapter(new FastScrollAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
//        } else {
//            lv.setAdapter(new SimpleAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
//        }
//    }

}


//
//class FastScrollAdapter extends SimpleAdapter implements SectionIndexer {
//
//    private Item[] sections;
//
//    public FastScrollAdapter(Context context, int resource, int textViewResourceId) {
//        super(context, resource, textViewResourceId);
//    }
//
//    @Override protected void prepareSections(int sectionsNumber) {
//        sections = new Item[sectionsNumber];
//    }
//
//    @Override protected void onSectionAdded(Item section, int sectionPosition) {
//        sections[sectionPosition] = section;
//    }
//
//    @Override public Item[] getSections() {
//        return sections;
//    }
//
//    @Override public int getPositionForSection(int section) {
//        if (section >= sections.length) {
//            section = sections.length - 1;
//        }
//        return sections[section].listPosition;
//    }
//
//    @Override public int getSectionForPosition(int position) {
//        if (position >= getCount()) {
//            position = getCount() - 1;
//        }
//        return getItem(position).sectionPosition;
//    }

//}


