package fj.swsk.cn.eqapp.subs.collect.V;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.Topbar;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;

/**
 * Created by apple on 16/6/17.
 */
public class DensityDescDialog extends Dialog implements AdapterView.OnItemClickListener {


    Context context;
    public CommonAdapter<Integer> adapt;
    public ListView lv;
    public Topbar mTopbar;
    ItemCB cb;
    public DensityDescDialog(Context context) {
       super(context,
               R.style.dialogStyle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        View contentView = LayoutInflater.from(context).inflate(R.layout.densitydescriptiondialog,
                null, false);
        getWindow().setContentView(contentView,
                new ViewGroup.LayoutParams(-1, -1));
        this.context = context;
        if(context instanceof ItemCB){
            this.cb = (ItemCB)context;
        }
        init();
    }





    private void init() {
        mTopbar = (Topbar) findViewById(R.id.topbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.letfListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
        lv = (ListView)findViewById(R.id.pinnedlv);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapt = new DescAdapter(context));
        List<Integer> list = new ArrayList<>();
        for(int i = 1;i<=12;i++){
            list.add(getContext().getResources().getIdentifier(String.format("description_%02d",i),"mipmap",context.getPackageName()));

        }
        adapt.setDatas(list);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dismiss();
        if(cb!=null)
            cb.onItemClick(position);
    }

    public class DescAdapter extends CommonAdapter<Integer>{
        public DescAdapter(Context context) {
            super(context, null, R.layout.item4lv_simpleimg);
        }

        @Override
        public void convert(ViewHolder holder, Integer resid) {

            holder.setImageResource(R.id.imageView1, resid);
        }
    }

    public static interface ItemCB{
        public abstract void onItemClick(int pos);
    }
}
