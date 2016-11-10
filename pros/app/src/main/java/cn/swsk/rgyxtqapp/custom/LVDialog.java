package cn.swsk.rgyxtqapp.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.R;

/**
 * Created by apple on 16/2/22.
 */
public class LVDialog  {
    public static Dialog getLvDialogNShow(Context con,List<Map<String,String>> list,String title, final ListView.OnItemClickListener listener){
        LinearLayout linearLayoutMain = new LinearLayout(con);
        linearLayoutMain.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ListView listView = new ListView(con);
        listView.setFadingEdgeLength(0);



        SimpleAdapter adapter = new SimpleAdapter(con,
                list, R.layout.item4lv_onerow01,
                new String[] { "name" },
                new int[] { android.R.id.text1 });
        listView.setAdapter(adapter);

        linearLayoutMain.addView(listView,new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        final AlertDialog dialog = new AlertDialog.Builder(con,AlertDialog.THEME_HOLO_LIGHT)
                .setTitle(title).setView(linearLayoutMain).create();

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent,view,position,id);
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

}
