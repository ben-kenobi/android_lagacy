package swsk.cn.rgyxtq.main.C;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import swsk.cn.rgyxtq.R;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    private Class framents[]={FragmentPage1.class,FragmentPage2.class,FragmentPage5.class};
    private int imgs[]={R.drawable.tab_home_btn,R.drawable.tab_message_btn,R.drawable.tab_more_btn};
    private String titles[]={"作业上报","二维码","更多"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayoutInflater=LayoutInflater.from(this);
        init();

    }
    private void init(){
        mTabHost=(FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        int count = framents.length;
        for(int i=0;i<count;i++){
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(titles[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec,framents[i],null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }
    private View getTabItemView(int idx){
        View view = mLayoutInflater.inflate(R.layout.tab_item_view,null);
        ((ImageView)view.findViewById(R.id.imageview)).setImageResource(imgs[idx]);
        ((TextView)view.findViewById(R.id.textview)).setText(titles[idx]);
        return view;
    }
}
