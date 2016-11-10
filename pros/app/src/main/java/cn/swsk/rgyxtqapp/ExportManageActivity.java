package cn.swsk.rgyxtqapp;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import cn.swsk.rgyxtqapp.adapter.AnquanItemsAdapter;

/**
 * Created by apple on 16/2/23.
 */
public class ExportManageActivity  extends AnquanguanliBaseActivity implements View.OnClickListener{
    private ListView lv;
    private AnquanItemsAdapter dapter;
    private Button scan,draw,move,returnback;



    public  void preCreate(){
        this.resid=R.layout.exportmanagementa_ctivity;
    }
    public   void anaCreate(){
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(dapter=new AnquanItemsAdapter(this,lv));
        scan = (Button)findViewById(R.id.scan);
        draw= (Button)findViewById(R.id.draw);
        move = (Button)findViewById(R.id.move);
        returnback = (Button)findViewById(R.id.returnback);
        move.setOnClickListener(this);
        scan.setOnClickListener(this);
        draw.setOnClickListener(this);
        returnback.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v==this.draw){
            Intent intent = new Intent();
            intent.setClass(this, DrawActivity.class);
            intent.putExtra("title", "领取");
            startActivity(intent);

        }else if (v==returnback){
            finish();
        }else if (v== move){
//            dapter.addInfos(Arrays.asList(new AnquanItemInfo[]{AnquanItemInfo.header(), AnquanItemInfo.header()}));

        }else if (v==scan){
            Intent intent = new Intent();
            intent.setClass(this, ScanQRCode.class);
            intent.putExtra("title", "扫描二维码");
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Dialog d = new android.app.AlertDialog.Builder(this).setTitle(getTitle()).
                setMessage(requestCode+"|"+resultCode+"|").create();
        d.setCancelable(true);
        d.show();
        if(requestCode==1&&resultCode==1){

        }
    }
}
