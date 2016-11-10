package fj.swsk.cn.eqapp.subs.more.Common;

import android.content.Context;
import android.content.Intent;

import fj.swsk.cn.eqapp.subs.collect.C.PendingSubmissionActivity2;
import fj.swsk.cn.eqapp.subs.collect.C.SubmissionHisActivity2;
import fj.swsk.cn.eqapp.subs.more.C.EQHisActivity;
import fj.swsk.cn.eqapp.subs.more.C.EmergencyPrescript;
import fj.swsk.cn.eqapp.subs.setting.C.SettingActivity;

/**
 * Created by apple on 16/6/27.
 */
public class MoreClickHandler {

    public static void clickAt(int pos,Context con){
        if(pos==0){
            con.startActivity(new Intent(con,PendingSubmissionActivity2.class));
        }else if(pos==1){
            con.startActivity(new Intent(con,SubmissionHisActivity2.class));

        }else if(pos == 2){
            con.startActivity(new Intent(con,EQHisActivity.class));

        }else if(pos==3){
            con.startActivity(new Intent(con,EmergencyPrescript.class));
        }else if(pos==4){

        }else if(pos==5){


        }else if(pos == 6){
            con.startActivity(new Intent(con,SettingActivity.class));

        }

    }

}
