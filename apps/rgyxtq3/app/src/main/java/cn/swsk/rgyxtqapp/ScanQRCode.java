package cn.swsk.rgyxtqapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import cn.swsk.rgyxtqapp.utils.CommonUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by apple on 16/2/22.
 */
public class ScanQRCode extends AnquanguanliBaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ViewGroup vg ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.scan_qr_activity;
        super.onCreate(savedInstanceState);
        vg=(ViewGroup)findViewById(R.id.fl01);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        mScannerView.setBackgroundColor(0xffffffff);
        vg.getLayoutParams().height=CommonUtils.getScreenHeight(this)-80;
        vg.addView(mScannerView);


    }



    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        getIntent().putExtra("result",rawResult.getText());
        setResult(1,getIntent());

        finish();
    }


}
