package swsk.cn.rgyxtq.subs.work.C;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.UUID;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;
import swsk.cn.rgyxtq.subs.work.Common.WorkSatatusGridViewAdapter;
import swsk.cn.rgyxtq.util.DialogUtil;
import swsk.cn.rgyxtq.util.NetworkUtils;

/**
 * Created by apple on 16/3/2.
 */
public class WorkStatusActivity extends Activity implements AdapterView.OnItemClickListener{



    //private static String PATH = "http://192.168.1.100:8080/rgyx/appWorkSub/upWorkStatus";
    //private List<String> imageViewList = new ArrayList<String>();
    private int curPosition = 0; //当前选中项下标
    private String curItemText; //当前选中项文本
    private ImageView mImageView;
    GridView dgWorkStatus;
    //private String destination;
    private String destinationNo;
    private String workType;
    private int curWorkStatus; //当前作业状态
    private boolean isNewWork; //是否为新建作业信息

    private int tempPosition = 0; //临时

    private boolean isFirstLoad = true; //是否第一次加载
    //    private boolean dialogIsShow = false;
    private String workInfoId; //作业信息Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_status);



        Topbar mTopbar = (Topbar) findViewById(R.id.workPlanTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
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
        curWorkStatus = intent.getIntExtra("status", 1);
        isNewWork = intent.getBooleanExtra("isNewWork", true);
        workType = intent.getStringExtra("workType"); //作业类型（1：增雨、2：防雹）

        if (intent.hasExtra("destinationName")) {
            ((TextView) findViewById(R.id.tv_work_place_V)).setText(intent.getStringExtra("destinationName"));
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

        /*dgWorkStatus = (DragGrid) findViewById(R.id.dgWorkStatus);*/
        dgWorkStatus = (GridView) findViewById(R.id.dgWorkStatus);
        dgWorkStatus.setAdapter(new WorkSatatusGridViewAdapter(this));
        dgWorkStatus.setOnItemClickListener(this);

    }

    /**
     * 完全加载完毕时触发
     *
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isFirstLoad) {
            //如果是新的作业就选中状态【出发】，否则选中当前作业状态
            if (isNewWork) {
                curWorkStatus = 1; //当前作业状态
                curPosition = curWorkStatus - 1;
                //设置出发默认选中
                View view = (View) dgWorkStatus.getChildAt(curPosition);
                ImageView ivReady = (ImageView) view.findViewById(R.id.select);
                ivReady.setVisibility(View.VISIBLE);
                mImageView = (ImageView) view.findViewById(R.id.select);
                TextView tv = (TextView) view.findViewById(R.id.main_gridview_item_name);
                curItemText = tv.getText().toString();

                if("".equals(PushUtils.workGroupId)){
                    //生成作业信息组标识并保存到全局变量中
                    PushUtils.workGroupId = UUID.randomUUID().toString();
                }

                callWebService();
            } else {
                curPosition = curWorkStatus - 1;
                View view = (View) dgWorkStatus.getChildAt(curPosition);
                ImageView ivReady = (ImageView) view.findViewById(R.id.select);
                ivReady.setVisibility(View.VISIBLE);
                mImageView = (ImageView) view.findViewById(R.id.select);
                TextView tv = (TextView) view.findViewById(R.id.main_gridview_item_name);
                curItemText = tv.getText().toString();
            }
            isFirstLoad = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view.findViewById(R.id.main_gridview_item_name);
        curItemText = tv.getText().toString();

        //忽略重复选中
        if (curWorkStatus == (position + 1)) {
            return;
        }

        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        if (isHas == false) {
            Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tv == null) {
            Toast.makeText(this, "TextView为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mImageView = (ImageView) view.findViewById(R.id.select);

//        if (!dialogIsShow) {
//            dialogIsShow = true;

        if (curItemText.equals("出发")) {
            Toast.makeText(WorkStatusActivity.this, "当前作业不能重复设置状态为【出发】！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (curItemText.equals("取消作业")) {
            if (curWorkStatus == 4) {
                Toast.makeText(WorkStatusActivity.this, "当前作业已完成，不可取消！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (curItemText.equals("作业完毕")) {
            if (curWorkStatus == 6) {
                Toast.makeText(WorkStatusActivity.this, "当前作业已被取消，请进行新的作业！", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (curItemText.equals("再作业") || curItemText.equals("恢复初始状态")) {

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
        AlertDialog.Builder builder = DialogUtil.getMessageDialogBuilder(this, "确定要修改当前状态吗？", "确认", dialogListener, calcleDialogListener);
        builder.show();
//        }
    }

    private android.content.DialogInterface.OnClickListener calcleDialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
//            dialogIsShow = false;
        }
    };
    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
//            dialogIsShow = false;

            //当前选中的项下标
            curPosition = tempPosition;

            if (curItemText.equals("出发")) {
                //return;
            } else if (curItemText.equals("到达")) {
            } else if (curItemText.equals("准备就绪")) {
            } else if (curItemText.equals("作业完毕")) {
            } else if (curItemText.equals("再作业")) {
                AlertDialog.Builder builder = DialogUtil.getItemDialogBuilder(WorkStatusActivity.this, new String[]{"到达", "准备就绪"}, "状态修改", dialogListenerStatus);
                builder.show();
            } else if (curItemText.equals("取消作业")) {
            } else if (curItemText.equals("恢复初始状态")) {
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


    /**
     * 调用webService去修改状态
     */
    private void callWebService() {
        String PATH = "http://" + PushUtils.getServerIPText(WorkStatusActivity.this) + "/rgyx/appWorkSub/upWorkStatus";
        String path = PATH + "?token=" + PushUtils.token + "&workPointNo=" + destinationNo +
                "&workStatus=" + (curPosition + 1) + "&workType=" + workType + "&workInfoId=" + workInfoId +
                "&terminalPhone=" + PushUtils.terminalPhone + "&workGroupId=" + PushUtils.workGroupId;
        NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map) {
                View upView = (View) dgWorkStatus.getChildAt(curWorkStatus - 1);
                ImageView ivReady = (ImageView) upView.findViewById(R.id.select);
                ivReady.setVisibility(View.GONE);

                mImageView.setVisibility(View.VISIBLE);
                //imageViewList.add(curItemText);
                workInfoId = map.get("workInfoId").toString();
                Toast.makeText(WorkStatusActivity.this, curItemText + "状态修改完成", Toast.LENGTH_SHORT).show();

                curWorkStatus = curPosition + 1; //由于状态从1开始，curPosition下标从0开始，所以curPosition需要加1
            }
        });
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

    private android.content.DialogInterface.OnClickListener dialogListenerStatus = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            View view = null;

            //重新设置再作业的逻辑
            switch (which) {
                case 0: //到达
                    curPosition = 1;
                    curWorkStatus = 2;
                    view = (View) dgWorkStatus.getChildAt(curPosition);
                    ImageView ivReady = (ImageView) view.findViewById(R.id.select);
                    ivReady.setVisibility(View.VISIBLE);

                    mImageView = ivReady;

                    //此处需调用服务端改变状态
                    callWebService();
                    break;
                case 1://准备就绪
                    curPosition = 2;
                    curWorkStatus = 3;
                    view = (View) dgWorkStatus.getChildAt(curPosition);
                    ivReady = (ImageView) view.findViewById(R.id.select);
                    ivReady.setVisibility(View.VISIBLE);

                    mImageView = ivReady;

                    //此处需调用服务端改变状态
                    callWebService();
                    break;
            }
        }
    };
}
