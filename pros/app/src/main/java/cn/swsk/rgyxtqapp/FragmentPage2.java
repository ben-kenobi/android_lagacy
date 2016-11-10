package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentPage2 extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, null);
        EditText qr = (EditText) view.findViewById(R.id.et_qr_string);
        String mes= qr.getHint().toString();
        System.out.println("FragmentPage2.onCreateView");
        System.out.println("inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        System.out.println("mes = " + mes);
        return view;
    }
}