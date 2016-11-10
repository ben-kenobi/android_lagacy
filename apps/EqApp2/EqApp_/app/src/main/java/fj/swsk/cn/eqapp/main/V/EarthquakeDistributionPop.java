package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/6/17.
 */
public class EarthquakeDistributionPop extends BasePopWindow implements View.OnClickListener,AdapterView.OnItemClickListener {
    ListView lv;

    StringAryAdapter adapter;

    Context context;
    public ImageButton dissmissBtn;
    StrAryPopWindow.OnPopListClickListener listener;



    public EarthquakeDistributionPop(View trig, Context context,String[] strary,int[] icns,StrAryPopWindow.OnPopListClickListener listner)  {
        super(R.layout.popupwindow_distribution, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
        update(trig,strary,icns,listner);
    }

    public EarthquakeDistributionPop(Context context){
        super(R.layout.popupwindow_distribution, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
    }


    private void init(){
        dissmissBtn=(ImageButton)contentView.findViewById(R.id.dismiss);
        dissmissBtn.setOnClickListener(this);
        lv=(ListView)contentView.findViewById(R.id.listView1);
        lv.setAdapter(adapter = new StringAryAdapter(context));
        lv.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==dissmissBtn){
            dismiss();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(listener!=null)
            listener.onclick(this,position);
        dismiss();
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
