package swsk.cn.rgyxtq.main.C;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import swsk.cn.rgyxtq.R;

/**
 * Created by apple on 16/2/26.
 */
public class FragmentPage2 extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_2,null);
        EditText et = (EditText)view.findViewById(R.id.et_qr_string);
        String mes = et.getHint().toString();
        return view;

    }
}
