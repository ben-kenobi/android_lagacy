package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.WorkSatatusGridViewAdapter;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by tom on 2015/10/17.
 */
public class WorkStatusOldActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener {
    private static String PATH = "http://192.168.1.100:8080/rgyx/appWorkSub/upWorkStatus";
    private final static int REQUEST_CODE = 1;

    private List<String> imageViewList = new ArrayList<String>();
    private int mPosition;
    private String mText;
    private ImageView mImageView;
    private GridView dgWorkStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_work_status);

            topbar mTopbar = (topbar) findViewById(R.id.workPlanTopbar);
            mTopbar.setRightButtonIsvisable(false);
            mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
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

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        dgWorkStatus = (GridView) findViewById(R.id.dgWorkStatus);
        WorkSatatusGridViewAdapter workStatusAda = new WorkSatatusGridViewAdapter(this);
        dgWorkStatus.setAdapter(workStatusAda);
        dgWorkStatus.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (imageViewList.contains(mText)) {
            return;
        }

        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        if (isHas == false) {
            Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
            return;
        }

        TextView tv = (TextView) view.findViewById(R.id.main_gridview_item_name);
        if (tv == null) {
            Toast.makeText(this, "TextView为空", Toast.LENGTH_SHORT).show();
            return;
        }

        mText = tv.getText().toString();
        mPosition = position + 1;
        mImageView = (ImageView) view.findViewById(R.id.select);

        AlertDialog.Builder builder = DialogUtils.getMessageDialogBuilder(this, "该操作不可回退，是否继续？", "确认", dialogListener);
        builder.show();
    }

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mText.equals("出发")) {
                Intent intent = new Intent();
                intent.setClass(WorkStatusOldActivity.this, AddWorkPointActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                return;
            } else if (mText.equals("到达")) {
            } else if (mText.equals("准备就绪")) {
            } else if (mText.equals("作业完毕")) {
            } else if (mText.equals("再作业")) {
                AlertDialog.Builder builder = DialogUtils.getItemDialogBuilder(WorkStatusOldActivity.this, new String[]{"到达", "准备就绪"}, "状态修改", dialogListenerStatus);
                builder.show();
                return;
            } else if (mText.equals("取消作业")) {
            }
            callWebService();
        }
    };

    /**
     * 调用webService去修改状态
     */
    private void callWebService() {
        PATH = "http://" + PushUtils.getServerIPText(WorkStatusOldActivity.this) + "/rgyx/appWorkSub/upWorkStatus";
        String path = PATH + "?action_flag=" + mPosition;
        String jsonString1 = HttpUtils.getJsonContent(path);
        if (jsonString1 == null) {
            Toast.makeText(WorkStatusOldActivity.this, "无法连接服务器,请检查配置。", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> map = JsonTools.getMap(jsonString1);
        if (map != null && map.get("status").equals("01")) {
            mImageView.setVisibility(View.VISIBLE);
            imageViewList.add(mText);
            Toast.makeText(WorkStatusOldActivity.this, mText + "状态修改完成", Toast.LENGTH_SHORT).show();
        }
    }

    private DialogInterface.OnClickListener dialogListenerStatus = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            //重新设置再作业的逻辑
            switch (which) {
                case 0: //到达
                    if (imageViewList.contains("到达") && imageViewList.contains("准备就绪")) {
                        imageViewList.remove("到达");
                        imageViewList.remove("准备就绪");
                        View view = (View) dgWorkStatus.getChildAt(1);
                        ImageView ivArrived = (ImageView) view.findViewById(R.id.select);
                        ivArrived.setVisibility(View.GONE);
                        view = (View) dgWorkStatus.getChildAt(2);
                        ImageView ivReady = (ImageView) view.findViewById(R.id.select);
                        ivReady.setVisibility(View.GONE);
                    }
                    //此处需调用服务端改变状态
                    break;
                case 1://准备就绪
                    if (imageViewList.contains("准备就绪")) {
                        imageViewList.remove("准备就绪");
                        View view = (View) dgWorkStatus.getChildAt(2);
                        ImageView ivReady = (ImageView) view.findViewById(R.id.select);
                        ivReady.setVisibility(View.GONE);
                    }
                    //此处需调用服务端改变状态
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == AddWorkPointActivity.RESULT_CODE) {
            Bundle bundle = data.getExtras();
            String[] results = bundle.getStringArray("result");
            Toast.makeText(WorkStatusOldActivity.this, results[0] + "," + results[1], Toast.LENGTH_SHORT).show();
            callWebService();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
