package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.collect.M.TsceneProg;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;

/**
 * Created by apple on 16/6/28.
 */
public class PendingSubAdapter  extends BaseAdapter  implements View.OnClickListener{
    Context mContext;
    public List<Tscene> list=new ArrayList<>();
    public List<PendingSubGVAdapter> adapters = new ArrayList<>();
    int resid = R.layout.pending_submission_innergv;

    public boolean mulSelMode=false;
    public boolean editMode=false;
    public int flag = 0;

    public PendingSubAdapter(Context con) {
        super();
        this.mContext=con;

    }

    public void setList(List<Tscene> list) {
        this.list = list;
        for(int i = 0;i<list.size();i++){
            PendingSubGVAdapter adapter= new PendingSubGVAdapter(mContext,list.get(i).list);
            adapters.add(adapter);
        }
        notifyDataSetChanged();
    }

    public void refreshData(){
//        for(int i=0;i<list.size();i++){
//            if(list.get(i).mediaList.size()==0){
//                list.remove(i);
//                i--;
//            }
//        }
//        setList(list);

    }


    public void updateDatas(){
        for(int i=0;i<list.size();i++){
            if(list.get(i).flag!=0||(CommonUtils.context.curscene!=null&&
                    CommonUtils.context.curscene._id==list.get(i)._id&&
                    CommonUtils.context.curscene.flag!=0)){
                list.remove(i);
                adapters.remove(i);
                i--;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)  {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(resid,null,false);
            holder=new ViewHolder(convertView);
            holder.delete.setOnClickListener(this);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        PendingSubGVAdapter adapter=adapters.get(position);
        adapter.setMode(mulSelMode);
        adapter.flag=flag;
        holder.gv.setAdapter(adapter);
        ((fj.swsk.cn.eqapp.main.V.InnerGV2)holder.gv).focusible=false;
        holder.gv.setOnItemClickListener(adapter);
        Tscene  event = list.get(position);
        holder.cate.setText(event.event_id);
        holder.time.setText(IConstants.TIMESDF.format(event.addtime));
        holder.delete.setVisibility(editMode&&!isUploadAt(position) ? View.VISIBLE : View.GONE);
        holder.delete.setTag(position);

        updateProg(holder, event,position);


//        ((ViewGroup)convertView).setDescendantFocusability(editMode?ViewGroup.FOCUS_AFTER_DESCENDANTS:ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        return convertView;

    }


    void updateProg(ViewHolder holder,Tscene event,int position){
        TsceneProg tp = CommonUtils.context.uploadingMap.get(event._id);
        if(tp!=null){
            holder.proglo.setVisibility(View.VISIBLE);
            if(tp.compressing){
                holder.pb.setProgress(0);
                holder.cancel.setEnabled(false);
                holder.describe.setText(tp.describe);
                holder.describe.setTextColor(0xff555555);
            }else if(tp.state==2){
                holder.pb.setProgress(0);
                holder.cancel.setEnabled(false);
                holder.describe.setText(tp.describe);
                holder.describe.setTextColor(0xffee5555);
            }else if(tp.state==1){
                holder.describe.setTextColor(0xff55ff55);
                holder.pb.setMax(10);
                holder.pb.setProgress(10);
                holder.cancel.setEnabled(false);
                holder.describe.setText(tp.describe);
                CommonUtils.context.uploadingMap.remove(tp._id);
                IConstants.MAIN_HANDLER.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateDatas();
                    }
                },2000);
            }else{
                holder.describe.setTextColor(0xff555555);
                holder.cancel.setEnabled(true);
                holder.pb.setMax((int) tp.total);
                holder.pb.setProgress((int) tp.progress);
                holder.cancel.setOnClickListener(this);
                holder.cancel.setTag(position);
                holder.describe.setText(tp.describe);
            }

        }else{
            holder.proglo.setVisibility(View.GONE);
        }
    }

    public boolean isUploadAt(int pos){
        TsceneProg tp= CommonUtils.context.uploadingMap.get(list.get(pos)._id);
        return tp!=null&&tp.state!=2;
    }


    public void toggleMod(){
        mulSelMode=!mulSelMode;
        for(PendingSubGVAdapter adapter:adapters){
            adapter.setMode(mulSelMode);
        }

    }
    public void toggleEditMode(){
        editMode=!editMode;
        notifyDataSetChanged();
    }

    class ViewHolder{
        public ViewHolder(View v){
            gv=(GridView)v.findViewById(R.id.gv01);
            time=(TextView)v.findViewById(R.id.time);
            cate=(TextView)v.findViewById(R.id.cate);
            delete=(ImageButton)v.findViewById(R.id.delete);
            cancel=(Button)v.findViewById(R.id.cancel);
            pb = (ProgressBar)v.findViewById(R.id.progress);
            describe = (TextView)v.findViewById(R.id.describe);
            proglo = v.findViewById(R.id.proglo);

            v.setTag(this);
        }
        public GridView gv;
        public TextView cate,time,describe;
        public ImageButton delete;
        public Button cancel;
        public ProgressBar pb;
        public View proglo;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.delete){
            final int pos = (int)v.getTag();
            DialogUtil.getMessageDialogBuilder(mContext, "确定删除该条采集信息？", "警告",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteAt(pos);
                        }
                    }).show();
        }else if(v.getId()==R.id.cancel){
            int pos = (int)v.getTag();
            TsceneProg tp= CommonUtils.context.uploadingMap.get(list.get(pos)._id);
            tp.state=2;
            tp.uploadhandler.cancel();

        }
    }
    public void deleteAt(int pos) {
        Tscene ts = list.remove(pos);
        adapters.remove(pos);
        notifyDataSetChanged();
        ts.delete();
    }

}
