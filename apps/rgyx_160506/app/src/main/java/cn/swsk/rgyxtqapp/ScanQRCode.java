package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ZbarZxing.XZbar.ZbarActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Arrays;

import cn.swsk.rgyxtqapp.utils.CommonUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by apple on 16/2/22.
 */
public class ScanQRCode extends AnquanguanliBaseActivity implements ZXingScannerView.ResultHandler,View.OnClickListener {
    private ZXingScannerView mScannerView;
    private ViewGroup vg ;
    private Button flashBtn,manual;


    public static void startScan(Activity context,int requestCode){
        Intent intent = new Intent();
        intent.setClass(context, ScanQRCode.class);
        intent.putExtra("title", "扫描二维码");
        context.startActivityForResult(intent, requestCode);
//        HxBarcode hxBarcode = new HxBarcode();
//        hxBarcode.scan(context, 1, false);
    }
    public static String getResult(Intent data){
        String result =  data.getStringExtra("result");
//        Bundle extras = data.getBundleExtra("data");
//        String result = extras.getString("result");
        return result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.scan_qr_activity;
        super.onCreate(savedInstanceState);
        vg=(ViewGroup)findViewById(R.id.fl01);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setBackgroundColor(0xffffffff);
        vg.getLayoutParams().height=CommonUtils.getScreenHeight(this)-80;
        vg.addView(mScannerView,-1,-1);
        flashBtn=(Button)findViewById(R.id.headerright);
        flashBtn.setOnClickListener(this);
        flashBtn.setBackgroundResource(R.mipmap.flashligh);
        mScannerView.setFormats(Arrays.asList(new BarcodeFormat[]{BarcodeFormat.QR_CODE}));
        mScannerView.setFitsSystemWindows(true);
        manual=(Button)findViewById(R.id.manual);
        manual.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        if(v==flashBtn){
            mScannerView.setFlash(!mScannerView.getFlash());
        }else if(v==manual){
            final View cv = LayoutInflater.from(this).inflate(R.layout.dialog_edittext,null);
            final AlertDialog d = new AlertDialog.Builder(this).setTitle("手动输入")
                    .setView(cv).
            setPositiveButton("确定", null).setNegativeButton("取消", null).create();
            d.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {

                    Button b = d.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String text = ((EditText)cv.findViewById(R.id.editText1)).getText().toString();
                            if(CommonUtils.isValidEqCode(text)){
                                d.dismiss();
                                done(text,1);
                            }else{
                                CommonUtils.toast("输入信息不合法",ScanQRCode.this);
                            }
                        }
                    });
                }
            });
            d.show();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();


// Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        done(rawResult.getText(),0);

    }


    public void done(String text,int type){
        getIntent().putExtra("result", text);
        getIntent().putExtra("type",type);
        setResult(1, getIntent());
        finish();
    }



}


class BarCode{
    private static Context mmcontext = null;

    public BarCode() {
    }

    public void scan(Context context, int requestCode, boolean isLandspace) {
        mmcontext = context;
        Intent intent = new Intent();
        intent.putExtra("isLandSpace", isLandspace);
        intent.setClass(context, ZbarActivity.class);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }

    public static Context getMainActivityInstance() {
        return mmcontext;
    }
}
