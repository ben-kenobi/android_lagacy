package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.UUID;

import cn.swsk.rgyxtqapp.adapter.WorkSatatusGridViewAdapter;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by tom on 2015/10/17.
 */
public class WorkStatusActivity extends FragmentActivity
        implements AdapterView.OnItemClickListener {
    //private static String PATH = "http://192.168.1.100:8080/rgyx/appWorkSub/upWorkStatus";
    //private List<String> imageViewList = new ArrayList<String>();
    GridView dgWorkStatus;
    //private String destination;
    private String destinationNo;
    private String workType;

    private int tempPosition = 0; //临时

    private boolean isFirstLoad = true; //是否第一次加载
    //    private boolean dialogIsShow = false;
    private String workInfoId; //作业信息Id
    private ProgressDialog dialog = null; //等待状态提示框
    private WorkSatatusGridViewAdapter adater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_status);

        init();

        topbar mTopbar = (topbar) findViewById(R.id.workPlanTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                // 点击“返回”后的操作
                WorkStatusActivity.this.finish();
            }

            @Override
            public void rightClick() {

            }
        });
        Intent intent = getIntent();
        mTopbar.setTitleText(getString(R.string.lbl_work_status_submit));
        //destination = intent.getStringExtra("destination");
        if (intent.hasExtra("workInfoId")) {
            workInfoId = intent.getStringExtra("workInfoId");
        }
        destinationNo = intent.getStringExtra("destinationNo"); //目的地编号（作业点编号）
        workType = intent.getStringExtra("workType"); //作业类型（1：增雨、2：防雹）

        if (intent.hasExtra("destinationName")) {
            ((TextView) findViewById(R.id.tv_work_place_V)).setText(intent.getStringExtra
                    ("destinationName"));
        }

        String tmpWorkType = "";

        if ("1".equals(workType)) {
            tmpWorkType = "增雨";
        } else if ("2".equals(workType)) {
            tmpWorkType = "防雹";
        } else {
            tmpWorkType = "未知";
        }
        ((TextView) findViewById(R.id.tv_work_type_V)).setText(tmpWorkType);
        //((TextView) findViewById(R.id.tv_work_place_V)).setText(workType);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        /*dgWorkStatus = (DragGrid) findViewById(R.id.dgWorkStatus);*/
        dgWorkStatus = (GridView) findViewById(R.id.dgWorkStatus);
         adater = new WorkSatatusGridViewAdapter(this);
        adater.setSelIdx(intent.getIntExtra("status", 1)-1);
        dgWorkStatus.setAdapter(adater);
        dgWorkStatus.setOnItemClickListener(this);

        if ("".equals(PushUtils.workGroupId)) {
            //生成作业信息组标识并保存到全局变量中
            PushUtils.workGroupId = UUID.randomUUID().toString();
        }

        callWebService();
    }

    /**
     * 初始化
     */
    private void init() {
        dialog = new ProgressDialog(WorkStatusActivity.this);
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String text =adater.applist.get(position).getName();

        //忽略重复选中
        if (adater.getSelIdx() == (position )) {
            return;
        }
        int curWorkStatus=adater.getSelIdx()+1;
        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        if (isHas == false) {
            Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
            return;
        }

        if (text == null) {
            Toast.makeText(this, "TextView为空", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (!dialogIsShow) {
//            dialogIsShow = true;

        if (text.equals("出发")) {
            Toast.makeText(WorkStatusActivity.this, "当前作业不能重复设置状态为【出发】！", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (text.equals("取消作业")) {
            if (curWorkStatus == 4) {
                Toast.makeText(WorkStatusActivity.this, "当前作业已完成，不可取消！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (text.equals("作业完毕")) {
            if (curWorkStatus == 6) {
                Toast.makeText(WorkStatusActivity.this, "当前作业已被取消，请进行新的作业！", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        if (text.equals("再作业") || text.equals("恢复初始状态")) {

            //如果 作业完毕和取消作业都未选中时
            if (curWorkStatus != 4 && curWorkStatus != 6) {
                new AlertDialog.Builder(WorkStatusActivity.this)
                        .setIcon(null)
                        .setTitle("提示")
                        .setMessage("当前作业未结束，请先选择\n【作业完毕】或【取消作业】\n以结束当前作业！")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                }).create().show();

//                    dialogIsShow = false;
                return;
            }
        }
        tempPosition = position;
        AlertDialog.Builder builder = DialogUtils.getMessageDialogBuilder(this, "确定要修改当前状态吗？",
                "确认", dialogListener, calcleDialogListener);
        builder.show();
//        }
    }

    private android.content.DialogInterface.OnClickListener calcleDialogListener = new
            DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
//            dialogIsShow = false;
                }
            };
    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface
            .OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
//            dialogIsShow = false;

            //当前选中的项下标
            adater.setSelIdx( tempPosition);
            String text =adater.applist.get(tempPosition).getName();
            if (text.equals("出发")) {
                //return;
            } else if (text.equals("到达")) {
            } else if (text.equals("准备就绪")) {
            } else if (text.equals("作业完毕")) {
            } else if (text.equals("再作业")) {
                AlertDialog.Builder builder = DialogUtils.getItemDialogBuilder(WorkStatusActivity
                        .this, new String[]{"到达", "准备就绪"}, "状态修改", dialogListenerStatus);
                builder.show();
            } else if (text.equals("取消作业")) {
            } else if (text.equals("恢复初始状态")) {
                new AlertDialog.Builder(WorkStatusActivity.this)
                        .setIcon(null)
                        .setTitle("提示")
                        .setMessage("本次作业完成！")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //生成作业信息组标识并保存到全局变量中
                                        PushUtils.workGroupId = UUID.randomUUID().toString();

                                        WorkStatusActivity.this.finish();
                                    }
                                }).create().show();

            }
            callWebService();
        }
    };

    class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog();
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭ProgressDialog

            if (stringObjectMap == null) {
                Toast.makeText(WorkStatusActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status")
                    .toString() : "";

            if ("1".equals(status)) {
                workInfoId = stringObjectMap.get("workInfoId").toString();
                Toast.makeText(WorkStatusActivity.this, adater.getCurName() + "状态修改完成", Toast
                        .LENGTH_SHORT).show();
            } else if ("2".equals(status)) {
                Toast.makeText(WorkStatusActivity.this, stringObjectMap.get("errmsg").toString(),
                        Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(WorkStatusActivity.this, stringObjectMap.get("errmsg").toString(),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WorkStatusActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            String jsonString1 = HttpUtils.getJsonContent(path);
            if (jsonString1 == null) {
                return null;
            }
            Map<String, Object> map = JsonTools.getMap(jsonString1);
            return map;
        }
    }

    /**
     * 调用webService去修改状态
     */
    private void callWebService() {
        String PATH = "http://" + PushUtils.getServerIPText(WorkStatusActivity.this) +
                "/rgyx/appWorkSub/upWorkStatus";
        String path = PATH + "?token=" + PushUtils.token + "&workPointNo=" + destinationNo +
                "&workStatus=" + (adater.getSelIdx() + 1) + "&workType=" + workType + "&workInfoId=" +
                workInfoId +
                "&terminalPhone=" + PushUtils.terminalPhone + "&workGroupId=" + PushUtils
                .workGroupId;
        new TomAsyncTask().execute(path);
    }

    /**
     * 监听Back键按下事件,方法1:
     * 注意:
     * super.onBackPressed()会自动调用finish()方法,关闭
     * 当前Activity.
     * 若要屏蔽Back键盘,注释该行代码即可
     */
    @Override
    public void onBackPressed() {

    }

    private android.content.DialogInterface.OnClickListener dialogListenerStatus = new
            DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    View view = null;

                    //重新设置再作业的逻辑
                    switch (which) {
                        case 0: //到达
                            adater.setSelIdx(1);

                            //此处需调用服务端改变状态
                            callWebService();
                            break;
                        case 1://准备就绪
                            adater.setSelIdx(2);
                            //此处需调用服务端改变状态
                            callWebService();
                            break;
                    }
                }
            };
}
