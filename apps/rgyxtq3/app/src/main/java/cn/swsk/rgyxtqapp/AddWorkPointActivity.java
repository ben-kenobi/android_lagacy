package cn.swsk.rgyxtqapp;

/**
 * Created by tom on 2015/10/28.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.SortGroupMemberAdapter;
import cn.swsk.rgyxtqapp.bean.GroupMemberBean;
import cn.swsk.rgyxtqapp.custom.ClearEditText;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.SideBar;
import cn.swsk.rgyxtqapp.custom.SideBar.OnTouchingLetterChangedListener;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.CharacterParser;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PinyinComparator;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class AddWorkPointActivity extends Activity implements SectionIndexer {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView txt_dialog;
    private SortGroupMemberAdapter adapter;
    private ClearEditText mClearEditText;

    private LinearLayout titleLayout;
    private TextView title;
    private TextView tvNofriends;

    private String workType = "增雨";

    private String destinationName; // 目的地名称

    private String destinationNo; //目的地编号

    public final static int RESULT_CODE = 1;

    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<GroupMemberBean> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private ProgressDialog dialog = null; //等待状态提示框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_add_workpoint);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        initViews();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 初始化
     */
    private void init(){
        dialog = new ProgressDialog(AddWorkPointActivity.this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddWorkPoint Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cn.swsk.rgyxtqapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddWorkPoint Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cn.swsk.rgyxtqapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * 查询作业点列表
     */
    class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog();
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭ProgressDialog

            if(stringObjectMap == null){
                Toast.makeText(AddWorkPointActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status")?stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //获取返回的数据集合
                List<Map<String, Object>> workPointList = (List<Map<String, Object>>) stringObjectMap.get("workPointList");

                //查询数据为空时
                if(workPointList == null || workPointList.size() == 0){
                    return;
                }
                SourceDateList = filledData(workPointList);//getResources().getStringArray(R.array.data)
                // 根据a-z进行排序源数据
                Collections.sort(SourceDateList, pinyinComparator);
                adapter = new SortGroupMemberAdapter(AddWorkPointActivity.this, SourceDateList);
                sortListView.setAdapter(adapter);
                sortListView.setOnScrollListener(new OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        int section = getSectionForPosition(firstVisibleItem);
                        if (firstVisibleItem != lastFirstVisibleItem) {
                            MarginLayoutParams params = (MarginLayoutParams) titleLayout
                                    .getLayoutParams();
                            params.topMargin = 0;
                            titleLayout.setLayoutParams(params);
                            title.setText(SourceDateList.get(
                                    getPositionForSection(section)).getSortLetters());
                        }
                        if(totalItemCount<2){

                            return;
                        }

                        int nextSection = getSectionForPosition(firstVisibleItem + 1);
                        int nextSecPosition = getPositionForSection(+nextSection);

                        if (nextSecPosition == firstVisibleItem + 1) {
                            View childView = view.getChildAt(0);
                            if (childView != null) {
                                int titleHeight = titleLayout.getHeight();
                                int bottom = childView.getBottom();
                                MarginLayoutParams params = (MarginLayoutParams) titleLayout
                                        .getLayoutParams();
                                if (bottom < titleHeight) {
                                    float pushedDistance = bottom - titleHeight;
                                    params.topMargin = (int) pushedDistance;
                                    titleLayout.setLayoutParams(params);
                                } else {
                                    if (params.topMargin != 0) {
                                        params.topMargin = 0;
                                        titleLayout.setLayoutParams(params);
                                    }
                                }
                            }
                        }
                        lastFirstVisibleItem = firstVisibleItem;
                    }
                });
            } else if("2".equals(status)) {
                Toast.makeText(AddWorkPointActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if("9999".equals(status)) {
                Toast.makeText(AddWorkPointActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddWorkPointActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            CommonUtils.log(path);
            String jsonString1 = HttpUtils.getJsonContent(path);
            if (jsonString1 == null) {
                return null;
            }
            Map<String, Object> map = JsonTools.getMap(jsonString1);
            return map;
        }
    }


    private void initViews() {
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);
        title = (TextView) this.findViewById(R.id.title_layout_catalog);
        topbar mTopbar = (topbar) findViewById(R.id.workPointTopbar);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
            }
        });
        tvNofriends = (TextView) this
                .findViewById(R.id.title_layout_no_friends);
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        txt_dialog = (TextView) findViewById(R.id.txt_dialog);
        sideBar.setTextView(txt_dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
               /* Toast.makeText(
                        getApplication(),
                        ((GroupMemberBean) adapter.getItem(position)).getName(),
                        Toast.LENGTH_SHORT).show();*/
                destinationName =
                        ((GroupMemberBean) adapter.getItem(position)).getName();
                destinationNo = ((GroupMemberBean) adapter.getItem(position)).getWorkTypeId();

                if (destinationName == null) {
                    Toast.makeText(
                            getApplication(),
                            "请选择目的地。",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = DialogUtils.getMessageDialogBuilder(AddWorkPointActivity.this, "目的地：" + destinationName + "\n作业类型为：" + workType, " 作业点选择", dialogListener);
                builder.show();
            }
        });

        //作业类型
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_work_type);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) findViewById(checkedId);

                /*Toast.makeText(
                        getApplication(),
                        radioButton.getText(),
                        Toast.LENGTH_SHORT).show();*/
                workType = radioButton.getText().toString();
            }
        });

        //请求服务端
        String PATH = "http://" + PushUtils.getServerIPText(AddWorkPointActivity.this) + "/rgyx/appWorkSub/findWorkPointList";
        String path = PATH + "?token=" + PushUtils.token;
        new TomAsyncTask().execute(path);

