package com.example.apple.zxingtest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.zxing.Result;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private ListView lv;

        @Override
        public void onCreate(Bundle state) {
            super.onCreate(state);
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
//            setContentView(mScannerView);                // Set the scanner view as the content view
            setContentView(R.layout.activity_main);                // Set the scanner view as the content view

            lv = (ListView)findViewById(R.id.lv);
            lv.setAdapter(new MainAda(this));
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

class MainAda extends BaseAdapter  implements SectionIndexer{
    public List<GroupMemberBean> list= GroupMemberBean.testList();
    Context con;
    public MainAda(Context con){
        super();
        this.con = con;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv=null;
        if(convertView==null){
            tv=  new TextView(con);
            convertView=tv;
            convertView.setLayoutParams(new ViewGroup.LayoutParams(-1,100));
            tv.setGravity(Gravity.CENTER);
        }
        tv=(TextView)convertView;
        tv.setText(list.get(position).getName());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        String ary[]= { "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                "W", "X", "Y", "Z", "#" };
        return ary;
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return sectionIndex;
    }

    @Override
    public int getSectionForPosition(int position) {
        return position;
    }
}