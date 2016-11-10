package fj.swsk.cn.guidetest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by apple on 16/7/15.
 */
public class DetailFrag extends Fragment {

    public int getShownIndex(){
        return getArguments().getInt("index",0);
    }

    public static DetailFrag newInstance(int pos) {

        Bundle args = new Bundle();

        DetailFrag fragment = new DetailFrag();
        fragment.setArguments(args);
        args.putInt("index",pos);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container==null){
            return null;
        }
        ScrollView scroller =new ScrollView(getActivity());
        TextView tv = new TextView(getActivity());
        int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                4,getActivity().getResources().getDisplayMetrics());
        tv.setPadding(padding,padding,padding,padding);
        scroller.addView(tv);
        tv.setText(Infos.dialog[getShownIndex()]);
        return scroller;
    }
}
