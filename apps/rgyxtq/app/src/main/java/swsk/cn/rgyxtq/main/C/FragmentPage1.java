package swsk.cn.rgyxtq.main.C;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import swsk.cn.rgyxtq.R;

/**
 * Created by apple on 16/2/26.
 */
public class FragmentPage1 extends android.support.v4.app.Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1,null);
    }
}
