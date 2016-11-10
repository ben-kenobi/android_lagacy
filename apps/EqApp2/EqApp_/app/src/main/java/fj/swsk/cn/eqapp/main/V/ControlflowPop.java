package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/6/17.
 */
public class ControlflowPop extends BasePopWindow implements View.OnClickListener,AdapterView.OnItemClickListener {
    GridView gv;

    public StringAryGvAdapter adapter;

    Context context;
    public ImageButton dissmissBtn;
    StrAryPopWindow.OnPopListClickListener listener;
    public Button save;



    public ControlflowPop(View trig, Context context, String[] strary, int[] icns, StrAryPopWindow.OnPopListClickListener listner)  {
        super(R.layout.popupwindow_controlflow, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
        update(trig,strary,icns,listner);
    }

    public ControlflowPop(Context context){
        super(R.layout.popupwindow_controlflow, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
    }


    private void init(){
        dissmissBtn=(ImageButton)contentView.findViewById(R.id.dismiss);
        dissmissBtn.setOnClickListener(this);
        gv=(GridView)contentView.findViewById(R.id.gv01);
        save=(Button)contentView.findViewById(R.id.save);
        save.setOnClickListener(this);
        gv.setAdapter(adapter = new StringAryGvAdapter(context));
        gv.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==dissmissBtn){
            dismiss();
        }else if(v==save){
//            if(listener!=null)
//                listener.onclick(this, -1);
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonUtils.log(view+"===="+view.isActivated());
        if(listener!=null)
            listener.onclick(this, position);
        adapter.onPosSelected(position);
    }


    public void update(View trig,String[] strAry,int[] icons,StrAryPopWindow.OnPopListClickListener listener){

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)dissmissBtn.getLayoutParams();
        int pos[]=new int[2];
        trig.getLocationOnScreen(pos);
        lp.topMargin=pos[1]- ResUtil.dp2Intp(25);

        this.listener=listener;
        adapter.setIcons(icons);
        adapter.setSary(strAry);
    }



    @Override
    public void execute() throws Throwable {

    }



    @Override
    public void resetTrigger() {}
}