//        mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);
//
//        // 根据输入框输入值的改变来过滤搜索
//        mClearEditText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before,
//                                      int count) {
//                // 这个时候不需要挤压效果 就把他隐藏掉
//                titleLayout.setVisibility(View.GONE);
//                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
//                filterData(s.toString());
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count,
//                                          int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
    }

    /**
     * 作业点选择确认框回调函数
     */
    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            boolean isHas = NetworkUtils.isNetworkAvailable(AddWorkPointActivity.this);
            if (isHas == false) {
                Toast.makeText(AddWorkPointActivity.this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
                return;
            }
//            Intent intent = new Intent();
//            intent.putExtra("result", new String[]{workType, destination, destinationNo});
//            setResult(RESULT_CODE, intent);

            String tmpWorkType = "";

            if("增雨".equals(workType)){
                tmpWorkType = "1";
            }else if("防雹".equals(workType)) {
                tmpWorkType = "2";
            }
            //打开作业状态修改窗口
            Intent intent = new Intent();
            intent.setClass(AddWorkPointActivity.this, WorkStatusActivity.class);
            intent.putExtra("workType", tmpWorkType);
            intent.putExtra("destinationNo",destinationNo);
            intent.putExtra("destinationName", destinationName);
            intent.putExtra("isNewWork",  true);
            startActivity(intent);

            finish();
        }
    };

    /**
     * 为ListView填充数据
     *
     * @param maps
     * @return
     */
    private List<GroupMemberBean> filledData(List<Map<String, Object>> maps) {
        List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

        for (int i = 0; i < maps.size(); i++) {
            GroupMemberBean sortModel = new GroupMemberBean();
            String name = ((Map<String, Object>) maps.get(i)).get("JOB_SITE_N").toString();
            String no = ((Map<String, Object>) maps.get(i)).get("JOB_SITE_C").toString();
            sortModel.setName(name);
            sortModel.setWorkTypeId(no);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(name);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
            tvNofriends.setVisibility(View.GONE);
        } else {
            filterDateList.clear();
            for (GroupMemberBean sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
        if (filterDateList.size() == 0) {
            tvNofriends.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return SourceDateList.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            String sortStr = SourceDateList.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
