package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/6/17.
 */
public class EarthquakeDetailPop extends BasePopWindow implements View.OnClickListener {


    Context context;
    public ImageButton dissmissBtn;



    public EarthquakeDetailPop(View trig,Context context)  {
        super(R.layout.earthquake_detail, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)dissmissBtn.getLayoutParams();
        int pos[]=new int[2];
        trig.getLocationOnScreen(pos);
        lp.topMargin=pos[1];
    }

    public EarthquakeDetailPop(Context context){
        super(R.layout.earthquake_detail, LayoutInflater.from(context),-1,-1);
        this.context=context;
        init();
    }


    private void init(){
        dissmissBtn=(ImageButton)contentView.findViewById(R.id.dismiss);
        dissmissBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v==dissmissBtn){
            dismiss();
        }
    }



    @Override
    public void execute() throws Throwable {

    }



    @Override
    public void resetTrigger() {}
}
