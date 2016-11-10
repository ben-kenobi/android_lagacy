package fj.swsk.cn.guidetest;

import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by apple on 16/7/15.
 */
public class TileFrag extends ListFragment {
    boolean mDualPane;
    int mCurCheckPosition = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_activated_1,
                Infos.titles));
        View detailFrame = getActivity().findViewById(R.id.detail);
        mDualPane = detailFrame!=null && detailFrame.getVisibility()==View.VISIBLE;
        getListView().setSaveEnabled(false);
        if(savedInstanceState!=null){
            mCurCheckPosition=savedInstanceState.getInt("curChoice",0);
        }

        if(mDualPane){
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        showDetails(position);
    }
     void showDetails(int pos){
        mCurCheckPosition=pos;
         if(mDualPane){
             getListView().setItemChecked(pos, true);
             getListView().smoothScrollToPosition(pos);
             DetailFrag detail = (DetailFrag)getFragmentManager().findFragmentById(R.id.detail);
             if(detail==null||detail.getShownIndex()!=pos){
                detail=DetailFrag.newInstance(pos);
                getFragmentManager().beginTransaction()
                        .replace(R.id.detail,detail)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
             }
         }else{
             Intent intent = new Intent();
             intent.setClass(getActivity(),DetailActivity.class);
             intent.putExtra("index", pos);
             startActivity(intent);
         }
    }
}
