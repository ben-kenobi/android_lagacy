package fj.swsk.cn.eqapp.main.V;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.LocationInfoService;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.main.M.LocationInfo;

/**
 * Created by apple on 16/6/17.
 */
public class LocationChooserPop extends Dialog implements AdapterView.OnItemSelectedListener {


    Context context;
    Spinner city,county,town;
    public CommonAdapter<LocationInfo> cityad,countyad,townad;
    public int selpos=0;

    public LocationChooserPop(Context context) {
       super(context,
                R.style.dialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.locationchooserpop,
                null, false);
        getWindow().setContentView(contentView,
                new ViewGroup.LayoutParams(-1, -1));
        this.context = context;
        init();
    }





    private void init() {
        city= (Spinner)findViewById(R.id.city);
        county= (Spinner)findViewById(R.id.county);
        town= (Spinner)findViewById(R.id.town);
        city.setOnItemSelectedListener(this);
        county.setOnItemSelectedListener(this);
        town.setOnItemSelectedListener(this);

        city.setAdapter(cityad = new LocAdapter(context));

        county.setAdapter(countyad = new LocAdapter(context));

        town.setAdapter(townad=new LocAdapter(context));
        cityad.setDatas(LocationInfoService.ins.findByParent(null));
        countyad.setDatas(LocationInfoService.ins.findByParent(cityad.getItem(0)));
        townad.setDatas(LocationInfoService.ins.findByParent(countyad.getItem(0)));
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent==city){
            countyad.setDatas(LocationInfoService.ins.findByParent(cityad.getItem(position)));
            townad.setDatas(LocationInfoService.ins.findByParent(countyad.getItem(0)));
        }else if(parent==county){
            townad.setDatas(LocationInfoService.ins.findByParent(countyad.getItem(position)));
        }else if(parent == town){
            selpos=position;
        }
    }
    public LocationInfo getSelInfo(){
        return townad.getItem(selpos);
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class LocAdapter extends CommonAdapter<LocationInfo>{
        public LocAdapter(Context context) {
            super(context, null, android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public void convert(ViewHolder holder, LocationInfo locationInfo) {
            holder.setText(android.R.id.text1,locationInfo.REGION_NAME);
        }
    }
}
