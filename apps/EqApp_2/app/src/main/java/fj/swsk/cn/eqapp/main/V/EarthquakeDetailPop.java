package fj.swsk.cn.eqapp.main.V;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;

/**
 * Created by apple on 16/6/17.
 */
public class EarthquakeDetailPop extends BasePopWindow implements View.OnClickListener {


    Context context;
    public ImageButton dissmissBtn, locationing;


    TextView time, location, latlon, magdepth,lat;


    public EarthquakeDetailPop(View trig, Context context) {
        super(R.layout.earthquake_detail, LayoutInflater.from(context), -1, -1);
        this.context = context;
        init();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) dissmissBtn.getLayoutParams();
        int pos[] = new int[2];
        trig.getLocationOnScreen(pos);
        lp.topMargin = pos[1];
    }

    public EarthquakeDetailPop(Context context) {
        super(R.layout.earthquake_detail, LayoutInflater.from(context), -1, -1);
        this.context = context;
        init();
    }


    private void init() {
        dissmissBtn = (ImageButton) contentView.findViewById(R.id.dismiss);
        dissmissBtn.setOnClickListener(this);
        time = (TextView) contentView.findViewById(R.id.time);
        location = (TextView) contentView.findViewById(R.id.location);
        latlon = (TextView) contentView.findViewById(R.id.latlon);
        lat=(TextView)contentView.findViewById(R.id.lat);
        magdepth = (TextView) contentView.findViewById(R.id.magdepth);
        locationing = (ImageButton) contentView.findViewById(R.id.locationing);
    }


    public void updateInfo() {
        EQInfo info = EQInfo.getIns();
        if (info == null){

            time.setText( "无");
            location.setText("无");
            latlon.setText("无");
            lat.setText("无");
            magdepth.setText("无"+" / "+ "无");
            locationing.setVisibility(View.GONE);
        } else {
            time.setText(info.occurTime);
            location.setText(info.occurRegionName);
            latlon.setText(info.lon+"");
            lat.setText(info.lat+"");
            magdepth.setText(info.magnitude+" / "+ info.focalDepth);
            locationing.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        if (v == dissmissBtn) {
            dismiss();
        }
    }


    @Override
    public void execute() throws Throwable {

    }


    @Override
    public void resetTrigger() {
    }
}
