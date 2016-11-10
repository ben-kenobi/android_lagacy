package cn.swsk.rgyxtqapp;

import android.util.Log;
import android.view.ViewGroup;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by apple on 16/2/22.
 */
public class ScanQRCode extends AnquanguanliBaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ViewGroup vg ;
    @Override
    public void preCreate() {

        resid=R.layout.scan_qr_activity;
    }

    @Override
    public void anaCreate() {
        vg=(ViewGroup)findViewById(R.id.fl01);
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
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
        Log.e("itag", rawResult.getText()); // Prints scan results
        Log.e("itag", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
    }


}
