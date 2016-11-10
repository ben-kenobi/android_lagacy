/**
 * Copyright 2014 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fj.swsk.cn.locationtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ZbarZxing.XZbar.DecodeThread;
import com.ZbarZxing.XZbar.HxBarcode;

/**
 * Location sample.
 *
 * Demonstrates use of the Location API to retrieve the last known location for a device.
 * This sample uses Google Play services (GoogleApiClient) but does not need to authenticate a user.
 * See https://github.com/googlesamples/android-google-accounts/tree/master/QuickStart if you are
 * also using APIs that need authentication.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    ImageView mResultImage;
    TextView mResultText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mResultImage=(ImageView)findViewById(R.id.imgv);
        mResultText=(TextView)findViewById(R.id.text);

    }

    @Override
    public void onClick(View v) {
        HxBarcode hxBarcode = new HxBarcode();
        hxBarcode.scan(MainActivity.this, 501, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode)
        {
            case 501:
                if(data!=null)
                {
                    Bundle extras = data.getBundleExtra("data");



                    if (null != extras) {
                        int width = extras.getInt("width");
                        int height = extras.getInt("height");
                        String result = extras.getString("result");
                        mResultText.setText(result);


                        //以下只是为了显示图片。
                        FrameLayout.LayoutParams lps = new FrameLayout.LayoutParams(width,height);
                        lps.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
                        lps.leftMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
                        lps.rightMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());

                        mResultImage.setLayoutParams(lps);
                        Bitmap barcode = null;
                        byte[] compressedBitmap = extras.getByteArray(DecodeThread.BARCODE_BITMAP);
                        if (compressedBitmap != null) {
                            barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                            // Mutable copy:
                            barcode = barcode.copy(Bitmap.Config.RGB_565, true);
                        }
                        mResultImage.setImageBitmap(barcode);
                    }
                }
                break;
        }
    }
}
