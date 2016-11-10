package fj.swsk.cn.eqapp.subs.more.Common;

import android.app.Activity;
import android.content.Intent;

import fj.swsk.cn.eqapp.subs.collect.C.PendingSubmissionActivity2;
import fj.swsk.cn.eqapp.subs.collect.C.SubmissionHisActivity2;
import fj.swsk.cn.eqapp.subs.more.C.EQHisActivity;
import fj.swsk.cn.eqapp.subs.more.C.EmergencyPrescript;
import fj.swsk.cn.eqapp.subs.more.C.EqBrochureActivity;
import fj.swsk.cn.eqapp.subs.more.C.LoacalWebActivity;
import fj.swsk.cn.eqapp.subs.setting.C.SettingActivity;

/**
 * Created by apple on 16/6/27.
 */
public class MoreClickHandler {

    public static void clickAt(int pos,String title,Activity con){
        if(pos==0){
            con.startActivity(new Intent(con,PendingSubmissionActivity2.class));
        }else if(pos==1){
            con.startActivity(new Intent(con,SubmissionHisActivity2.class));

        }else if(pos == 2){
            con.startActivityForResult(new Intent(con, EQHisActivity.class),1);

        }else if(pos==3){
            Intent intent = new Intent(con,EmergencyPrescript.class);
            intent.putExtra("type",0);
            intent.putExtra("title",title);
            con.startActivity(intent);

        }else if(pos==4){

            Intent intent = new Intent(con,EmergencyPrescript.class);
            intent.putExtra("type",1);
            intent.putExtra("title",title);
            con.startActivity(intent);
        }else if(pos == 5){
            Intent intent = new Intent(con, LoacalWebActivity.class);
            intent.putExtra("path","file:///android_asset/fzsc.html");
            intent.putExtra("title",title);

            con.startActivity(intent);

        }else if(pos == 6){
            Intent intent = new Intent(con, EqBrochureActivity.class);
            intent.putExtra("title",title);

            con.startActivity(intent);
        } else if(pos == 7){
            con.startActivity(new Intent(con,SettingActivity.class));

        }

    }

}
